package eg.gov.iti.skyscanner.home.view

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.skyscanner.databinding.RvRowDailyTempBinding
import eg.gov.iti.skyscanner.models.MyIcons
import eg.gov.iti.skyscanner.models.WeatherDetail
import java.text.SimpleDateFormat
import java.util.*

class AdapterDailyRV(
    var weather: WeatherDetail,
    val context: Context
) : RecyclerView.Adapter<AdapterDailyRV.ViewHolder>() {
    var icon: MyIcons=MyIcons()
    private lateinit var binding: RvRowDailyTempBinding

    inner class ViewHolder(var binding: RvRowDailyTempBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvRowDailyTempBinding.inflate(inflater, parent, false)
        Log.i(ContentValues.TAG, "onCreateViewHolder: ")
        return ViewHolder(binding)
    }

    fun setList(list: WeatherDetail) {
        weather = list
    }

    override fun getItemCount(): Int {
        return weather.daily.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (weather != null) {
            var myDay: WeatherDetail.Daily = weather.daily[position]
            icon.replaceAPIIcon(myDay.weather.get(0).icon, holder.binding.rvImgWDay)
            holder.binding.txtDayName.text = getDay(myDay.dt)
            holder.binding.rvTxtTemp.text = myDay.weather.get(0).description
            holder.binding.rvNumTemp.text = myDay.temp.day.toString()

        }
    }

    fun getDay(dt: Int): String {
        val cityTxtFormat = SimpleDateFormat("E")
        val cityTxtData = Date(dt.toLong() * 1000)
        return cityTxtFormat.format(cityTxtData)
    }

}