package spiral.bit.dev.simpledecidehelper.other

import androidx.core.view.isVisible
import spiral.bit.dev.simpledecidehelper.databinding.FragmentComplTasksBinding
import spiral.bit.dev.simpledecidehelper.databinding.FragmentMyTasksBinding
import spiral.bit.dev.simpledecidehelper.databinding.FragmentProsConsBinding
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem

infix fun List<ProsConsItem>.checkRecyclerProsCons(prosConsBinding: FragmentProsConsBinding) {
    if (this.isEmpty()) {
        with(prosConsBinding) {
            emptyAnim.isVisible = true
            addTaskTv.isVisible = true
            noTasksTv.isVisible = true
            prosConsRv.isVisible = false
            emptyAnim.playAnimation()
        }
    } else {
        with(prosConsBinding) {
            emptyAnim.isVisible = false
            addTaskTv.isVisible = false
            noTasksTv.isVisible = false
            prosConsRv.isVisible = true
        }
    }
}

infix fun List<Decision>.checkRecyclerDecisions(myTasksBinding: FragmentMyTasksBinding) {
    if (this.isEmpty()) {
        with(myTasksBinding) {
            emptyAnim.isVisible = true
            addTaskTv.isVisible = true
            noTasksTv.isVisible = true
            decisionsRv.isVisible = false
            emptyAnim.playAnimation()
        }
    } else {
        with(myTasksBinding) {
            emptyAnim.isVisible = false
            addTaskTv.isVisible = false
            noTasksTv.isVisible = false
            decisionsRv.isVisible = true
        }
    }
}

infix fun List<CompletedDecision>.checkRecyclerCompl(complTasksBinding: FragmentComplTasksBinding) {
    if (this.isEmpty()) {
        with(complTasksBinding) {
            emptyAnim.isVisible = true
            addTaskTv.isVisible = true
            noTasksTv.isVisible = true
            decisionsRv.isVisible = false
            emptyAnim.playAnimation()
        }
    } else {
        with(complTasksBinding) {
            emptyAnim.isVisible = false
            addTaskTv.isVisible = false
            noTasksTv.isVisible = false
            decisionsRv.isVisible = true
        }
    }
}