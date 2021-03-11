package spiral.bit.dev.simpledecidehelper.listeners

import spiral.bit.dev.simpledecidehelper.models.Decision

interface UpdateListener {
    fun onUpdate(decision: Decision)
}