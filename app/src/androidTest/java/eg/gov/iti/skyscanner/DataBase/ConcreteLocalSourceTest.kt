package eg.gov.iti.skyscanner.DataBase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Database
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import eg.gov.iti.skyscanner.MainRule
import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.UserAlerts
import eg.gov.iti.skyscanner.models.WeatherDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@ExperimentalStdlibApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ConcreteLocalSourceTest{

    @ExperimentalCoroutinesApi
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalStdlibApi
    @get:Rule
    val mainRule = MainRule()


    lateinit var localSource: ConcreteLocalSource
    lateinit var database: DataBase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
           DataBase::class.java
        ).allowMainThreadQueries().build()
        localSource= ConcreteLocalSource(ApplicationProvider.getApplicationContext())
    }

    @After
    fun closeDataBase(){
        database.close()
    }

    @Test
    fun insertWeather_WeatherDetailObj()=mainRule.runBlockingTest {
        //Given
        localSource.deleteAllWeather()
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
        localSource.insertWeather(weather4)
        //when
        val weather=localSource.allStoredWeather().first()
        //then
        assertThat(weather.size, CoreMatchers.`is`(1))
    }

    fun deleteWeather_() = mainRule.runBlockingTest {
        //Given
        val weather4 = WeatherDetail(
            emptyList(),
            WeatherDetail.Current(6, 5.0, 0, 5.0, 1, 1, 1, 1, 0.0, 0.0, 1, emptyList(), 1, 0.0),
            emptyList(),
            emptyList(),
            5.0,
            5.0,
            "",
            0,
            1
        )
        localSource.insertWeather(weather4)
        localSource.deleteAllWeather()
        //when
        val weather= localSource.allStoredWeather().first()
        //then
       assertThat(weather.size,CoreMatchers.`is`(0))
    }

    @Test
    fun insertFav_FavObj() = mainRule.runBlockingTest {
        //Given
        val fav4 = FavModel(3.0, 3.0)
        localSource.insertWeatherFav(fav4)
        //when
        val favourite= localSource.allStoredWeatherFav().first()
        //then
        Assert.assertThat(favourite.get(favourite.size-1).latitude,CoreMatchers.`is`(3.0))
    }
    @Test
    fun deleteFav_FavObj() = mainRule.runBlockingTest {
        //Given
        val fav4 = FavModel(3.0, 3.0)
        localSource.insertWeatherFav(fav4)
        localSource.deleteWeather(fav4)
        //when
        val favourite= localSource.allStoredWeatherFav().first()
        //then
        Assert.assertThat(favourite,CoreMatchers.`is`(emptyList()))
    }

    @Test
    fun insertAlert_AlertObj() = mainRule.runBlockingTest {
        //Given
        val alert4 = UserAlerts(6L, 4L, 4L, "")
        localSource.insertUserAlerts(alert4)
        //when
        val myAlert=localSource.allStoredUserAlerts().first()
        //then
        Assert.assertThat(myAlert.get(myAlert.size-1).startLongDate,CoreMatchers.`is`(6L))
    }
    @Test
    fun deleteAlert_AlertObj() = mainRule.runBlockingTest {
        //Given
        val alert4 = UserAlerts(10L, 3L,3L,"")
        localSource.insertUserAlerts(alert4)
        val alertNumBeforeDelete= localSource.allStoredUserAlerts().first().size
        localSource.deleteUserAlert(alert4)
        //when
        val alertNumAfterDelete= localSource.allStoredUserAlerts().first().size
        //then
        Assert.assertThat(alertNumBeforeDelete-1,CoreMatchers.`is`(alertNumAfterDelete))
    }
}