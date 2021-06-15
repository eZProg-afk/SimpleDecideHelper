package spiral.bit.dev.simpledecidehelper.listeners

import spiral.bit.dev.simpledecidehelper.models.CompletedDecision

interface InsertComplDecisionListener {
    fun onInsert(completedDecision: CompletedDecision, position: Int)
}