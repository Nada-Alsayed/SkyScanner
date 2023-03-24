package eg.gov.iti.skyscanner.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.gov.iti.skyscanner.models.RepositoryInterface

class HomeViewModelFactory(private val irepo: RepositoryInterface):
    ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(irepo) as T
        }else{
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}