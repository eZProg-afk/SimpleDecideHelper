package spiral.bit.dev.simpledecidehelper.listeners

import spiral.bit.dev.simpledecidehelper.models.CompletedDecision

interface ComplOpenListener {
    fun open(completedDecision: CompletedDecision)
}