package eg.gov.iti.skyscanner

import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.RepositoryInterface
import eg.gov.iti.skyscanner.models.UserAlerts
import eg.gov.iti.skyscanner.models.WeatherDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepo:RepositoryInterface {
    var userAlertsList: MutableList<UserAlerts> = mutableListOf()
    var favLocationList: MutableList<FavModel> = mutableListOf()
    var weatherDetailList: MutableList<WeatherDetail> = mutableListOf()
    override fun getStoredWeather(): Flow<List<WeatherDetail>?> {
        return flowOf(weatherDetailList)
    }

    override fun getStoredFavWeather(): Flow<List<FavModel>?> {
        return flowOf(favLocationList)
    }

    override fun getStoredAlerts(): Flow<List<UserAlerts>?> {
        return flowOf(userAlertsList)
    }

    override suspend fun deleteAll() {
        weatherDetailList.clear()
    }

    override suspend fun deleteWeather(fav: FavModel) {
        favLocationList.remove(fav)
    }

    override suspend fun deleteAlert(alert: UserAlerts) {
        userAlertsList.remove(alert)
    }

    override suspend fun insertWeather(weatherDetail: WeatherDetail) {
        weatherDetailList.add(weatherDetail)
    }

    override suspend fun insertFavWeather(fav: FavModel) {
        favLocationList.add(fav)
    }

    override suspend fun insertAlert(alert: UserAlerts) {
       userAlertsList.add(alert)
    }

    override suspend fun getRetrofitWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String,
        apiKey: String
    ): Flow<WeatherDetail>? {
        TODO("Not yet implemented")
    }

    override suspend fun getRetrofitWeatherKelvin(
        lat: Double,
        lon: Double,
        lang: String,
        apiKey: String
    ): Flow<WeatherDetail>? {
        TODO("Not yet implemented")
    }
}