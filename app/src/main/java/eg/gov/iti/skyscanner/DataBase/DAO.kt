package eg.gov.iti.skyscanner.DataBase

import androidx.room.*
import eg.gov.iti.skyscanner.models.WeatherDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
    @Query("Select * FROM weather")
    suspend fun getStoredWeather():List<WeatherDetail>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(weatherDetail: WeatherDetail)
    @Query("DELETE FROM weather")
    suspend fun deleteAllWeather()
    @Delete
    suspend fun deleteWeather(weatherDetail: WeatherDetail)
}