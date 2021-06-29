package spiral.bit.dev.simpledecidehelper.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.adapters.ComplDecisionAdapter
import spiral.bit.dev.simpledecidehelper.databinding.FragmentComplTasksBinding
import spiral.bit.dev.simpledecidehelper.listeners.ComplDeleteListener
import spiral.bit.dev.simpledecidehelper.listeners.ComplDismissListener
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.other.checkRecyclerCompl
import spiral.bit.dev.simpledecidehelper.viewmodels.DecisionsViewModel
import spiral.bit.dev.simpledecidehelper.viewmodels.ProsConsViewModel

@AndroidEntryPoint
class CompletedTasksFragment : Fragment(R.layout.fragment_compl_tasks), ComplDeleteListener,
ComplDismissListener {

    private val decisionsViewModel: DecisionsViewModel by viewModels()
    private val prosConsViewModel: ProsConsViewModel by viewModels()
    private val complTasksBinding: FragmentComplTasksBinding by viewBinding(
        FragmentComplTasksBinding::bind
    )
    private val decisionAdapter by lazy { ComplDecisionAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        decisionAdapter.setMyDeleteListener(this)
        decisionsViewModel.allCompletedDecisions.observe(viewLifecycleOwner) {
            decisionAdapter.submitList(it)
            it checkRecyclerCompl complTasksBinding
        }

        complTasksBinding.decisionsRv.apply {
            adapter = decisionAdapter
            hasFixedSize()
        }
    }

    override fun onDelete(completedDecision: CompletedDecision, position: Int) {
        prosConsViewModel.deleteAllComplProsConsByID(completedDecision.id)
        decisionsViewModel.deleteCompletedDecision(completedDecision)
        decisionAdapter.notifyItemRemoved(position)
        decisionsViewModel.allCompletedDecisions.observe(viewLifecycleOwner) {
            it checkRecyclerCompl complTasksBinding
        }
    }

    override fun editDismiss() {
    }
}