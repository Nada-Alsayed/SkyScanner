package eg.gov.iti.skyscanner.models

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getRetrofitWeather(lat:Float,lon:Float,units:String,lang:String,apiKey:String): Flow<WeatherDetail>?

}