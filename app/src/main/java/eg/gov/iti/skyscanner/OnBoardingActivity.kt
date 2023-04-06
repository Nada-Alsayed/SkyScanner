package eg.gov.iti.skyscanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import eg.gov.iti.skyscanner.databinding.ActivityOnboardingBinding
import eg.gov.iti.skyscanner.mainactivity.view.MainActivity
import eg.gov.iti.skyscanner.map.MapsActivity
import eg.gov.iti.skyscanner.settings.view.Language
import eg.gov.iti.skyscanner.settings.view.Location
import eg.gov.iti.skyscanner.settings.view.Notification

const val OnBoardingPref = "onBoarding_completed"

class OnBoardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(Notification, "enable").apply()
        sharedPreferences.edit().putString(Location, "gps").apply()
        sharedPreferences.edit().putString(Language, "en").apply()

        binding.switch1.isChecked = true

        binding.btnSetUpDone.setOnClickListener {
            binding.switch1.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Switch is on
                    sharedPreferences.edit().putString(Notification, "enable").apply()
                } else {
                    // Switch is off
                    sharedPreferences.edit().putString(Notification, "disable").apply()
                }
            }
            sharedPreferences.edit().putBoolean(OnBoardingPref, true).apply()
            if (binding.rbgps.isChecked) {
                sharedPreferences.edit().putString(Location, "gps").apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                //Toast.makeText(applicationContext, "gps", Toast.LENGTH_SHORT).show()
            } else if (binding.rbMap.isChecked) {
                sharedPreferences.edit().putString(Location, "map").apply()
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
                //Toast.makeText(applicationContext, "map", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
