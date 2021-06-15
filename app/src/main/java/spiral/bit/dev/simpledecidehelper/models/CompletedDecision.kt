package spiral.bit.dev.simpledecidehelper.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "completed_decisions_table")
data class CompletedDecision(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "parent_id")
    var id: Int,
    var title: String,
    var currentProgress: Int,
    var color: String?
) : Serializable