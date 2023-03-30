package eg.gov.iti.skyscanner.DataBase

import android.content.Context
import eg.gov.iti.skyscanner.models.WeatherDetail

class ConcreteLocalSource (context: Context): LocalSource {
    private val dao:DAO by lazy {
        val dp:DataBase=DataBase.getInstance(context)
        dp.getDAO()
    }
    override suspend fun allStoredWeather():List<WeatherDetail>{
        return dao.getStoredWeather()
    }
    override suspend fun insertWeather(weatherDetail: WeatherDetail) {
        dao.insertWeather(weatherDetail)
    }
    override suspend fun deleteAllWeather(){
        dao.deleteAllWeather()
    }
    override suspend fun deleteWeather(weatherDetail: WeatherDetail) {
        dao.deleteWeather(weatherDetail)
    }
}