package eg.gov.iti.skyscanner.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserAlerts")
data class UserAlerts(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
  //  @ColumnInfo(name = "startLongDate")
    var startLongDate:Long,
  //  @ColumnInfo(name = "endLongDate")
    var endLongDate:Long,
  //  @ColumnInfo(name = "alertOption")
    //var alertOption:String
)