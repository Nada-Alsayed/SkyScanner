package eg.gov.iti.skyscanner.DataBase

import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.WeatherDetail
import kotlinx.coroutines.flow.Flow

interface LocalSource {
     fun allStoredWeather(): Flow<List<WeatherDetail>>
     fun allStoredWeatherFav():Flow< List<FavModel>>
    suspend fun insertWeather(weatherDetail: WeatherDetail)
    suspend fun insertWeatherFav(fav: FavModel)
    suspend fun deleteAllWeather()
    //suspend fun deleteWeather(weatherDetail: WeatherDetail)
    suspend fun deleteWeather(weatherDetail: FavModel)

}