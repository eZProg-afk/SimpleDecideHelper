package spiral.bit.dev.simpledecidehelper.listeners

import spiral.bit.dev.simpledecidehelper.models.CompletedDecision

interface ComplDeleteListener {
    fun onDelete(completedDecision: CompletedDecision, position: Int)
}