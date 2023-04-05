package eg.gov.iti.skyscanner.models.DataBase

import eg.gov.iti.skyscanner.DataBase.LocalSource
import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.UserAlerts
import eg.gov.iti.skyscanner.models.WeatherDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalSource(
    var userAlertsList: MutableList<UserAlerts> = mutableListOf(),
    var favLocationList: MutableList<FavModel> = mutableListOf(),
    var weatherDetailList: MutableList<WeatherDetail> = mutableListOf()
) : LocalSource {
    //  val listWeatherDetail: Flow<List<WeatherDetail>> = flowOf( emptyList<WeatherDetail>())
    override fun allStoredWeather(): Flow<List<WeatherDetail>> {
        return flowOf(weatherDetailList)
    }

    override fun allStoredWeatherFav(): Flow<List<FavModel>> {
        return flowOf(favLocationList)
    }

    override fun allStoredUserAlerts(): Flow<List<UserAlerts>> {
        return flowOf(userAlertsList)
    }

    override suspend fun insertWeather(weatherDetail: WeatherDetail) {
        weatherDetailList?.add(weatherDetail)
    }

    override suspend fun insertWeatherFav(fav: FavModel) {
        favLocationList?.add(fav)
    }

    override suspend fun insertUserAlerts(userAlerts: UserAlerts) {
        userAlertsList?.add(userAlerts)
    }

    override suspend fun deleteAllWeather() {
        weatherDetailList?.clear()
    }

    override suspend fun deleteUserAlert(userAlerts: UserAlerts) {
        userAlertsList?.remove(userAlerts)
    }

    override suspend fun deleteWeather(favLocation: FavModel) {
        favLocationList?.remove(favLocation)
    }
}