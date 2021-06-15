package spiral.bit.dev.simpledecidehelper.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.adapters.ComplDecisionAdapter
import spiral.bit.dev.simpledecidehelper.databinding.FragmentComplTasksBinding
import spiral.bit.dev.simpledecidehelper.listeners.ComplDeleteListener
import spiral.bit.dev.simpledecidehelper.listeners.InsertComplDecisionListener
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem
import spiral.bit.dev.simpledecidehelper.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CompletedTasksFragment : Fragment(R.layout.fragment_compl_tasks), ComplDeleteListener,
    InsertComplDecisionListener {

    @Inject
    lateinit var decisionAdapter: ComplDecisionAdapter
    private val decisionsViewModel: MainViewModel by viewModels()

    private val complTasksBinding: FragmentComplTasksBinding by viewBinding(
        FragmentComplTasksBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        decisionAdapter.setMyDeleteListener(this)
        decisionsViewModel.allCompletedDecisions.observe(viewLifecycleOwner) {
            decisionAdapter.submitList(it)
            checkIsRecyclerEmpty(it)
        }

        complTasksBinding.decisionsRv.apply {
            adapter = decisionAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
        }
    }

    private fun checkIsRecyclerEmpty(list: List<CompletedDecision>) {
        if (list.isEmpty()) {
            complTasksBinding.emptyAnim.visibility = View.VISIBLE
            complTasksBinding.addTaskTv.visibility = View.VISIBLE
            complTasksBinding.noTasksTv.visibility = View.VISIBLE
            complTasksBinding.decisionsRv.visibility = View.GONE
            complTasksBinding.emptyAnim.playAnimation()
        } else {
            complTasksBinding.emptyAnim.visibility = View.GONE
            complTasksBinding.addTaskTv.visibility = View.GONE
            complTasksBinding.noTasksTv.visibility = View.GONE
            complTasksBinding.decisionsRv.visibility = View.VISIBLE
        }
    }


    override fun onDelete(completedDecision: CompletedDecision, position: Int) {
        decisionsViewModel.deleteAllComplProsConsByID(completedDecision.id)
        decisionsViewModel.deleteCompletedDecision(completedDecision)
        decisionAdapter.notifyItemRemoved(position)
        decisionsViewModel.allCompletedDecisions.observe(viewLifecycleOwner) { checkIsRecyclerEmpty(it) }
    }

    override fun onInsert(completedDecision: CompletedDecision, position: Int) {
        decisionsViewModel.insertCompletedDecision(completedDecision)
    }
}