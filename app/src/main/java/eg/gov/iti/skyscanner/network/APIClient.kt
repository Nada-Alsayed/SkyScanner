package eg.gov.iti.skyscanner.network

import eg.gov.iti.skyscanner.models.WeatherDetail

class APIClient private constructor() : RemoteSource {
    val apiService: APIService by lazy {
        RetrofitHelper.RetrofitInstance.create(APIService::class.java)
    }
    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String,
        apiKey: String
    ): WeatherDetail {
        return apiService.getWeather(lat,lon,units,lang,apiKey)
    }

    override suspend fun getWeatherKelvin(
        lat: Double,
        lon: Double,
        lang: String,
        apiKey: String
    ): WeatherDetail {
        return apiService.getWeatherKelvin(lat,lon,lang,apiKey)
    }

    companion object {
        private var instance: APIClient? = null
        fun getInstance(): APIClient {
            return instance ?: synchronized(this) {
                val temp = APIClient()
                instance = temp
                temp
            }
        }
    }


}