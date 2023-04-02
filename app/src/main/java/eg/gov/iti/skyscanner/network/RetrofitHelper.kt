package eg.gov.iti.skyscanner.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

object RetrofitHelper{
    const val BASE_URL="https://api.openweathermap.org/data/2.5/"
    val RetrofitInstance= Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun convertTimeInMillisIntoString(milliSeconds:Long, dateFormat:String,language: String):String
    {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale(language))

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds;
        return formatter.format(calendar.time);
    }
}