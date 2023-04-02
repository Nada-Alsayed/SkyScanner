package eg.gov.iti.skyscanner.DataBase

import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.UserAlerts
import eg.gov.iti.skyscanner.models.WeatherDetail
import kotlinx.coroutines.flow.Flow

interface LocalSource {
     fun allStoredWeather(): Flow<List<WeatherDetail>>
     fun allStoredWeatherFav():Flow< List<FavModel>>
    fun allStoredUserAlerts():Flow< List<UserAlerts>>
    suspend fun insertWeather(weatherDetail: WeatherDetail)
    suspend fun insertWeatherFav(fav: FavModel)
    suspend fun insertUserAlerts(userAlerts: UserAlerts)
    suspend fun deleteAllWeather()
    suspend fun deleteUserAlert(userAlerts: UserAlerts)
    //suspend fun deleteWeather(weatherDetail: WeatherDetail)
    suspend fun deleteWeather(weatherDetail: FavModel)

}