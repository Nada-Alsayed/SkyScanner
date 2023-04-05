package eg.gov.iti.skyscanner.favourite.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eg.gov.iti.skyscanner.FakeRepo
import eg.gov.iti.skyscanner.MainRule
import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.network.RequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FavouriteViewModelTest{
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainRule = MainRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: FavouriteViewModel
    lateinit var repository: FakeRepo
    val fav4 = FavModel(6.0, 6.0)
    @Before
    fun setup() {
        repository = FakeRepo()
        viewModel= FavouriteViewModel(repository)
    }

    @Test
    fun insertFav_favObj() =mainRule.runBlockingTest {
        //Given
        var x= emptyList<FavModel>()
        //when
        viewModel.insertWeather(fav4)
        //then
        viewModel.getStoredFavWeather()
        val db = viewModel.favWeatherFromRoom.first()
            when (db) {
                is RequestState.Success -> {
                   x= db.data!!
                }
                else ->{}
            }

        assertThat(x, CoreMatchers.notNullValue())
    }
    @Test
    fun deleteFav_favObj() =mainRule.runBlockingTest {
        //Given
        var x= emptyList<FavModel>()
        //when
        viewModel.deleteOneFav(fav4)
        //then
        viewModel.getStoredFavWeather()
        val db = viewModel.favWeatherFromRoom.first()
        when (db) {
            is RequestState.Success -> {
                x= db.data!!
            }
            else ->{}
        }

        assertThat(x, CoreMatchers.`is`(emptyList()))
    }
    @Test
    fun getFavourites() =mainRule.runBlockingTest {
        //Given
        viewModel.insertWeather(fav4)
        //when
        viewModel.getStoredFavWeather()
        var x= emptyList<FavModel>()
        //then
        val db = viewModel.favWeatherFromRoom.first()
        when (db) {
            is RequestState.Success -> {
                x= db.data!!
            }
            else ->{}
        }

        //assertThat(x.size, CoreMatchers.notNullValue())
        assertThat(x.size, CoreMatchers.`is`(1))
    }
}