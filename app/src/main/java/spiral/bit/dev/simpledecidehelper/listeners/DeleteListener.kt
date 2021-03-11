package spiral.bit.dev.simpledecidehelper.listeners

import spiral.bit.dev.simpledecidehelper.models.Decision

interface DeleteListener {
    fun onDelete(decision: Decision, position: Int)
}