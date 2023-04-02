package eg.gov.iti.skyscanner.models

import eg.gov.iti.skyscanner.DataBase.LocalSource
import eg.gov.iti.skyscanner.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Repository private constructor(
    var localSource: LocalSource,
    var remoteSource: RemoteSource
) : RepositoryInterface {

    companion object {
        private var instance: Repository? = null
        fun getInstance(remoteSource: RemoteSource, localSource: LocalSource): Repository {
            return instance ?: synchronized(this) {
                val temp = Repository(localSource, remoteSource)
                instance = temp
                temp
            }
        }
    }
    override  fun getStoredWeather(): Flow<List<WeatherDetail>? >{
        return localSource.allStoredWeather()
    }

    override  fun getStoredFavWeather(): Flow<List<FavModel>?>{
        return  localSource.allStoredWeatherFav()
    }

    override fun getStoredAlerts(): Flow<List<UserAlerts>?> {
        return localSource.allStoredUserAlerts()
    }

    /*override suspend fun getStoredWeather(): Flow<WeatherDetail>? {
        return flowOf( localSource.allStoredWeather())
    }*/
    override suspend fun deleteAll() {
        return localSource.deleteAllWeather()
    }
    override suspend fun deleteWeather(weatherDetail: FavModel) {
        return localSource.deleteWeather(weatherDetail)
    }

    override suspend fun deleteAlert(alert: UserAlerts) {
        return localSource.deleteUserAlert(alert)
    }

    override suspend fun insertWeather(weatherDetail: WeatherDetail) {
        return localSource.insertWeather(weatherDetail)
    }

    override suspend fun insertFavWeather(fav: FavModel) {
       return localSource.insertWeatherFav(fav)
    }

    override suspend fun insertAlert(alert: UserAlerts) {
        return localSource.insertUserAlerts(alert)
    }

    override suspend fun getRetrofitWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String,
        apiKey: String
    ): Flow<WeatherDetail>? {
        return flowOf(remoteSource.getWeather(lat,lon,units,lang, apiKey))
    }
    override suspend fun getRetrofitWeatherKelvin(
        lat: Double,
        lon: Double,
        lang: String,
        apiKey: String
    ): Flow<WeatherDetail>? {
        return flowOf(remoteSource.getWeatherKelvin(lat,lon,lang, apiKey))
    }
}