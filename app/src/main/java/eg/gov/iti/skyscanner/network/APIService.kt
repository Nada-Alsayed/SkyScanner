package eg.gov.iti.skyscanner.network

import eg.gov.iti.skyscanner.models.WeatherDetail
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("onecall")
    suspend fun getWeather(@Query("lat")lat:Float, @Query("lon")lon:Float,@Query("units")units:String, @Query("lang")lang:String, @Query("appid")apiKey:String): WeatherDetail
  //  https://api.openweathermap.org/data/3.0/onecall?lat=33.44&lon=-94.04&units=metric&lang=ar&appid=40dac0af7018969cbb541943f944ba29
}