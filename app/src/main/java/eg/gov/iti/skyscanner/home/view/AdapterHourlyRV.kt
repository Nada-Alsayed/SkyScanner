package eg.gov.iti.skyscanner.home.view

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eg.gov.iti.skyscanner.databinding.RvRowHourlyTempBinding
import eg.gov.iti.skyscanner.models.WeatherDetail
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class AdapterHourlyRV(
    var weather: WeatherDetail,
    val context: Context
) : RecyclerView.Adapter<AdapterHourlyRV.ViewHolder>() {
    private lateinit var binding: RvRowHourlyTempBinding

    inner class ViewHolder(var binding: RvRowHourlyTempBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvRowHourlyTempBinding.inflate(inflater, parent, false)
        Log.i(ContentValues.TAG, "onCreateViewHolder: ")
        return ViewHolder(binding)
    }

    fun setList(list:WeatherDetail) {
        weather = list
    }

    override fun getItemCount(): Int {
        return weather.hourly.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (weather != null) {
            var myHour: WeatherDetail.Hourly = weather.hourly[position]
            Glide.with(context)
                .load(myHour.weather.get(0).icon)
                .into(holder.binding.rvImgW);
            holder.binding.rvTxtTemp.text = myHour.temp.toString()
            holder.binding.txtHour.text = getCurrentTime( myHour.dt, weather.timezone)

        }
    }
    fun getCurrentTime(dt: Int, timezone: String, format: String = "K:mm a"): String {
        val zoneId = ZoneId.of(timezone)
        val instant = Instant.ofEpochSecond(dt.toLong())
        val formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH)
        return instant.atZone(zoneId).format(formatter)
    }

}