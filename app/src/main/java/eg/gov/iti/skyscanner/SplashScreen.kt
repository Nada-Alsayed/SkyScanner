package eg.gov.iti.skyscanner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import eg.gov.iti.skyscanner.databinding.ScreenSplashBinding
import eg.gov.iti.skyscanner.mainactivity.view.MainActivity
import eg.gov.iti.skyscanner.settings.view.Language

class SplashScreen : AppCompatActivity() {
    lateinit var bindingSplashScreen :ScreenSplashBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSplashScreen= ScreenSplashBinding.inflate(layoutInflater)
        setContentView(bindingSplashScreen.root)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        var lang = sharedPreferences.getString(Language, "en")
        Toast.makeText(this,lang,Toast.LENGTH_SHORT).show()
        if (lang.equals("ar")){
            LanguageManager.setLanguage(this,"ar")
        }else{
            LanguageManager.setLanguage(this,"en")
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1900)
    }
}