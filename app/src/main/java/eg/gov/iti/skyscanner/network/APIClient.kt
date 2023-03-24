package eg.gov.iti.skyscanner.network

import eg.gov.iti.skyscanner.models.WeatherDetail

class APIClient private constructor() : RemoteSource {
    val apiService: APIService by lazy {
        RetrofitHelper.RetrofitInstance.create(APIService::class.java)
    }
    override suspend fun getWeather(
        lat: Float,
        lon: Float,
        units: String,
        lang: String,
        apiKey: String
    ): WeatherDetail {
        return apiService.getWeather(lat,lon,units,lang,apiKey)
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