package eg.gov.iti.skyscanner.network

import eg.gov.iti.skyscanner.models.WeatherDetail

interface RemoteSource {
suspend fun getWeather(lat:Double,lon:Double,units:String,lang:String,apiKey:String):WeatherDetail
    //lat:Float,lon:Float,units:String,lang:String,apiKey:String
}