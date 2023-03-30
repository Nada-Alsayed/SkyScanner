package eg.gov.iti.skyscanner.models

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson



@Entity(tableName="weather",primaryKeys = ["lat", "lon","isFav"])

@TypeConverters(WeatherTypeConverter::class)
data class WeatherDetail (
    val alerts: List<Alert>?,
    val current: Current,
    var daily: List<Daily>,
    var hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val isFav:Int =0
)  {
    data class Alert(
        val description: String,
        val end: Int,
        val event: String,
        val sender_name: String,
        val start: Int,
        val tags: List<String>
    )

    data class Current(
        val clouds: Int,
        val dew_point: Double,
        val dt: Int,
        val feels_like: Double,
        val humidity: Int,
        val pressure: Int,
        val sunrise: Int,
        val sunset: Int,
        val temp: Double,
        val uvi: Double,
        val visibility: Int,
        val weather: List<Weather>,
        val wind_deg: Int,
        val wind_speed: Double
    ) {
        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        )
    }

    data class Daily(
        val clouds: Int,
        val dew_point: Double,
        val dt: Int,
        val feels_like: FeelsLike,
        val humidity: Int,
        val moon_phase: Double,
        val moonrise: Int,
        val moonset: Int,
        val pop: Double,
        val pressure: Int,
        val rain: Double,
        val snow: Double,
        val sunrise: Int,
        val sunset: Int,
        val temp: Temp,
        val uvi: Double,
        val weather: List<Weather>,
        val wind_deg: Int,
        val wind_gust: Double,
        val wind_speed: Double
    ) {
        data class FeelsLike(
            val day: Double,
            val eve: Double,
            val morn: Double,
            val night: Double
        )

        data class Temp(
            val day: Double,
            val eve: Double,
            val max: Double,
            val min: Double,
            val morn: Double,
            val night: Double
        )

        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        )
    }

    data class Hourly(
        val clouds: Int,
        val dew_point: Double,
        val dt: Int,
        val feels_like: Double,
        val humidity: Int,
        val pop: Double,
        val pressure: Int,
        val rain: Rain,
        val snow: Snow,
        val temp: Double,
        val uvi: Double,
        val visibility: Int,
        val weather: List<Weather>,
        val wind_deg: Int,
        val wind_gust: Double,
        val wind_speed: Double
    ) {
        data class Rain(
            val `1h`: Double
        )

        data class Snow(
            val `1h`: Double
        )

        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        )
    }

}

class WeatherTypeConverter {
    @TypeConverter
    fun fromWeatherResponseToString(weatherResponse: WeatherDetail) = Gson().toJson(weatherResponse)
    @TypeConverter
    fun fromStringToWeatherResponse(weatherString : String) = Gson().fromJson(weatherString, WeatherDetail::class.java)

    @TypeConverter
    fun fromCurrentToString(current: WeatherDetail.Current) = Gson().toJson(current)
    @TypeConverter
    fun fromStringToCurrent(currentString : String) = Gson().fromJson(currentString, WeatherDetail.Current::class.java)

    @TypeConverter
    fun fromHourlyToString(hourly: List<WeatherDetail.Hourly>) = Gson().toJson(hourly)
    @TypeConverter
    fun fromStringToHourly(hourlyString : String) = Gson().fromJson(hourlyString, Array<WeatherDetail.Hourly>::class.java).toList()

    @TypeConverter
    fun fromDailyToString(daily: List<WeatherDetail.Daily>) = Gson().toJson(daily)
    @TypeConverter
    fun fromStringToDaily(dailyString : String) = Gson().fromJson(dailyString, Array<WeatherDetail.Daily>::class.java).toList()

    @TypeConverter
    fun fromTempToString(temp: WeatherDetail.Daily.Temp) = Gson().toJson(temp)
    @TypeConverter
    fun fromStringToTemp(tempString : String) = Gson().fromJson(tempString, WeatherDetail.Daily.Temp::class.java)

    @TypeConverter
    fun fromFeelsLikeToString(feelsLike: WeatherDetail.Daily.FeelsLike) = Gson().toJson(feelsLike)
    @TypeConverter
    fun fromStringToFeelsLike(feelLiksString : String) = Gson().fromJson(feelLiksString, WeatherDetail.Daily.FeelsLike::class.java)

    @TypeConverter
    fun fromWeatherToString(weather: List<WeatherDetail.Current.Weather>) = Gson().toJson(weather)
    @TypeConverter
    fun fromStringToWeather(weatherString : String) = Gson().fromJson(weatherString, Array<WeatherDetail.Current.Weather>::class.java).toList()

    @TypeConverter
    fun fromAlertToString(alert: List<WeatherDetail.Alert>) = Gson().toJson(alert)
    @TypeConverter
    fun fromStringToAlerts(alertString : String) = Gson().fromJson(alertString, Array<WeatherDetail.Alert>::class.java).toList()

}