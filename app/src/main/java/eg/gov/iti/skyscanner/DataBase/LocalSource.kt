package eg.gov.iti.skyscanner.DataBase

import eg.gov.iti.skyscanner.models.WeatherDetail

interface LocalSource {
    suspend fun allStoredWeather(): List<WeatherDetail>
    suspend fun insertWeather(weatherDetail: WeatherDetail)
    suspend fun deleteAllWeather()
    suspend fun deleteWeather(weatherDetail: WeatherDetail)

}