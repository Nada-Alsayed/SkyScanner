package eg.gov.iti.skyscanner.home.view

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import eg.gov.iti.skyscanner.DataBase.ConcreteLocalSource
import eg.gov.iti.skyscanner.databinding.FragmentHomeBinding
import eg.gov.iti.skyscanner.home.viewModel.HomeViewModel
import eg.gov.iti.skyscanner.home.viewModel.HomeViewModelFactory
import eg.gov.iti.skyscanner.models.MyIcons
import eg.gov.iti.skyscanner.models.Repository
import eg.gov.iti.skyscanner.models.WeatherDetail
import eg.gov.iti.skyscanner.network.APIClient
import eg.gov.iti.skyscanner.network.RequestState
import kotlinx.coroutines.launch
import java.util.*


class FragmentHome : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapterDailyRV: AdapterDailyRV
    lateinit var adapterHourlyRV: AdapterHourlyRV

    lateinit var viewModel: HomeViewModel
    lateinit var viewModelFactory: HomeViewModelFactory
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var icon: MyIcons= MyIcons()

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
        var lat= sharedPreference.getString("lat", "N/v")
        var lon =sharedPreference.getString("lon", "N/v")

        var actLat= lat?.toDoubleOrNull()
        var actLon= lon?.toDoubleOrNull()

        viewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                APIClient.getInstance(),
                ConcreteLocalSource(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
//        binding.RVDaily.adapter=adapterDailyRV
//        binding.RVHourly.adapter=adapterHourlyRV


        if (actLat != null) {
            if (actLon != null) {
                viewModel.getRemoteWeather(actLat, actLon, "metric", "en", "783ffb0fa4b28b09291b839b6ad74d26")
            }
        }

        lifecycleScope.launch() {
            viewModel.weather.collect { result ->
                when (result) {
                    is RequestState.Loading -> {
                        binding.scrollView.visibility=View.GONE
                        binding.iconFail.visibility=View.GONE
                        binding.pBar.visibility=View.VISIBLE
                    }
                    is RequestState.Success -> {
                        binding.scrollView.visibility=View.VISIBLE
                        binding.pBar.visibility=View.GONE
                        binding.iconFail.visibility=View.GONE
                        displayRVHour(result.data)
                        displayRVDay(result.data)
                        if (actLat != null) {
                            if (actLon != null) {
                                displayWeather(result.data,actLat,actLon)
                            }
                        }
                    }
                    else -> {
                        binding.scrollView.visibility=View.GONE
                        binding.pBar.visibility=View.GONE
                        binding.iconFail.visibility=View.VISIBLE
                        Toast.makeText(
                            requireContext(), "Check Your Internet Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }

    private fun displayWeather(weatherDetail: WeatherDetail,lat:Double,lon:Double) {
        Toast.makeText(requireContext(),"lon-"+lon.toString()+weatherDetail.lat.toString(),Toast.LENGTH_SHORT).show()
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: MutableList<Address> =
            geocoder.getFromLocation(weatherDetail.lat,weatherDetail.lon, 1) as MutableList<Address>
        icon.replaceAPIIcon( weatherDetail.current.weather[0].icon,binding.imgW)
        binding.txtTemp.text = weatherDetail.current.temp.toString()
        binding.txtTempState.text = weatherDetail.current.weather[0].description
        binding.txtWind.text = weatherDetail.current.wind_speed.toString()
        binding.txtPressure.text = weatherDetail.current.pressure.toString()
        binding.txtHumidity.text=weatherDetail.current.humidity.toString()
        binding.txttCloud.text=weatherDetail.current.clouds.toString()
        binding.txttUltraViolet.text=weatherDetail.current.uvi.toString()
        binding.txttVisibility.text=weatherDetail.current.visibility.toString()
        if (addresses.isNotEmpty()) {
            binding.txtLocate.text= addresses[0].countryName.toString()
        }else{
            Toast.makeText(requireContext(),"empty",Toast.LENGTH_SHORT).show()
        }

    }

    private fun displayRVDay(weatherDetail: WeatherDetail) {
        adapterDailyRV = AdapterDailyRV(weatherDetail, requireContext())
        binding.RVDaily.adapter = adapterDailyRV
    }

    private fun displayRVHour(weatherDetail: WeatherDetail) {
        adapterHourlyRV = AdapterHourlyRV(weatherDetail, requireContext())
        binding.RVHourly.adapter = adapterHourlyRV
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}