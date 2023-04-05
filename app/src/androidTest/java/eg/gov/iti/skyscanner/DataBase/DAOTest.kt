package eg.gov.iti.skyscanner.DataBase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import eg.gov.iti.skyscanner.MainRule
import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.UserAlerts
import eg.gov.iti.skyscanner.models.WeatherDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalStdlibApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DAOTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalStdlibApi
    @get:Rule
    val mainRule = MainRule()

    lateinit var database:DataBase

    @Before
    fun createDataBase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DataBase::class.java
        ).allowMainThreadQueries().build()

    }
    @After
    fun closeDataBase() {
        database.close()
    }

    @Test
    fun insertWeather_WeatherObj() = mainRule.runBlockingTest {
        //Given
        val weather4 = WeatherDetail(
            emptyList(),
            WeatherDetail.Current(5, 5.0, 0, 5.0, 1, 1, 1, 1, 0.0, 0.0, 1, emptyList(), 1, 0.0),
            emptyList(),
            emptyList(),
            5.0,
            5.0,
            "",
            0,
            1
        )
        database.getDAO().insertWeather(weather4)
        //when
       val weather= database.getDAO().getStoredWeather().first()
        //then
        Assert.assertThat(weather,CoreMatchers.notNullValue())
    }
    @Test
    fun deleteWeather_() = mainRule.runBlockingTest {
        //Given
        val weather4 = WeatherDetail(
            emptyList(),
            WeatherDetail.Current(5, 5.0, 0, 5.0, 1, 1, 1, 1, 0.0, 0.0, 1, emptyList(), 1, 0.0),
            emptyList(),
            emptyList(),
            5.0,
            5.0,
            "",
            0,
            1
        )
        database.getDAO().insertWeather(weather4)
        database.getDAO().deleteAllWeather()
        //when
        val weather= database.getDAO().getStoredWeather().first()
        //then
        Assert.assertThat(weather,CoreMatchers.`is`(emptyList()))
    }
    @Test
    fun insertFav_FavObj() = mainRule.runBlockingTest {
        //Given
        val fav4 = FavModel(3.0, 3.0)
        database.getDAO().insertFavWeather(fav4)
        //when
        val favourite= database.getDAO().getFavWeather().first()
        //then
        Assert.assertThat(favourite,CoreMatchers.notNullValue())
    }
    @Test
    fun deleteFav_FavObj() = mainRule.runBlockingTest {
        //Given
        val fav4 = FavModel(3.0, 3.0)
        database.getDAO().insertFavWeather(fav4)
        database.getDAO().deleteWeather(fav4)
        //when
        val favourite= database.getDAO().getFavWeather().first()
        //then
        Assert.assertThat(favourite,CoreMatchers.`is`(emptyList()))
    }
    @Test
    fun insertAlert_alertObj() = mainRule.runBlockingTest {
        //Given
        val alert4 = UserAlerts(4L, 4L, 4L, "")
        database.getDAO().insertUserAlert(alert4)
        //when
        val myAlert= database.getDAO().getStoredUserAlert().first()
        //then
        Assert.assertThat(myAlert,CoreMatchers.notNullValue())
    }

    @Test
    fun deleteAlert_alertObj() = mainRule.runBlockingTest {
        //Given
        val alert4 = UserAlerts(4L, 4L, 4L, "")
        database.getDAO().insertUserAlert(alert4)
        database.getDAO().deleteUserAlert(alert4)
        //when
        val myAlert= database.getDAO().getStoredUserAlert().first()
        //then
        Assert.assertThat(myAlert,CoreMatchers.`is`(emptyList()))
    }
    @Test
    fun getAlerts_alertObj() = runBlockingTest {
        //Given
        val alert1 = UserAlerts(0L, 0L, 0L, "")
        val alert2 = UserAlerts(1L, 1L, 1L, "")
        val alert3 = UserAlerts(2L, 2L, 2L, "")
        val alert4 = UserAlerts(4L, 4L, 4L, "")
        database.getDAO().insertUserAlert(alert1)
        database.getDAO().insertUserAlert(alert2)
        database.getDAO().insertUserAlert(alert3)
        database.getDAO().insertUserAlert(alert4)

        //when
         val myAlertSize=database.getDAO().getStoredUserAlert().first()
        //then
        Assert.assertThat(myAlertSize.size,CoreMatchers.`is`(4))

    }
}