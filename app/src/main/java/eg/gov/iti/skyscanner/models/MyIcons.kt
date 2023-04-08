package eg.gov.iti.skyscanner.models

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.workmanager.Notification
import java.lang.reflect.Type

object MyIcons {
    const val ALERT: String = "my alert"
    const val DESCRIPTION: String = "description"
    const val ICON: String = "icon"
    const val FROM_TIME_IN_MILLIS: String = "fromTimeInMillis"
    @TypeConverter
    fun convertToUserAlert(value: String): UserAlerts {
        val type: Type = object : TypeToken<UserAlerts>() {}.type
        return Gson().fromJson(value, type)
    }
    @TypeConverter
    fun convertUserAlertToString(myAlert: UserAlerts): String = Gson().toJson(myAlert)
    fun openNotification(context: Context, myAlert:UserAlerts, description: String, icon: String, title: String) {
        Log.e("TAG", "notify: *************7777 " +myAlert+description+icon+title)

        val notificationHelper = Notification(context, description, icon, title)
        val nb = notificationHelper.getChannelNotification()
        notificationHelper.getManager()!!.notify(55, nb.build())
    }
    fun replaceAPIIcon(img:String,imageView: ImageView){
        when(img){
            "01d" -> imageView.setImageResource(R.drawable.sunny)
            "01n" -> imageView.setImageResource(R.drawable.m4)

            "02d" -> imageView.setImageResource(R.drawable.sc)
            "02n" -> imageView.setImageResource(R.drawable.nc)

            "03d" -> imageView.setImageResource(R.drawable.cloud)
            "03n" -> imageView.setImageResource(R.drawable.cloud)

            "04d" -> imageView.setImageResource(R.drawable.clouds)
            "04n" -> imageView.setImageResource(R.drawable.clouds)

            "09d" -> imageView.setImageResource(R.drawable.rain)
            "09n" -> imageView.setImageResource(R.drawable.rain)

            "10d" -> imageView.setImageResource(R.drawable.sun_cloud_rain)
            "10n" -> imageView.setImageResource(R.drawable.night_cloud_rain)

            "11d" -> imageView.setImageResource(R.drawable.storm)
            "11n" -> imageView.setImageResource(R.drawable.storm)

            "13d" -> imageView.setImageResource(R.drawable.storm)
            "13n" -> imageView.setImageResource(R.drawable.storm)

            "50d" -> imageView.setImageResource(R.drawable.mist)
            "50n" -> imageView.setImageResource(R.drawable.mist)
        }
    }
    fun aPIIconInt(img:String):Int{
        var imageInteger: Int=0
        when(img){
            "01d" ->  imageInteger= R.drawable.sun
            "01n" -> imageInteger=R.drawable.moon2

            "02d" -> imageInteger=R.drawable.sun_cloud
            "02n" -> imageInteger=R.drawable.cloud_night

            "03d" -> imageInteger=R.drawable.cloud
            "03n" -> imageInteger=R.drawable.cloud

            "04d" -> imageInteger=R.drawable.broken_cloud
            "04n" -> imageInteger=R.drawable.broken_cloud

            "09d" -> imageInteger=R.drawable.rain
            "09n" -> imageInteger=R.drawable.rain

            "10d" -> imageInteger=R.drawable.sun_cloud_rain
            "10n" -> imageInteger=R.drawable.night_cloud_rain

            "11d" -> imageInteger=R.drawable.storm
            "11n" -> imageInteger=R.drawable.storm

            "13d" -> imageInteger=R.drawable.storm
            "13n" -> imageInteger=R.drawable.storm

            "50d" -> imageInteger=R.drawable.mist
            "50n" -> imageInteger=R.drawable.mist
        }
        return imageInteger
    }
}