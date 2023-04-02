package eg.gov.iti.skyscanner.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.gov.iti.skyscanner.models.RepositoryInterface
import eg.gov.iti.skyscanner.models.UserAlerts
import eg.gov.iti.skyscanner.network.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel (private val repo: RepositoryInterface) : ViewModel() {
    private var _AlertFromRoom: MutableStateFlow<RequestState<List<UserAlerts>?>> =
        MutableStateFlow(RequestState.Loading)
    val alertFromRoom = _AlertFromRoom

    fun deleteAlert(alert: UserAlerts) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAlert(alert)
        }
    }

    fun insertAlert(alert: UserAlerts)  {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertAlert(alert)
        }
    }

    fun getStoredAlert() {
        viewModelScope.launch(Dispatchers.IO) {

            repo.getStoredAlerts()?.catch { e ->
                _AlertFromRoom.value = RequestState.Failure(e)
            }?.collect { it ->
                _AlertFromRoom.value = RequestState.Success(it)
            }
        }
    }
}