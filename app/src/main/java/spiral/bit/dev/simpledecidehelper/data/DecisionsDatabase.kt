package spiral.bit.dev.simpledecidehelper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.models.CompletedProsConsItem
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem

@Database(
    entities = [Decision::class, ProsConsItem::class, CompletedDecision::class, CompletedProsConsItem::class],
    version = 6, exportSchema = false
)
abstract class DecisionsDatabase : RoomDatabase() {

    abstract fun getDecisionsDao(): DecisionsDAO

    abstract fun getProsConsDao(): ProsConsDao
}