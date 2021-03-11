package spiral.bit.dev.simpledecidehelper.listeners

import spiral.bit.dev.simpledecidehelper.models.Decision

interface OpenListener {
    fun open(decision: Decision)
}