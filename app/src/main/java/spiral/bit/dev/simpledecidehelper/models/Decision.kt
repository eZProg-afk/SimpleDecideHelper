package spiral.bit.dev.simpledecidehelper.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "decisions_table")
data class Decision(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "parent_id")
    var id: Int,
    var title: String,
    var currentProgress: Int,
    var color: String?
) : Serializable {
    constructor(): this(
        0,
        "",
        0,
        ""
    )
}
