package eg.gov.iti.skyscanner.DataBase

import android.content.Context
import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.WeatherDetail
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource (context: Context): LocalSource {
    private val dao:DAO by lazy {
        val dp:DataBase=DataBase.getInstance(context)
        dp.getDAO()
    }
    override fun allStoredWeather(): Flow<List<WeatherDetail>> {
        return dao.getStoredWeather()
    }

    override fun allStoredWeatherFav(): Flow<List<FavModel>> {
        return dao.getFavWeather()
    }

    override suspend fun insertWeather(weatherDetail: WeatherDetail) {
        dao.insertWeather(weatherDetail)
    }

    override suspend fun insertWeatherFav(fav: FavModel) {
        dao.insertFavWeather(fav)
    }

    override suspend fun deleteAllWeather(){
        dao.deleteAllWeather()
    }
    override suspend fun deleteWeather(weatherDetail: FavModel) {
        dao.deleteWeather(weatherDetail)
    }
   /* override suspend fun deleteWeather(weatherDetail: WeatherDetail) {
        dao.deleteWeather(weatherDetail)
    }*/
}