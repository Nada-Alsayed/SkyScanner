package eg.gov.iti.skyscanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import eg.gov.iti.skyscanner.databinding.ActivityOnboardingBinding
import eg.gov.iti.skyscanner.mainactivity.view.MainActivity
import eg.gov.iti.skyscanner.map.MapsActivity
import eg.gov.iti.skyscanner.settings.view.Location

const val OnBoardingPref="onBoarding_completed"
class OnBoardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        binding.btnSetUpDone.setOnClickListener {
            if (binding.rbgps.isChecked) {
                sharedPreferences.edit().putBoolean(OnBoardingPref, true).apply()
                sharedPreferences.edit().putString(Location,"gps").apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(applicationContext, "gps", Toast.LENGTH_SHORT).show()
            } else if (binding.rbMap.isChecked) {
                sharedPreferences.edit().putBoolean(OnBoardingPref, true).apply()
                sharedPreferences.edit().putString(Location,"map").apply()
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(applicationContext, "map", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
