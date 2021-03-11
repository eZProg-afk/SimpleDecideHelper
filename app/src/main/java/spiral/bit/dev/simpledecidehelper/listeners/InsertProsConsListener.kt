package spiral.bit.dev.simpledecidehelper.listeners

import spiral.bit.dev.simpledecidehelper.models.ProsConsItem

interface InsertProsConsListener {
    fun onInsert(prosConsItem: ProsConsItem, position: Int)
}