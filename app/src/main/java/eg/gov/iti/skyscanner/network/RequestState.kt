package eg.gov.iti.skyscanner.network

import eg.gov.iti.skyscanner.models.WeatherDetail

sealed class RequestState <out T>{
    class Success<T>(val data:T): RequestState<T>()
    class Failure(val msg:Throwable): RequestState<Nothing>()
    object Loading: RequestState<Nothing>()
}
/*
sealed class ResponseState<out T> {
    class Success<T>(val data:T): ResponseState<T>()
    class Failure(val msg:Throwable): ResponseState<Nothing>()
    object Loading: ResponseState<Nothing>()
}*/
