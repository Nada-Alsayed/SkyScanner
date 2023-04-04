package eg.gov.iti.skyscanner.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "UserAlerts",primaryKeys = ["startLongDate", "endLongDate"])
data class UserAlerts(
    @ColumnInfo(name = "startLongDate")
    var startLongDate:Long,
    @ColumnInfo(name = "endLongDate")
    var endLongDate:Long,
  //  @ColumnInfo(name = "alertOption")
    var timeAlert:Long,
    var alertOption:String
)