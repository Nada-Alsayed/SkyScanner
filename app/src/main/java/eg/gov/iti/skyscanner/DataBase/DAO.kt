package eg.gov.iti.skyscanner.DataBase

import androidx.room.*
import eg.gov.iti.skyscanner.models.FavModel
import eg.gov.iti.skyscanner.models.WeatherDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
    @Query("Select * FROM favourite")
     fun getFavWeather():Flow<List<FavModel>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavWeather(weatherDetail: FavModel)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(weatherDetail: WeatherDetail)
    @Query("DELETE FROM weather")
    suspend fun deleteAllWeather()
    @Delete
    suspend fun deleteWeather(fav: FavModel)
    @Query("Select * FROM weather")
     fun getStoredWeather():Flow<List<WeatherDetail>>
   /*
   @Delete
    suspend fun deleteWeather(weatherDetail: WeatherDetail)
    */

}