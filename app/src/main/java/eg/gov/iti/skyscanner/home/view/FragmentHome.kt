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
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.databinding.FragmentHomeBinding
import eg.gov.iti.skyscanner.home.viewModel.HomeViewModel
import eg.gov.iti.skyscanner.home.viewModel.HomeViewModelFactory
import eg.gov.iti.skyscanner.models.MyIcons
import eg.gov.iti.skyscanner.models.Repository
import eg.gov.iti.skyscanner.models.WeatherDetail
import eg.gov.iti.skyscanner.network.APIClient
import eg.gov.iti.skyscanner.network.RequestState
import eg.gov.iti.skyscanner.settings.view.Language
import eg.gov.iti.skyscanner.settings.view.Unit
import kotlinx.coroutines.launch
import java.util.*

const val Latitude="lat"
const val Longitude="lon"
class FragmentHome : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapterDailyRV: AdapterDailyRV
    lateinit var adapterHourlyRV: AdapterHourlyRV
    lateinit var viewModel: HomeViewModel
    lateinit var viewModelFactory: HomeViewModelFactory
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var icon: MyIcons = MyIcons()

    override fun onResume() {
        super.onResume()
        val activity: Activity? = activity
        if (activity != null) {
            activity.title = getString(eg.gov.iti.skyscanner.R.string.home)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
        var lat = sharedPreference.getString(Latitude, "N/v")?.toDoubleOrNull()
        var lon = sharedPreference.getString(Longitude, "N/v")?.toDoubleOrNull()

        var lang = sharedPreference.getString(Language, "en")
        var unit = sharedPreference.getString(Unit, "metric")

        Toast.makeText(requireContext(),"unit-"+unit,Toast.LENGTH_SHORT).show()

        viewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                APIClient.getInstance(),
                ConcreteLocalSource(requireContext())
            )
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        getSiutableData(lat,lon,lang,unit)

        lifecycleScope.launch() {
            viewModel.weather.collect { result ->
                when (result) {
                    is RequestState.Loading -> {
                        binding.scrollView.visibility = View.GONE
                        binding.iconFail.visibility = View.GONE
                        binding.pBar.visibility = View.VISIBLE
                    }
                    is RequestState.Success -> {
                        binding.scrollView.visibility = View.VISIBLE
                        binding.pBar.visibility = View.GONE
                        binding.iconFail.visibility = View.GONE
                        if (unit != null) {
                            displayRVHour(result.data, unit)
                            displayRVDay(result.data, unit)
                            displayWeather(result.data, unit)
                        }

                    }
                    else -> {
                        binding.scrollView.visibility = View.GONE
                        binding.pBar.visibility = View.GONE
                        binding.iconFail.visibility = View.VISIBLE
                        Toast.makeText(
                            requireContext(), "Check Your Internet Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }

    private fun getSiutableData(lat: Double?, lon: Double?, lang: String?, unit: String?) {
//        var currentLang:String
//        var currentUnit:String
    //    Toast.makeText(requireContext(),"unit : "+unit,Toast.LENGTH_SHORT).show()
        if (unit.equals("kelvin")) {
            if (lat != null) {
                if (lon != null) {
                    if (lang != null) {
                        if (unit != null) {
                            viewModel.getRemoteWeatherKelvin(
                                lat,
                                lon,
                                lang,
                                "783ffb0fa4b28b09291b839b6ad74d26"
                            )
                        }
                    }
                }
            }
        }
        else {
            if (lat != null) {
                if (lon != null) {
                    if (lang != null) {
                        if (unit != null) {
                            viewModel.getRemoteWeather(
                                lat,
                                lon,
                                unit,
                                lang,
                                "783ffb0fa4b28b09291b839b6ad74d26"
                            )
                        }
                    }
                }
            }
        }
    }
    private fun displayWeather(weatherDetail: WeatherDetail, unit: String) {
         //Toast.makeText(requireContext(),"lon-"+lon.toString()+weatherDetail.lat.toString(),Toast.LENGTH_SHORT).show()
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: MutableList<Address> =
            geocoder.getFromLocation(
                weatherDetail.lat,
                weatherDetail.lon,
                1
            ) as MutableList<Address>
        icon.replaceAPIIcon(weatherDetail.current.weather[0].icon, binding.imgW)
        binding.txtTempState.text = weatherDetail.current.weather[0].description
        binding.txtWind.text = weatherDetail.current.wind_speed.toString()
        binding.txtPressure.text = "${weatherDetail.current.pressure} hpa"
        binding.txtHumidity.text = "${weatherDetail.current.humidity}%"
        binding.txttCloud.text ="${weatherDetail.current.clouds}%"
        binding.txttUltraViolet.text = weatherDetail.current.uvi.toString()
        binding.txttVisibility.text = "${weatherDetail.current.visibility}"
        when (unit) {
            "metric" -> {
                binding.txtTemp.text = "${weatherDetail.current.temp}°C"
            }
            "imperial" -> {
                binding.txtTemp.text = "${weatherDetail.current.temp}°f"
            }
            else -> {
                binding.txtTemp.text = "${weatherDetail.current.temp}°k"
            }
        }


        if (addresses.isNotEmpty()) {
            binding.txtLocate.text = "${addresses[0].locality},${addresses[0].countryName}"
            // ${addresses[0].adminArea},
        } else {
            Toast.makeText(requireContext(), "empty", Toast.LENGTH_SHORT).show()
        }

    }
    private fun displayRVDay(weatherDetail: WeatherDetail,unit: String) {
        adapterDailyRV = AdapterDailyRV(weatherDetail, requireContext(),unit)
        binding.RVDaily.adapter = adapterDailyRV
    }
    private fun displayRVHour(weatherDetail: WeatherDetail,unit: String) {
        adapterHourlyRV = AdapterHourlyRV(weatherDetail, requireContext(),unit)
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