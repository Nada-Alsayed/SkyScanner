package eg.gov.iti.skyscanner.DataBase

import android.content.Context
import androidx.room.*

import eg.gov.iti.skyscanner.models.WeatherDetail
import eg.gov.iti.skyscanner.models.WeatherTypeConverter

@Database(entities = [WeatherDetail::class], exportSchema = false ,version = 1)
@TypeConverters(WeatherTypeConverter::class)
abstract class DataBase:RoomDatabase() {
    abstract fun getDAO():DAO
    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null
        fun getInstance(ctx: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, DataBase::class.java, "database_z"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}