package eg.gov.iti.skyscanner.network

import eg.gov.iti.skyscanner.models.WeatherDetail

sealed class RequestState {
    class Success(val data:WeatherDetail):RequestState()
    class Failure(val msg:Throwable):RequestState()
    object Loading:RequestState()
}