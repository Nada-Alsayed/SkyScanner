package eg.gov.iti.skyscanner.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.gov.iti.skyscanner.favourite.viewModel.FavouriteViewModel
import eg.gov.iti.skyscanner.models.RepositoryInterface

class AlertViewModelFactory (private val irepo: RepositoryInterface):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(irepo) as T
        }else{
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}