package spiral.bit.dev.simpledecidehelper.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "prof_cons_table", foreignKeys = [ForeignKey(
        entity = Decision::class,
        parentColumns = arrayOf("parent_id"), childColumns = arrayOf("decisionId")
    )]
)
data class ProsConsItem(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var decisionId: Int,
    val title: String,
    val isProf: Boolean,
    val weight: Int
)