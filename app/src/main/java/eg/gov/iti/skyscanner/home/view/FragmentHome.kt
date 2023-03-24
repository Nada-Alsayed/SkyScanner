package eg.gov.iti.skyscanner.home.view

import android.R
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eg.gov.iti.skyscanner.DataBase.ConcreteLocalSource
import eg.gov.iti.skyscanner.databinding.FragmentHomeBinding
import eg.gov.iti.skyscanner.home.viewModel.HomeViewModel
import eg.gov.iti.skyscanner.home.viewModel.HomeViewModelFactory
import eg.gov.iti.skyscanner.models.Repository
import eg.gov.iti.skyscanner.models.WeatherDetail
import eg.gov.iti.skyscanner.network.APIClient
import eg.gov.iti.skyscanner.network.RequestState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FragmentHome : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapterDailyRV: AdapterDailyRV
    lateinit var adapterHourlyRV: AdapterHourlyRV
//    lateinit var rvDay:RecyclerView
//    lateinit var rvHour:RecyclerView
    lateinit var viewModel: HomeViewModel
    lateinit var viewModelFactory: HomeViewModelFactory
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
  //  lateinit var weatherDetail: WeatherDetail
    override fun onResume() {
        super.onResume()
        val activity: Activity? = activity
        if (activity != null) {
            activity.title = getString(eg.gov.iti.skyscanner.R.string.home)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lat = sharedPreference.getFloat("lat", 0F)
        val lon = sharedPreference.getFloat("lon", 0F)
        viewModelFactory= HomeViewModelFactory(
            Repository.getInstance(APIClient.getInstance(),
            ConcreteLocalSource(requireContext())))
        viewModel=ViewModelProvider(this,viewModelFactory).get(HomeViewModel::class.java)
//        binding.RVDaily.adapter=adapterDailyRV
//        binding.RVHourly.adapter=adapterHourlyRV

        viewModel.getRemoteWeather(lat,lon,"metric","en","40dac0af7018969cbb541943f944ba29")
        lifecycleScope.launch(){
            viewModel.weather.collect{
                result->
                when(result){
                    is RequestState.Loading->{}
                    is RequestState.Success->{
                        displayRVHour(result.data,requireContext())
                        displayRVDay(result.data,requireContext())
                        displayWeather(result.data,requireContext())
                    }
                    else->{
                        Toast.makeText(requireContext(),"Check Your Internet Connection",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun displayWeather(weatherDetail: WeatherDetail,context: Context) {
        Glide.with(context)
            .load(weatherDetail.current.weather.get(0).icon)
            .into(binding.imgW);
        binding.txtTemp.text=weatherDetail.current.temp.toString()
        binding.txtTempState.text=weatherDetail.current.weather.get(0).description


    }

    private fun displayRVDay(weatherDetail: WeatherDetail,context: Context) {
        adapterDailyRV= AdapterDailyRV(weatherDetail,requireContext())
        binding.RVDaily.adapter=adapterDailyRV
    }

    private fun displayRVHour(weatherDetail: WeatherDetail,context: Context) {
        adapterHourlyRV= AdapterHourlyRV(weatherDetail,requireContext())
        //adapterHourlyRV.setList(weatherDetail)
        binding.RVHourly.adapter=adapterHourlyRV
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }
}