package eg.gov.iti.skyscanner.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.gov.iti.skyscanner.models.RepositoryInterface
import eg.gov.iti.skyscanner.models.WeatherDetail
import eg.gov.iti.skyscanner.network.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepositoryInterface) : ViewModel() {
    private var _allWeatherFromRoom: MutableStateFlow<List<WeatherDetail>?> = MutableStateFlow(null)
    val allWeatherFromRoom: StateFlow<List<WeatherDetail>?> = _allWeatherFromRoom

    private var stateShare = MutableStateFlow<RequestState>(RequestState.Loading)
    var weather =stateShare

    fun getRemoteWeather(lat:Double,lon:Double,units:String,lang:String,apiKey:String)
    {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getRetrofitWeather(lat,lon,units,lang, apiKey)?.catch {
                    e->stateShare.value=RequestState.Failure(e) }?.collect{
                    data->stateShare.value=RequestState.Success(data)
            }
        }
    }
    fun getStoredWeather(){
        viewModelScope.launch (Dispatchers.IO){

            try {
                var listWeatherDB: List<WeatherDetail>? = repo.getStoredWeather()
                _allWeatherFromRoom.value= listWeatherDB

            } catch (e: Exception) {
                Log.e("WeatherDBViewModel", e.message.toString())

            }

        }
    }

    /*fun getStoredWeather(){
        viewModelScope.launch (Dispatchers.IO){
           repo.getStoredWeather()?.catch {
                   e->stateShare.value=RequestState.Failure(e) }
            ?.collect{
                   data->stateShare.value=RequestState.Success(data)
           }
            weather=stateShare
        }
    }*/
    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll()
        }
    }
    fun insertWeather(weatherDetail: WeatherDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWeather(weatherDetail)
        }
    }
    fun getRemoteWeatherKelvin(lat:Double,lon:Double,lang:String,apiKey:String)
    {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getRetrofitWeatherKelvin(lat,lon,lang, apiKey)?.catch {
                    e->stateShare.value=RequestState.Failure(e) }?.collect{
                    data->stateShare.value=RequestState.Success(data)
            }
        }
    }
}