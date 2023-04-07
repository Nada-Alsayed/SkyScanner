package eg.gov.iti.skyscanner.home.view

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.skyscanner.databinding.RvRowDailyTempBinding
import eg.gov.iti.skyscanner.models.MyIcons
import eg.gov.iti.skyscanner.models.MyIcons.replaceAPIIcon
import eg.gov.iti.skyscanner.models.WeatherDetail
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class AdapterDailyRV(
    var weather: WeatherDetail,
    val context: Context,
    val unit:String,
    val lang:String
) : RecyclerView.Adapter<AdapterDailyRV.ViewHolder>() {
    private lateinit var binding: RvRowDailyTempBinding
    val formatter = NumberFormat.getInstance(Locale(lang))

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
            replaceAPIIcon(myDay.weather[0].icon, holder.binding.rvImgWDay)
            holder.binding.txtDayName.text = getDay(myDay.dt,lang)
            holder.binding.rvTxtTemp.text = myDay.weather[0].description
            when (unit) {
                "metric" -> {
                    val formattedNumber1 = formatter.format(myDay.temp.min.toInt())
                    val formattedNumber2 = formatter.format(myDay.temp.max.toInt())
                    holder.binding.rvNumTemp.text = "${formattedNumber1}/${formattedNumber2}°C"
                }
                "imperial" -> {
                    val formattedNumber1 = formatter.format(myDay.temp.min.toInt())
                    val formattedNumber2 = formatter.format(myDay.temp.max.toInt())
                    holder.binding.rvNumTemp.text = "${formattedNumber1}/${formattedNumber2}°F"
                }
                else -> {
                    val formattedNumber1 = formatter.format(myDay.temp.min.toInt())
                    val formattedNumber2 = formatter.format(myDay.temp.max.toInt())
                    holder.binding.rvNumTemp.text = "${formattedNumber1}/${formattedNumber2}°K"
                }
            }


        }
    }

    fun getDay(dt: Int,lang:String): String {
        val cityTxtFormat = SimpleDateFormat("E",Locale(lang))
        val cityTxtData = Date(dt.toLong() * 1000)
        return cityTxtFormat.format(cityTxtData)
    }

}