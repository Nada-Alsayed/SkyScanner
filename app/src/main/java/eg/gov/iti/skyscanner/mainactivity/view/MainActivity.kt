package eg.gov.iti.skyscanner.mainactivity.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.location.*
import eg.gov.iti.skyscanner.LanguageManager
import eg.gov.iti.skyscanner.OnBoardingActivity
import eg.gov.iti.skyscanner.OnBoardingPref
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.databinding.ActivityMainBinding
import eg.gov.iti.skyscanner.home.view.Latitude
import eg.gov.iti.skyscanner.home.view.Longitude
import eg.gov.iti.skyscanner.settings.view.Language
import eg.gov.iti.skyscanner.settings.view.Location
import java.util.*

const val Permission_1 = 10

class MainActivity : AppCompatActivity() {
    var lat: Double = 0.0
    var lon: Double = 0.0
    lateinit var binding: ActivityMainBinding
    lateinit var toolbar: Toolbar
    lateinit var toggle: ActionBarDrawerToggle
   // lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val onBoardingCompleted = sharedPreferences.getBoolean(OnBoardingPref, false)
        var lang = sharedPreferences.getString(Language, "en")

      //  Toast.makeText(this,lang,Toast.LENGTH_SHORT).show()
        if (lang.equals("ar")){
            LanguageManager.setLanguage(this,"ar")
        }else{
            LanguageManager.setLanguage(this,"en")
        }
        if (!onBoardingCompleted) {
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            if (sharedPreferences.getString(Location, "gps").equals("gps")) {
             //   getLastLocation()
            }
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
            supportActionBar?.setDisplayShowTitleEnabled(true)
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
            NavigationUI.setupWithNavController(binding.navView, navController)
        }
    }
    override fun onResume() {
        super.onResume()
        /*if (sharedPreferences.getString(Location, "gps").equals("gps")) {
            if (checkPermissions())
                getLastLocation()
        }*/

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

 /*   private fun checkPermissions(): Boolean {
        val result = ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return result
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            Permission_1
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(0)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            if (mLastLocation != null) {
                lat = mLastLocation.latitude
                // Toast.makeText(applicationContext, "lat:$lat", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putString(Latitude, lat.toString()).apply()
                lon = mLastLocation.longitude
                // Toast.makeText(applicationContext, "lon:$lon", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putString(Longitude, lon.toString()).apply()
            }
        }
    }
*/
 /*   private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }*/

}
