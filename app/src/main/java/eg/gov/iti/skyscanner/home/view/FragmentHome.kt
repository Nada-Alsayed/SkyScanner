package eg.gov.iti.skyscanner.home.view

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
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
import eg.gov.iti.skyscanner.settings.view.Language
import eg.gov.iti.skyscanner.settings.view.MeasureUnit
import eg.gov.iti.skyscanner.settings.view.TempUnit
import kotlinx.coroutines.launch
import java.util.*

const val Latitude = "lat"
const val Longitude = "lon"

class FragmentHome : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapterDailyRV: AdapterDailyRV
    lateinit var adapterHourlyRV: AdapterHourlyRV
    lateinit var viewModel: HomeViewModel
    lateinit var viewModelFactory: HomeViewModelFactory
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var icon: MyIcons = MyIcons()
    var lang: String = ""

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

        var lang = sharedPreference.getString(Language, "ar")
        var unit = sharedPreference.getString(TempUnit, "metric")
        var measureUnit = sharedPreference.getString(MeasureUnit, "m/s")

        Toast.makeText(requireContext(), "lat-" + lat, Toast.LENGTH_SHORT).show()
        //checkLang(lang)
        viewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                APIClient.getInstance(), ConcreteLocalSource(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        if (isNetworkAvailable(requireContext())) {
            getSiutableData(lat, lon, lang, unit)
            // getSiutableData(33.44, -94.04, lang, unit)
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
                            viewModel.deleteAll()
                            viewModel.insertWeather(result.data)
                            if (unit != null) {
                                displayRVHour(result.data, unit)
                                displayRVDay(result.data, unit)
                                if (measureUnit != null) {
                                    displayWeather(result.data, unit, measureUnit)
                                }
                            }

                        }
                        else -> {
                            binding.scrollView.visibility = View.GONE
                            binding.pBar.visibility = View.GONE
                            binding.iconFail.visibility = View.VISIBLE
                            Toast.makeText(
                                requireContext(),
                                "Check Your Internet Connection",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        }
        else {
             /*Snackbar.make(binding.root, "you're offline, Check your network", Snackbar.LENGTH_LONG)
                 .show()*/
            Toast.makeText(
                requireContext(),
                "you're offline, Check your network",
                Toast.LENGTH_SHORT
            )
                .show()

            viewModel.getStoredWeather()
            lifecycleScope.launch {
                viewModel.allWeatherFromRoom.collect() { db ->
                    if (db != null) {
                        val weather = WeatherDetail(
                            db[0].alerts,
                            db[0].current,
                            db[0].daily,
                            db[0].hourly,
                            db[0].lat,
                            db[0].lon,
                            db[0].timezone,
                            db[0].timezone_offset,
                            db[0].isFav
                        )
                        if (unit != null) {
                            displayRVHour(weather, unit)
                            displayRVDay(weather, unit)
                            if (measureUnit != null) {
                                displayWeather(weather, unit, measureUnit)
                            }
                        }
                    }
                }
            }
        }

    }

    fun convertWindSpeedMpS(mph: Double): Double {
        val mps = mph / 2.23694 // conversion factor from mph to m/s
        return mps
    }

    fun convertWindSpeedMpH(mps: Double): Double {
        val mph = mps * 2.23694 // conversion factor from m/s to mph
        return mph
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
                                lat, lon, lang, "783ffb0fa4b28b09291b839b6ad74d26"
                            )
                        }
                    }
                }
            }
        } else {
            if (lat != null) {
                if (lon != null) {
                    if (lang != null) {
                        if (unit != null) {
                            viewModel.getRemoteWeather(
                                lat, lon, unit, lang, "783ffb0fa4b28b09291b839b6ad74d26"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun displayWeather(weatherDetail: WeatherDetail, unit: String, measurement: String) {
        //Toast.makeText(requireContext(),"lon-"+lon.toString()+weatherDetail.lat.toString(),Toast.LENGTH_SHORT).show()
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: MutableList<Address> = geocoder.getFromLocation(
            weatherDetail.lat, weatherDetail.lon, 1
        ) as MutableList<Address>
        icon.replaceAPIIcon(weatherDetail.current.weather[0].icon, binding.imgW)
        binding.txtTempState.text = weatherDetail.current.weather[0].description

        binding.txtPressure.text = "${weatherDetail.current.pressure} hpa"
        binding.txtHumidity.text = "${weatherDetail.current.humidity} %"
        binding.txttCloud.text = "${weatherDetail.current.clouds} %"
        binding.txttUltraViolet.text = weatherDetail.current.uvi.toString()
        binding.txttVisibility.text = "${weatherDetail.current.visibility} m"
        when (unit) {
            "metric" -> {
                binding.txtTemp.text = "${weatherDetail.current.temp}°C"
                if (measurement.equals("m/s")) {
                    binding.txtWind.text = "${weatherDetail.current.wind_speed} m/s"
                } else {
                    binding.txtWind.text =
                        "${convertWindSpeedMpH(weatherDetail.current.wind_speed).toInt()} m/h"
                }
            }
            "imperial" -> {
                binding.txtTemp.text = "${weatherDetail.current.temp}°f"
                if (measurement.equals("m/s")) {
                    binding.txtWind.text =
                        "${convertWindSpeedMpS(weatherDetail.current.wind_speed).toInt()} m/s"
                } else {
                    binding.txtWind.text = "${weatherDetail.current.wind_speed} m/h"
                }
            }
            else -> {
                binding.txtTemp.text = "${weatherDetail.current.temp}°k"
                if (measurement.equals("m/s")) {
                    binding.txtWind.text = "${weatherDetail.current.wind_speed} m/s"
                } else {
                    binding.txtWind.text =
                        "${convertWindSpeedMpH(weatherDetail.current.wind_speed).toInt()} m/h"
                }
            }
        }


        if (addresses.isNotEmpty()) {
            binding.txtLocate.text = "${addresses[0].locality},${addresses[0].countryName}"
            // ${addresses[0].adminArea},
        } else {
            Toast.makeText(requireContext(), "empty", Toast.LENGTH_SHORT).show()
        }

    }

    private fun displayRVDay(weatherDetail: WeatherDetail, unit: String) {
        adapterDailyRV = AdapterDailyRV(weatherDetail, requireContext(), unit)
        binding.RVDaily.adapter = adapterDailyRV
    }

    private fun displayRVHour(weatherDetail: WeatherDetail, unit: String) {
        adapterHourlyRV = AdapterHourlyRV(weatherDetail, requireContext(), unit)
        binding.RVHourly.adapter = adapterHourlyRV
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    /*fun checkLang(lang: String?) {
        val locale = Locale(lang)

        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
    }*/
}