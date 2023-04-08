package eg.gov.iti.skyscanner.favourite.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import eg.gov.iti.skyscanner.DataBase.ConcreteLocalSource
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.databinding.FragmentFavouriteBinding
import eg.gov.iti.skyscanner.favourite.viewModel.FavouriteViewModel
import eg.gov.iti.skyscanner.favourite.viewModel.FavouriteViewModelFactory
import eg.gov.iti.skyscanner.home.view.Latitude
import eg.gov.iti.skyscanner.home.view.Longitude
import eg.gov.iti.skyscanner.mainactivity.view.MainActivity
import eg.gov.iti.skyscanner.map.MapsActivity
import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.Repository
import eg.gov.iti.skyscanner.models.WeatherDetail
import eg.gov.iti.skyscanner.network.APIClient
import eg.gov.iti.skyscanner.network.RequestState
import eg.gov.iti.skyscanner.settings.view.ActivityFlag
import kotlinx.coroutines.launch

class FragmentFavourite : Fragment(), OnClickInterface {
    lateinit var binding: FragmentFavouriteBinding
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var adapterFavourite: AdapterFavourite
    lateinit var viewModel: FavouriteViewModel
    lateinit var viewModelFactory: FavouriteViewModelFactory
    override fun onResume() {
        super.onResume()
        val activity: Activity? = activity
        if (activity != null) {
            activity.title = getString(eg.gov.iti.skyscanner.R.string.favourites)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favourite.visibility= View.VISIBLE
        sharedPreference = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
        adapterFavourite=AdapterFavourite(emptyList(),requireContext(),this)
        viewModelFactory = FavouriteViewModelFactory(
            Repository.getInstance(
                APIClient.getInstance(), ConcreteLocalSource(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[FavouriteViewModel::class.java]

        viewModel.getStoredFavWeather()
        lifecycleScope.launch() {
            viewModel.favWeatherFromRoom.collect { db ->
                when (db) {
                    is RequestState.Success -> {
                        if(db.data.isNullOrEmpty()){
                            binding.favourite.visibility= View.VISIBLE
                            binding.conFavourite.visibility= View.GONE
                        }else{
                            binding.favourite.visibility= View.GONE
                            binding.conFavourite.visibility= View.VISIBLE
                            db.data?.let { adapterFavourite.setFavList(it) }
                            binding.RVFav.adapter=adapterFavourite
                        }
                    }
                    is RequestState.Failure -> {
                        binding.conFavourite.visibility= View.GONE
                        binding.favourite.visibility= View.GONE
                        Snackbar.make(requireView(),R.string.problem,Snackbar.LENGTH_SHORT).show()
                    }
                    is RequestState.Loading -> {

                    }
                }
            }
        }
        binding.floatingActionButton.setOnClickListener {
            if(isNetworkAvailable(requireContext())){
            editor.putString(ActivityFlag, "fragFavourite").apply()
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivityForResult(intent, 1)}
            else{
                Snackbar.make(requireView(),R.string.connect_wifi, Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==1  && resultCode ==Activity.RESULT_OK) {
            val lat = data?.getStringExtra("Lat")?.toDoubleOrNull()
            val lon = data?.getStringExtra("Lon")?.toDoubleOrNull()
            if (lat != null) {
                if (lon != null) {
                    addInRoom(lat,lon)
                }
            } else {
                Toast.makeText(context,"vvvvvvvv"+lat,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addInRoom(lat:Double,lon:Double){
        val favModel=FavModel(lat,lon)
        viewModel.insertWeather(favModel)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onRowClicked(favModel: FavModel) {
        if (isNetworkAvailable(requireContext()))
        {
            editor.putString(ActivityFlag, "fragFavourite").apply()
            editor.putString(Latitude, favModel.latitude.toString()).apply()
            editor.putString(Longitude, favModel.longitude.toString()).apply()
            val intent = Intent(activity, MainActivity::class.java)
            Log.e("TAG", "fragfavonclick: ${sharedPreference.getString(ActivityFlag,"m")}" )
            //  intent.putExtra(IntentKeys.FAVOURITE_Place,favouriteModel)
            startActivity(intent)
        }
        else{
             Snackbar.make(requireView(),R.string.connect_wifi, Snackbar.LENGTH_SHORT).show()
        }
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }
    override fun onDeleteIconClicked(favModel: FavModel) {
        val builder: AlertDialog.Builder =  AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setTitle(getString(R.string.delete_alert_title))
        builder.setMessage(getString(R.string.are_you_sure))
        builder.setPositiveButton(getString(R.string.confirm_yes)) { _, _ ->
            viewModel.deleteOneFav(favModel)
        }
        builder.setNegativeButton(android.R.string.cancel){ _, _ -> }
        builder.show()
    }
}

//  val dialog: AlertDialog = builder.create()
//  dialog.setCanceledOnTouchOutside(false)
/*val requestAddFavoritePlace =
      registerForActivityResult(ActivityResultContracts.StartActivityForResult())
      { result ->
          if (result.resultCode == Activity.RESULT_OK) {
              val data = result.data
              val lat = data?.getStringExtra("Lat")?.toDoubleOrNull()
              Toast.makeText(context,"vvvvvvvv"+lat,Toast.LENGTH_SHORT).show()
              val lon = data?.getStringExtra("Lon")?.toDoubleOrNull()
              if (lat != null) {
                  if (lon != null) {
                      addInRoom(lat,lon)
                  }
              }
          }
      }*/
