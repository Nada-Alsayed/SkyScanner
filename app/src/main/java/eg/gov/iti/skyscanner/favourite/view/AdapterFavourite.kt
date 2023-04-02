package eg.gov.iti.skyscanner.favourite.view

import android.content.ContentValues
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.skyscanner.databinding.RvRowFavCountryBinding
import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.WeatherDetail
import java.util.*

class AdapterFavourite  (var list :List< FavModel>,
val context: Context,
private var onClickInterface:OnClickInterface
) : RecyclerView.Adapter<AdapterFavourite.ViewHolder>() {
    private lateinit var binding: RvRowFavCountryBinding
    inner class ViewHolder(var binding: RvRowFavCountryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvRowFavCountryBinding.inflate(inflater, parent, false)
        Log.i(ContentValues.TAG, "onCreateViewHolder: ")
        return ViewHolder(binding)
    }

    fun setFavList(listWeather: List<FavModel>) {
        this.list = listWeather
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list != null) {
            var myFav: FavModel = list[position]
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: MutableList<Address> = geocoder.getFromLocation(
                myFav.latitude, myFav.longitude, 1
            ) as MutableList<Address>
            if (addresses.isNotEmpty()) {
               holder.binding.txtCountryName.text = "${addresses[0].locality},${addresses[0].countryName}"
                // ${addresses[0].adminArea},
            } else {
                Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show()
            }
            holder.binding.iconDel.setOnClickListener { onClickInterface.onDeleteIconClicked(myFav) }
            holder.binding.conFav.setOnClickListener {  onClickInterface.onRowClicked(myFav) }
        }
    }

}