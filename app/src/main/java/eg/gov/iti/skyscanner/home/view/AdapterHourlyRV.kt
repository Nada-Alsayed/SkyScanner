package eg.gov.iti.skyscanner.home.view

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.skyscanner.databinding.RvRowHourlyTempBinding
import eg.gov.iti.skyscanner.models.MyIcons
import eg.gov.iti.skyscanner.models.WeatherDetail
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class AdapterHourlyRV(
    var weather: WeatherDetail,
    val context: Context,
    val unit:String
) : RecyclerView.Adapter<AdapterHourlyRV.ViewHolder>() {
    private lateinit var binding: RvRowHourlyTempBinding
     var icon: MyIcons=MyIcons()

    inner class ViewHolder(var binding: RvRowHourlyTempBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvRowHourlyTempBinding.inflate(inflater, parent, false)
        Log.i(ContentValues.TAG, "onCreateViewHolder: ")
        return ViewHolder(binding)
    }

    fun setList(list: WeatherDetail) {
        weather = list
    }

    override fun getItemCount(): Int {
        return weather.hourly.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (weather != null) {
            var myHour: WeatherDetail.Hourly = weather.hourly[position]
            icon.replaceAPIIcon(myHour.weather.get(0).icon, holder.binding.rvImgW)
            holder.binding.txtHour.text = getCurrentTime(myHour.dt, weather.timezone)
            holder.binding.rvTxtTemp.text = myHour.temp.toInt().toString()
            when (unit) {
                "metric" -> {
                    holder.binding.rvTxtTemp.text = "${myHour.temp.toInt()}°C"
                }
                "imperial" -> {
                    holder.binding.rvTxtTemp.text = "${myHour.temp.toInt()}°F"
                }
                else -> {
                    holder.binding.rvTxtTemp.text = "${myHour.temp.toInt()}°K"
                }
            }
        }
    }

    fun getCurrentTime(dt: Int, timezone: String, format: String = "K:mm a"): String {
        val zoneId = ZoneId.of(timezone)
        val instant = Instant.ofEpochSecond(dt.toLong())
        val formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH)
        return instant.atZone(zoneId).format(formatter)
    }

}