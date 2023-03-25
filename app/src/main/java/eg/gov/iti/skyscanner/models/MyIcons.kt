package eg.gov.iti.skyscanner.models

import android.widget.ImageView
import eg.gov.iti.skyscanner.R

class MyIcons {
    fun replaceAPIIcon(img:String,imageView: ImageView){
        when(img){
            "01d" -> imageView.setImageResource(R.drawable.sun)
            "01n" -> imageView.setImageResource(R.drawable.moon2)

            "02d" -> imageView.setImageResource(R.drawable.sun_cloud)
            "02n" -> imageView.setImageResource(R.drawable.cloud_night)

            "03d" -> imageView.setImageResource(R.drawable.cloud)
            "03n" -> imageView.setImageResource(R.drawable.cloud)

            "04d" -> imageView.setImageResource(R.drawable.broken_cloud)
            "04n" -> imageView.setImageResource(R.drawable.broken_cloud)

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
}