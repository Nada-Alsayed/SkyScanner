package eg.gov.iti.skyscanner.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.gov.iti.skyscanner.models.RepositoryInterface
import eg.gov.iti.skyscanner.network.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private var stateShare = MutableStateFlow<RequestState>(RequestState.Loading)
    val weather =stateShare

    fun getRemoteWeather(lat:Float,lon:Float,units:String,lang:String,apiKey:String)
    {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getRetrofitWeather(lat,lon,units,lang, apiKey)?.catch {
                    e->stateShare.value=RequestState.Failure(e) }?.collect{
                    data->stateShare.value=RequestState.Success(data)
            }
        }
    }
}