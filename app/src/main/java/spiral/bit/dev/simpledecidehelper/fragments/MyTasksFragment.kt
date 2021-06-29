package spiral.bit.dev.simpledecidehelper.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.activities.ProsConsActivity
import spiral.bit.dev.simpledecidehelper.adapters.DecisionAdapter
import spiral.bit.dev.simpledecidehelper.databinding.FragmentMyTasksBinding
import spiral.bit.dev.simpledecidehelper.listeners.DeleteListener
import spiral.bit.dev.simpledecidehelper.listeners.InsertDecisionListener
import spiral.bit.dev.simpledecidehelper.listeners.OpenListener
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.other.DecisionBottomSheet
import spiral.bit.dev.simpledecidehelper.other.checkRecyclerDecisions
import spiral.bit.dev.simpledecidehelper.viewmodels.DecisionsViewModel
import spiral.bit.dev.simpledecidehelper.viewmodels.ProsConsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MyTasksFragment : Fragment(R.layout.fragment_my_tasks), DeleteListener,
    InsertDecisionListener,
    OpenListener {

    private val decisionsViewModel: DecisionsViewModel by viewModels()
    private val prosConsViewModel: ProsConsViewModel by viewModels()
    private val myTasksBinding: FragmentMyTasksBinding by viewBinding(
        FragmentMyTasksBinding::bind
    )
    lateinit var myDecisionBottomSheet: DecisionBottomSheet
    private val decisionAdapter by lazy { DecisionAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        decisionAdapter.setMyDeleteListener(this)
        decisionAdapter.setMyOpenListener(this)

        decisionsViewModel.allDecisions.observe(viewLifecycleOwner) {
            decisionAdapter.submitList(it)
            it checkRecyclerDecisions myTasksBinding
        }

        myTasksBinding.decisionsRv.apply {
            adapter = decisionAdapter
            hasFixedSize()
        }
    }

    override fun onDelete(decision: Decision, position: Int) {
        prosConsViewModel.apply {
            setParentId(decision.id)
            decisionsViewModel.insertCompletedDecision(
                CompletedDecision(
                    decision.id,
                    decision.title,
                    decision.currentProgress,
                    decision.color
                )
            )
            allComplProsCons.observe(viewLifecycleOwner) {
                it.forEach { prosConsItem ->
                    prosConsViewModel.insertComplProsConsItem(prosConsItem)
                }
            }
            deleteAllProsConsByID(decision.id)
            decisionsViewModel.deleteDecision(decision)
            decisionAdapter.notifyItemRemoved(position)
            decisionsViewModel.allDecisions.observe(viewLifecycleOwner, {
                it checkRecyclerDecisions myTasksBinding
            })
        }
    }

    override fun onInsert(decision: Decision, position: Int) {
        decisionsViewModel.insertDecision(decision)
    }

    fun setModalBottomSheet(decisionBottomSheet: DecisionBottomSheet) {
        this.myDecisionBottomSheet = decisionBottomSheet
        //myDecisionBottomSheet.setMyInsertListener(this)
    }

    override fun open(decision: Decision) {
        Intent(activity, ProsConsActivity::class.java)
            .apply { putExtra("decision", decision) }.also { startActivity(it) }
    }
}