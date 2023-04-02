package eg.gov.iti.skyscanner.models

import androidx.room.Entity

@Entity(tableName="favourite",primaryKeys = ["latitude", "longitude"])
data class FavModel(
    val latitude:Double,
    val longitude:Double)