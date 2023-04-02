package eg.gov.iti.skyscanner.favourite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.gov.iti.skyscanner.models.RepositoryInterface

class FavouriteViewModelFactory (private val irepo: RepositoryInterface):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)){
            FavouriteViewModel(irepo) as T
        }else{
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}