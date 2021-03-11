package spiral.bit.dev.simpledecidehelper.listeners

import spiral.bit.dev.simpledecidehelper.models.ProsConsItem

interface DeleteProsConsListener {
    fun onDelete(prosConsItem: ProsConsItem, position: Int)
}