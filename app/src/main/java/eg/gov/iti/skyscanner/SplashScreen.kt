package eg.gov.iti.skyscanner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import eg.gov.iti.skyscanner.databinding.ScreenSplashBinding

class SplashScreen : AppCompatActivity() {
    lateinit var bindingSplashScreen :ScreenSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSplashScreen= ScreenSplashBinding.inflate(layoutInflater)
        setContentView(bindingSplashScreen.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1900)
    }
}