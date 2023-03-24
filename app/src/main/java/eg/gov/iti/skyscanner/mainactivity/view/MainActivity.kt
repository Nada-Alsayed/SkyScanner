package eg.gov.iti.skyscanner.mainactivity.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import eg.gov.iti.skyscanner.OnboardingActivity
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toolbar: Toolbar
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get instance of SharedPreferences
        val preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        // Get the value of onboarding_completed key. If it is not set, then it will return false
        val onBoardingCompleted = preferences.getBoolean("onboarding_completed", false)
        // Check the value of onboardingCompleted
        if (!onBoardingCompleted) {
            // Show OnboardingActivity because it is the first time user opens the app
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            /*  Do something else because user has already completed onboarding


            dialog= Dialog(this)
         dialog.setCancelable(false)
         dialog.setContentView(R.layout.weather_popup_win)
         dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
         dialog.show()*/

            //ToolBar
            toolbar = findViewById(R.id.toolBar)
            setSupportActionBar(toolbar)

            toggle = ActionBarDrawerToggle(
                this,
                binding.navDrawerLayout,
                toolbar,
                R.string.home,
                R.string.settings
            )
            binding.navDrawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            toolbar.setTitleTextColor(getColor(R.color.white))

            //supportActionBar?.setCustomView(R.layout.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(true)
            //mTitle=toolbar.findViewById(R.id.toolbar_title)
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
            NavigationUI.setupWithNavController(binding.navView, navController)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (binding.navDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.navDrawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.navDrawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


/* val actionBar = supportActionBar
 if (actionBar != null) {
     Toast.makeText(this,"jjjj",Toast.LENGTH_SHORT).show()
     actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
     actionBar.setDisplayShowHomeEnabled(true)
     actionBar.setDisplayHomeAsUpEnabled(true)
 }
*/