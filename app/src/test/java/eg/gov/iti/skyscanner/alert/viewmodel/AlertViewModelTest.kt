package eg.gov.iti.skyscanner.alert.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eg.gov.iti.skyscanner.FakeRepo
import eg.gov.iti.skyscanner.MainRule
import eg.gov.iti.skyscanner.favourite.viewModel.FavouriteViewModel
import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.UserAlerts
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
class AlertViewModelTest{
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainRule = MainRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: AlertViewModel
    lateinit var repository: FakeRepo
    val alert1 = UserAlerts(0L, 0L, 0L, "")
    @Before
    fun setup() {
        repository = FakeRepo()
        viewModel= AlertViewModel(repository)
    }
    @Test
    fun insertAlert_alertObj() =mainRule.runBlockingTest {
        //Given
        var x= emptyList<UserAlerts>()
        //when
        viewModel.insertAlert(alert1)
        //then
        viewModel.getStoredAlert()
        val db = viewModel.alertFromRoom.first()
        when (db) {
            is RequestState.Success -> {
                x= db.data!!
            }
            else ->{}
        }
        assertThat(x, CoreMatchers.notNullValue())
    }
    @Test
    fun deleteAlert_alertObj() =mainRule.runBlockingTest {
        //Given
        var x= emptyList<UserAlerts>()
        //when
        viewModel.deleteAlert(alert1)
        //then
        viewModel.getStoredAlert()
        val db = viewModel.alertFromRoom.first()
        when (db) {
            is RequestState.Success -> {
                x= db.data!!
            }
            else ->{}
        }

        assertThat(x, CoreMatchers.`is`(emptyList()))
    }
    @Test
    fun getAlerts() =mainRule.runBlockingTest {
        //Given
        viewModel.insertAlert(alert1)
        //when
        viewModel.getStoredAlert()
        var x= emptyList<UserAlerts>()
        //then
        val db = viewModel.alertFromRoom.first()
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