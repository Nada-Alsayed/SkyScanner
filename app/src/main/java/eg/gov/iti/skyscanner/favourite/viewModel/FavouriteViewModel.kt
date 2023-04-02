package eg.gov.iti.skyscanner.favourite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.RepositoryInterface
import eg.gov.iti.skyscanner.network.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteViewModel(private val repo: RepositoryInterface) : ViewModel() {
    private var _FavWeatherFromRoom: MutableStateFlow<RequestState<List<FavModel>?>> =
        MutableStateFlow(RequestState.Loading)
    val favWeatherFromRoom = _FavWeatherFromRoom

    fun deleteOneFav(weather: FavModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteWeather(weather)
        }
    }

    fun insertWeather(weather: FavModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertFavWeather(weather)
        }
    }

    fun getStoredFavWeather() {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getStoredFavWeather()?.catch { e ->
                _FavWeatherFromRoom.value = RequestState.Failure(e)
            }?.collect { it ->
                _FavWeatherFromRoom.value = RequestState.Success(it)
            }
        }
    }

}