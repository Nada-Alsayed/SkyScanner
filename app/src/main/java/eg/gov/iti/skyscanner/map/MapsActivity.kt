package eg.gov.iti.skyscanner.map

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.databinding.ActivityMapsBinding
import eg.gov.iti.skyscanner.home.view.Latitude
import eg.gov.iti.skyscanner.home.view.Longitude
import eg.gov.iti.skyscanner.mainactivity.view.MainActivity
import eg.gov.iti.skyscanner.settings.view.ActivityFlag
import java.io.IOException
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        var latLng = LatLng(-34.0, 151.0)
        mMap = googleMap
        mMap.setOnMapClickListener(GoogleMap.OnMapClickListener {
            /*Toast.makeText(this, "lat " + it.latitude + "lon " + it.longitude, Toast.LENGTH_SHORT)
                .show()*/
            latLng = LatLng(it.latitude, it.longitude)
            sharedPreference.edit().putString(Latitude, it.latitude.toString()).apply()
            sharedPreference.edit().putString(Longitude, it.longitude.toString()).apply()
            mMap.addMarker(MarkerOptions().position(latLng).title("Location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            confirmDialog(latLng)

          //if (sharedPreference.getString(ActivityFlag, "map").equals("fragSettings")) {
                /*val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()*/
           // }
        })

        // Add a marker in Sydney and move the camera
        /* val sydney = LatLng(-34.0, 151.0)
         mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
         mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }

    private fun confirmDialog(latLng: LatLng) {
        val builder: AlertDialog.Builder =  AlertDialog.Builder(this)
       // builder.setCancelable(true)
        builder.setTitle(getString( R.string.location_alert_title))
        builder.setMessage(" "+getFullAddress(latLng.latitude,latLng.longitude)+" ?")
        builder.setPositiveButton(getString(R.string.confirm_yes)) { dialog, which ->
           if(sharedPreference.getString(ActivityFlag,"m").equals("fragFavourite"))
            {
                val replyIntent = Intent()
                replyIntent.putExtra("Lat", latLng.latitude.toString())
                replyIntent.putExtra("Lon", latLng.longitude.toString())
                Toast.makeText(this,"xx"+latLng.latitude,Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, replyIntent)
                finish()
            }
            else if(sharedPreference.getString(ActivityFlag,"m").equals("fragSettings"))
            {
                intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        builder.setNegativeButton(android.R.string.cancel){ dialog, which -> }
      //  val dialog: AlertDialog = builder.create()
       // dialog.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun getFullAddress(latitude: Double, longitude: Double): Any {
        var geocoder = Geocoder(this)
        var allAddress = "Unknown"
        try{
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()){
                var city = addresses[0].adminArea
                var country = addresses[0].countryName
                allAddress = "$city,$country"
            }
        }catch (e: IOException){
            Log.e("TAG", "getFullAddress: ${e.message}" )
        }
        return allAddress
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        val latLng = marker.position
        val latitude = latLng.latitude
        val longitude = latLng.longitude

        Toast.makeText(
            this,
            "Marker clicked: lat = $latitude, lon = $longitude",
            Toast.LENGTH_SHORT
        ).show()

        return true
    }


}

/*private fun AlertDialog.Builder.setMessage(fullAddress: Any) {

}*/
