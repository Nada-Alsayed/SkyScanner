package eg.gov.iti.skyscanner.settings.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import eg.gov.iti.skyscanner.LanguageManager
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.databinding.FragmentSettingBinding
import eg.gov.iti.skyscanner.map.MapsActivity

const val Language = "lang"
const val TempUnit = "unit"
const val Notification = "notification"
const val Location = "location"
const val MeasureUnit="measure"

class FragmentSetting : Fragment() {
    lateinit var binding: FragmentSettingBinding
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onResume() {
        super.onResume()
        val activity: Activity? = activity
        if (activity != null) {
            activity.title = getString(eg.gov.iti.skyscanner.R.string.settings)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
       // Falerinheit =Imperial: miles/hour
        var lang = sharedPreference.getString(Language, "en")
        var temp = sharedPreference.getString(TempUnit, "metric")
        var notif = sharedPreference.getString(Notification, "enable")
        var loca = sharedPreference.getString(Location, "gps")
        var measure = sharedPreference.getString(MeasureUnit, "m/s")

        when (lang) {
            "en" -> {
                binding.rgLang.check(R.id.rbEnglish)
            }
            "ar"-> {
                binding.rgLang.check(R.id.rbArabic)
            }
        }
        when (temp) {
            "metric" -> {
                binding.rgTemperature.check(R.id.rbCelsius)
            }
            "imperial" -> {
                binding.rgTemperature.check(R.id.rbFahrenheit)
            }
           "kelvin" -> {
               binding.rgTemperature.check(R.id.rbKelvin)
            }
        }

        when (notif) {
            "enable" -> {
                binding.rgNotification.check(R.id.rbEnable)
            }
            "disable" -> {
                binding.rgNotification.check(R.id.rbDisable)
            }
        }

        when (loca) {
            "gps" -> {
                binding.rgLocation.check(R.id.rbGPS)
            }
            "map" -> {
                binding.rgLocation.check(R.id.rbMap)
            }
        }

        when (measure) {
            "m/s" -> {
                binding.rgMeasure.check(R.id.rbMeterSec)
            }
            "m/h" -> {
                binding.rgMeasure.check(R.id.rbMilesHour)
            }
        }

        binding.rgLang.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rbArabic -> {
                        editor.putString(Language, "ar").apply()
                        activity?.recreate()
                        activity?.recreate()
                        activity?.recreate()
                        //changeLanguage("ar")
                    }
                    R.id.rbEnglish -> {
                        editor.putString(Language, "en").apply()
                        activity?.recreate()
                        activity?.recreate()
                        activity?.recreate()
                       // changeLanguage("en")
                    }
                }
            }
        }

        binding.rgTemperature.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rbCelsius -> {
                        editor.putString(TempUnit, "metric").apply()
                    }
                    R.id.rbFahrenheit -> {
                        editor.putString(TempUnit, "imperial").apply()
                    }
                    R.id.rbKelvin -> {
                        editor.putString(TempUnit, "kelvin").apply()
                    }
                }
            }
        }

        binding.rgLocation.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rbGPS -> {
                        editor.putString(Location, "gps").apply()
                    }
                    R.id.rbMap -> {
                        editor.putString(Location, "map").apply()
                        val intent = Intent(requireContext(), MapsActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }
            }
        }

        binding.rgNotification.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rbEnable -> {
                        editor.putString(Notification, "enable").apply()
                    }
                    R.id.rbDisable -> {
                        editor.putString(Notification, "disable").apply()
                    }
                }
            }
        }
        binding.rgMeasure.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rbMeterSec -> {
                        editor.putString(MeasureUnit, "m/s").apply()
                    }
                    R.id.rbMilesHour -> {
                        editor.putString(MeasureUnit, "m/h").apply()
                    }
                }
            }
        }
    }
    fun changeLanguage(language: String) {
        activity?.let { LanguageManager.setLanguage(it, language) }
        activity?.recreate()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

}