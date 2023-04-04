package eg.gov.iti.skyscanner.home.view

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.skyscanner.databinding.RvRowHourlyTempBinding
import eg.gov.iti.skyscanner.models.MyIcons
import eg.gov.iti.skyscanner.models.MyIcons.replaceAPIIcon
import eg.gov.iti.skyscanner.models.WeatherDetail
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class AdapterHourlyRV(
    var weather: WeatherDetail,
    val context: Context,
    val unit:String,val lang:String
) : RecyclerView.Adapter<AdapterHourlyRV.ViewHolder>() {
    private lateinit var binding: RvRowHourlyTempBinding

    val formatter = NumberFormat.getInstance(Locale(lang))

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
            replaceAPIIcon(myHour.weather.get(0).icon, holder.binding.rvImgW)
            holder.binding.txtHour.text = getCurrentTime(myHour.dt, weather.timezone)
            holder.binding.rvTxtTemp.text = myHour.temp.toInt().toString()
            when (unit) {
                "metric" -> {
                    val formattedNumber = formatter.format(myHour.temp.toInt())
                    holder.binding.rvTxtTemp.text = "${formattedNumber}°C"
                }
                "imperial" -> {
                    val formattedNumber = formatter.format(myHour.temp.toInt())
                    holder.binding.rvTxtTemp.text = "${formattedNumber}°F"
                }
                else -> {
                    val formattedNumber = formatter.format(myHour.temp.toInt())
                    holder.binding.rvTxtTemp.text = "${formattedNumber}°K"
                }
            }
        }
    }

    fun getCurrentTime(dt: Int, timezone: String, format: String = "K:mm a"): String {
        val zoneId = ZoneId.of(timezone)
        val instant = Instant.ofEpochSecond(dt.toLong())
        val formatter = DateTimeFormatter.ofPattern(format, Locale(lang))
        return instant.atZone(zoneId).format(formatter)
    }

}