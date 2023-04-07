package eg.gov.iti.skyscanner.home.view

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import eg.gov.iti.skyscanner.DataBase.ConcreteLocalSource
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.databinding.FragmentHomeBinding
import eg.gov.iti.skyscanner.home.viewModel.HomeViewModel
import eg.gov.iti.skyscanner.home.viewModel.HomeViewModelFactory
import eg.gov.iti.skyscanner.models.MyIcons.replaceAPIIcon
import eg.gov.iti.skyscanner.models.Repository
import eg.gov.iti.skyscanner.models.WeatherDetail
import eg.gov.iti.skyscanner.network.APIClient
import eg.gov.iti.skyscanner.network.RequestState
import eg.gov.iti.skyscanner.settings.view.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.util.*

const val Latitude = "lat"
const val Longitude = "lon"
const val LongitudeMap = "lonMap"
const val LatitudeMap = "latMap"

class FragmentHome : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapterDailyRV: AdapterDailyRV
    lateinit var adapterHourlyRV: AdapterHourlyRV
    lateinit var viewModel: HomeViewModel
    lateinit var viewModelFactory: HomeViewModelFactory
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var lang: String = ""

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rootView=requireActivity().findViewById<View>(android.R.id.content)

        sharedPreference = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()

        val activity: Activity? = activity
        if (activity != null) {
            if (sharedPreference.getString(ActivityFlag, "fragFavourite").equals("fragFavourite")) {
                activity.title = getString(eg.gov.iti.skyscanner.R.string.favourites)
                Log.e("TAG", "homenamecheck1: ${sharedPreference.getString(ActivityFlag,"m")}" )
                // editor.putString(ActivityFlag, "fragHome").apply()
            } else if(sharedPreference.getString(ActivityFlag, "fragHome").equals("fragHome")){
                activity?.title = getString(eg.gov.iti.skyscanner.R.string.home)
                Log.e("TAG", "homenamecheck2: ${sharedPreference.getString(ActivityFlag,"m")}" )
            }
            else
            {
                activity?.title = getString(eg.gov.iti.skyscanner.R.string.home)
                Log.e("TAG", "homenamecheck3: ${sharedPreference.getString(ActivityFlag,"m")}" )
            }
        }

        var lat = sharedPreference.getString(Latitude, "N/v")?.toDoubleOrNull()
        var lon = sharedPreference.getString(Longitude, "N/v")?.toDoubleOrNull()

        var latMap = sharedPreference.getString(LatitudeMap, "N/v")?.toDoubleOrNull()
        var lonMap = sharedPreference.getString(LongitudeMap, "N/v")?.toDoubleOrNull()

        var lang = sharedPreference.getString(Language, "ar")
        var location = sharedPreference.getString(Location, "gps")
        var unit = sharedPreference.getString(TempUnit, "metric")
        var measureUnit = sharedPreference.getString(MeasureUnit, "m/s")
       var flagname= sharedPreference.getString(ActivityFlag, "fragHome")
       // Toast.makeText(requireContext(), "flagname " + flagname, Toast.LENGTH_SHORT).show()
        Log.e("TAG", "homename1: ${flagname}" )

        //Toast.makeText(requireContext(), "lang" + lang, Toast.LENGTH_SHORT).show()
        //checkLang(lang)
        viewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                APIClient.getInstance(), ConcreteLocalSource(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        if (isNetworkAvailable(requireContext())) {
            if (location.equals("map")) {
                if(flagname.equals("fragFavourite")){
                    Log.e("TAG", "homename2: ${flagname}" )
                    getSiutableData(lat, lon, lang, unit)
                    retrieveRetrofitData(lat, lon, lang, measureUnit, unit)
                    editor.putString(ActivityFlag, "fragHome").apply()
                    Log.e("TAG", "homename3:  ${sharedPreference.getString(ActivityFlag,"m")}" )
                }else {
                    getSiutableData(latMap, lonMap, lang, unit)
                    retrieveRetrofitData(latMap, lonMap, lang, measureUnit, unit)
                }
            }else if(location.equals("gps")){
               // activity?.recreate()
                Log.e("TAG", "homename4: ${flagname}" )
                getSiutableData(lat, lon, lang, unit)
                retrieveRetrofitData(lat, lon, lang, measureUnit, unit)
                editor.putString(ActivityFlag, "fragHome").apply()
                Log.e("TAG", "homename5:  ${sharedPreference.getString(ActivityFlag,"m")}" )

            }
        } else {
           // Toast.makeText(requireContext(),"You're offline, this is your stored data",Toast.LENGTH_SHORT).show()
            viewModel.getStoredWeather()
            lifecycleScope.launch() {
                viewModel.allWeatherFromRoom.collectLatest { db ->
                    when (db) {
                        is RequestState.Success -> {
                            val weather = db.data?.get(0)?.let {
                                WeatherDetail(
                                    it.alerts,
                                    it.current,
                                    it.daily,
                                    it.hourly,
                                    it.lat,
                                    it.lon,
                                    it.timezone,
                                    it.timezone_offset,
                                    it.isFav
                                )
                            }
                            if (unit != null) {
                                if (lang != null) {
                                    if (weather != null) {
                                        if (measureUnit != null) {
                                            displayRVDay(weather, unit, lang)
                                            displayRVHour(weather, unit, lang)
                                            displayWeather(weather, unit, measureUnit, lang)
                                        }
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
            Snackbar.make(
                rootView,
                R.string.offline,
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }

    fun retrieveRetrofitData(lat: Double?, lon: Double?, lang: String?,measureUnit: String?, unit: String?){
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

                        viewModel.insertWeather(result.data)
                        if (unit != null) {
                            if (lang != null) {
                                displayRVDay(result.data, unit, lang)
                                displayRVHour(result.data, unit, lang)
                            }
                            if (measureUnit != null) {
                                if (lang != null) {
                                    displayWeather(result.data, unit, measureUnit, lang)
                                }
                            }
                        }

                    }
                    else -> {
                        binding.scrollView.visibility = View.GONE
                        binding.pBar.visibility = View.GONE
                        binding.iconFail.visibility = View.VISIBLE
                        Snackbar.make(requireContext(),requireView(),
                            R.string.problem.toString(), Snackbar.LENGTH_SHORT).show()
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

    private fun displayWeather(
        weatherDetail: WeatherDetail,
        unit: String,
        measurement: String,
        lang: String
    ) {
        //Toast.makeText(requireContext(),"lon-"+lon.toString()+weatherDetail.lat.toString(),Toast.LENGTH_SHORT).show()
        val formatter = NumberFormat.getInstance(Locale(lang))
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: MutableList<Address> = geocoder.getFromLocation(
            weatherDetail.lat, weatherDetail.lon, 1
        ) as MutableList<Address>
        replaceAPIIcon(weatherDetail.current.weather[0].icon, binding.imgW)
        binding.txtTempState.text = weatherDetail.current.weather[0].description
        binding.txtDate.text = date(lang)
        binding.txtPressure.text = "${weatherDetail.current.pressure} hpa"
        binding.txtHumidity.text = "${weatherDetail.current.humidity} %"
        binding.txttCloud.text = "${weatherDetail.current.clouds} %"
        binding.txttUltraViolet.text = weatherDetail.current.uvi.toString()
        binding.txttVisibility.text = "${weatherDetail.current.visibility} m"
        when (unit) {
            "metric" -> {
                val formattedNumber = formatter.format(weatherDetail.current.temp)
                binding.txtTemp.text = "${formattedNumber}°C"
                if (measurement.equals("m/s")) {
                    binding.txtWind.text = "${weatherDetail.current.wind_speed} m/s"
                } else {
                    binding.txtWind.text =
                        "${convertWindSpeedMpH(weatherDetail.current.wind_speed).toInt()} m/h"
                }
            }
            "imperial" -> {
                val formattedNumber = formatter.format(weatherDetail.current.temp)
                binding.txtTemp.text = "${formattedNumber}°f"
                if (measurement.equals("m/s")) {
                    binding.txtWind.text =
                        "${convertWindSpeedMpS(weatherDetail.current.wind_speed).toInt()} m/s"
                } else {
                    binding.txtWind.text = "${weatherDetail.current.wind_speed} m/h"
                }
            }
            else -> {
                val formattedNumber = formatter.format(weatherDetail.current.temp)
                binding.txtTemp.text = "${formattedNumber}°k"
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

    private fun displayRVDay(weatherDetail: WeatherDetail, unit: String, lang: String) {
        adapterDailyRV = AdapterDailyRV(weatherDetail, requireContext(), unit, lang)
        binding.RVDaily.adapter = adapterDailyRV
    }

    private fun displayRVHour(weatherDetail: WeatherDetail, unit: String, lang: String) {
        adapterHourlyRV = AdapterHourlyRV(weatherDetail, requireContext(), unit, lang)
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

    fun date(lang: String): String? {
        val currentDateTime = LocalDateTime.now()
        val locale = Locale(lang)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(locale)
            .withDecimalStyle(DecimalStyle.of(locale))
        val formattedDateTime = currentDateTime.format(formatter)
        return formattedDateTime
    }
}