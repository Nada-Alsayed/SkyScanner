package eg.gov.iti.skyscanner.models

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

   // suspend fun getStoredWeather():Flow<WeatherDetail>?
     fun getStoredWeather():Flow<List<WeatherDetail>?>
     fun getStoredFavWeather():Flow<List<FavModel>?>

    suspend fun deleteAll()
    suspend fun deleteWeather(fav: FavModel)
    suspend fun insertWeather(weatherDetail: WeatherDetail)
    suspend fun insertFavWeather(fav: FavModel)

    suspend fun getRetrofitWeather(lat:Double,lon:Double,units:String,lang:String,apiKey:String): Flow<WeatherDetail>?
    suspend fun getRetrofitWeatherKelvin(lat:Double,lon:Double,lang:String,apiKey:String): Flow<WeatherDetail>?

}