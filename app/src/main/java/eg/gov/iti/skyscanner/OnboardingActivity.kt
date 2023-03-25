package eg.gov.iti.skyscanner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import eg.gov.iti.skyscanner.databinding.ActivityOnboardingBinding
import eg.gov.iti.skyscanner.mainactivity.view.MainActivity

//const val Permission_1 = 10

class OnboardingActivity : AppCompatActivity() {
    /*var lat: Double = 0.0
    var lon: Double = 0.0*/
    lateinit var binding: ActivityOnboardingBinding
   // lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        /*       val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
               val locationListener = LocationListener { TODO("Not yet implemented") }
       */
        binding.btnSetUpDone.setOnClickListener {
            if (binding.rbgps.isChecked) {
                val preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                preferences.edit().putBoolean("onboarding_completed", true).apply()
                /* val x=lat.toString()
                 val y=lon.toString()

                 Toast.makeText(applicationContext,x+y,Toast.LENGTH_SHORT).show()

                 preferences.edit().putString("lat", x).apply()
                 preferences.edit().putString("lon", y).apply()*/
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(applicationContext, "gps", Toast.LENGTH_SHORT).show()
            } else if (binding.rbMap.isChecked) {
                Toast.makeText(applicationContext, "map", Toast.LENGTH_SHORT).show()
            }
        }
    }

/*    private fun checkLocationPermission() {
        val task =fusedLocationProviderClient.lastLocation
       if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
           != PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
           != PackageManager.PERMISSION_GRANTED
       ){
           ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
           return
       }
      task.addOnSuccessListener {
          if(it !=null){
              Toast.makeText(applicationContext,"${it.latitude} ${it.longitude}",Toast.LENGTH_SHORT).show()
          }
      }
    }*/


    /*private fun checkPermissions(): Boolean {
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
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
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
                Toast.makeText(applicationContext, "lat:$lat", Toast.LENGTH_SHORT).show()
                // latTextView.text = mLastLocation.latitude.toString()
            }
            if (mLastLocation != null) {
                lon = mLastLocation.longitude
                Toast.makeText(applicationContext, "lon:$lon", Toast.LENGTH_SHORT).show()
                //   lonTextView.text = mLastLocation.longitude.toString()
            }


        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }*/
}
