package eg.gov.iti.skyscanner.models

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getRetrofitWeather(lat:Double,lon:Double,units:String,lang:String,apiKey:String): Flow<WeatherDetail>?
    suspend fun getRetrofitWeatherKelvin(lat:Double,lon:Double,lang:String,apiKey:String): Flow<WeatherDetail>?

}