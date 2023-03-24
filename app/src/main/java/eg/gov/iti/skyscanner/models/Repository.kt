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

    override suspend fun getRetrofitWeather(
        lat: Float,
        lon: Float,
        units: String,
        lang: String,
        apiKey: String
    ): Flow<WeatherDetail>? {
        return flowOf(remoteSource.getWeather(lat,lon,units,lang, apiKey))
    }


}