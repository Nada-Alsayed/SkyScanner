package eg.gov.iti.skyscanner.models

import eg.gov.iti.skyscanner.MainRule
import eg.gov.iti.skyscanner.models.DataBase.FakeLocalSource
import eg.gov.iti.skyscanner.network.FakeRemoteSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RepositoryTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainRule = MainRule()

    val alert1 = UserAlerts(0L, 0L, 0L, "")
    val alert2 = UserAlerts(1L, 1L, 1L, "")
    val alert3 = UserAlerts(2L, 2L, 2L, "")

    val listAlert = mutableListOf<UserAlerts>(alert1, alert2, alert3)

    val fav1 = FavModel(0.0, 0.0)
    val fav2 = FavModel(1.0, 1.0)
    val fav3 = FavModel(2.0, 2.0)

    val listFav = mutableListOf<FavModel>(fav1, fav2, fav3)

    val weather1 = WeatherDetail(
        emptyList(),
        WeatherDetail.Current(1, 0.0, 0, 0.0, 1, 1, 1, 1, 0.0, 0.0, 1, emptyList(), 1, 0.0),
        emptyList(),
        emptyList(),
        1.0,
        1.0,
        "",
        0,
        0
    )
    val weather2 = WeatherDetail(
        emptyList(),
        WeatherDetail.Current(0, 0.0, 0, 0.0, 1, 1, 1, 1, 0.0, 0.0, 1, emptyList(), 1, 0.0),
        emptyList(),
        emptyList(),
        2.0,
        2.0,
        "",
        0,
        0
    )
    val weather3 = WeatherDetail(
        emptyList(),
        WeatherDetail.Current(3, 0.0, 0, 0.0, 1, 1, 1, 1, 0.0, 0.0, 1, emptyList(), 1, 0.0),
        emptyList(),
        emptyList(),
        3.0,
        3.0,
        "",
        0,
        1
    )
    val listWeather = mutableListOf<WeatherDetail>(weather1, weather2, weather3)

    lateinit var fakeLocalSource: FakeLocalSource
    lateinit var fakeRemoteSource: FakeRemoteSource

    lateinit var repo: Repository


    @Before
    fun setUp() {
        fakeLocalSource = FakeLocalSource(listAlert, listFav, listWeather)
        fakeRemoteSource = FakeRemoteSource(weather1)

        repo = Repository.getInstance(fakeRemoteSource, fakeLocalSource)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun funGetWeather_returnWeatherDetailObj() = mainRule.runBlockingTest {
        //Given
        val lat = 0.0
        val lon = 0.0
        val unit = "metric"
        val lang = "en"
        val key = "00000"
        //when
        repo.getRetrofitWeather(lat, lon, unit, lang, key)?.collect {
            val weather = it
            assertThat(weather, Matchers.`is`(weather1))
        }

    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun funGetWeatherKelvin_returnWeatherDetailObj() = mainRule.runBlockingTest {
        //Given
        val lat = 0.0
        val lon = 0.0
        val lang = "en"
        val key = "00000"
        //when
        repo.getRetrofitWeatherKelvin(lat, lon, lang, key)?.collect {
            val weather = it
            //then
            assertThat(weather, Matchers.`is`(weather1))
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertWeather_Weather_(weather: WeatherDetail) = mainRule.runBlockingTest {
        //Given
        val weather4 = WeatherDetail(
            emptyList(),
            WeatherDetail.Current(5, 5.0, 0, 5.0, 1, 1, 1, 1, 0.0, 0.0, 1, emptyList(), 1, 0.0),
            emptyList(),
            emptyList(),
            55.0,
            5.0,
            "",
            0,
            1
        )
        //when
        repo.insertWeather(weather4)
        //then
        assertThat(listWeather[listWeather.size-1].lat, Matchers.`is`(55))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertFavLocation_favouriteLocation_(favModel: FavModel) = mainRule.runBlockingTest {
        //Given
        val fav4 = FavModel(3.0, 3.0)
        //when
        repo.insertFavWeather(fav4)
        val size = listFav.size
        //then
        assertThat(size, Matchers.`is`(4))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteAllFavLocation_favouriteLocation_(favModel: FavModel) = mainRule.runBlockingTest {
        //Given
        //when
        repo.deleteWeather(fav1)
        val size = listWeather.size
        //then
        assertThat(size, Matchers.`is`(2))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteAllWeather_noInput_noReturn() = mainRule.runBlockingTest {
        //Given
        // there is no given params
        //when
        repo.deleteAll()
        val size = listWeather.size
        //then
        assertThat(size, Matchers.`is`(0))
    }

}
