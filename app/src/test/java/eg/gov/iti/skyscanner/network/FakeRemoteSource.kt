package eg.gov.iti.skyscanner.network

import eg.gov.iti.skyscanner.models.WeatherDetail

class FakeRemoteSource(val weatherDetail: WeatherDetail):RemoteSource {
    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String,
        apiKey: String
    ): WeatherDetail {
       return weatherDetail
    }

    override suspend fun getWeatherKelvin(
        lat: Double,
        lon: Double,
        lang: String,
        apiKey: String
    ): WeatherDetail {
        val weather=WeatherDetail(emptyList(),
            WeatherDetail.Current(0,0.0,0,0.0,1,1,1,1,0.0,0.0,1, emptyList(),1,0.0), emptyList(),emptyList(),0.0,0.0,"",0,0)
        return weather
    }
}