package spiral.bit.dev.simpledecidehelper.listeners

import spiral.bit.dev.simpledecidehelper.models.Decision

interface InsertDecisionListener {
    fun onInsert(decision: Decision, position: Int)
}