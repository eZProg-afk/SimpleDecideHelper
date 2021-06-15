package spiral.bit.dev.simpledecidehelper.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.activities.ProsConsActivity
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.adapters.DecisionAdapter
import spiral.bit.dev.simpledecidehelper.databinding.FragmentMyTasksBinding
import spiral.bit.dev.simpledecidehelper.listeners.DeleteListener
import spiral.bit.dev.simpledecidehelper.listeners.InsertDecisionListener
import spiral.bit.dev.simpledecidehelper.listeners.OpenListener
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.other.DecisionBottomSheet
import spiral.bit.dev.simpledecidehelper.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MyTasksFragment : Fragment(R.layout.fragment_my_tasks), DeleteListener,
    InsertDecisionListener,
    OpenListener {

    private val myTasksBinding: FragmentMyTasksBinding by viewBinding(FragmentMyTasksBinding::bind)
    private val decisionsViewModel: MainViewModel by viewModels()
    lateinit var myDecisionBottomSheet: DecisionBottomSheet

    @Inject
    lateinit var decisionAdapter: DecisionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        decisionAdapter.setMyDeleteListener(this)
        decisionAdapter.setMyOpenListener(this)

        decisionsViewModel.allDecisions.observe(viewLifecycleOwner) {
            decisionAdapter.submitList(it)
            checkIsRecyclerEmpty(it)
        }

        myTasksBinding.decisionsRv.apply {
            adapter = decisionAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
        }
    }

    private fun checkIsRecyclerEmpty(list: List<Decision>) {
        if (list.isEmpty()) {
            myTasksBinding.emptyAnim.visibility = View.VISIBLE
            myTasksBinding.addTaskTv.visibility = View.VISIBLE
            myTasksBinding.noTasksTv.visibility = View.VISIBLE
            myTasksBinding.decisionsRv.visibility = View.GONE
            myTasksBinding.emptyAnim.playAnimation()
        } else {
            myTasksBinding.emptyAnim.visibility = View.GONE
            myTasksBinding.addTaskTv.visibility = View.GONE
            myTasksBinding.noTasksTv.visibility = View.GONE
            myTasksBinding.decisionsRv.visibility = View.VISIBLE
        }
    }

    override fun onDelete(decision: Decision, position: Int) {
        decisionsViewModel.insertCompletedDecision(
            CompletedDecision(
                decision.id,
                decision.title,
                decision.currentProgress,
                decision.color
            )
        )
        decisionsViewModel.setParentId(decision.id)
        decisionsViewModel.allComplProsCons.observe(this) {
            for (prosConsItem in it) {
                decisionsViewModel.insertComplProsConsItem(prosConsItem)
            }
        }
        decisionsViewModel.deleteAllProsConsByID(decision.id)
        decisionsViewModel.deleteDecision(decision)
        decisionAdapter.notifyItemRemoved(position)
        decisionsViewModel.allDecisions.observe(this, { checkIsRecyclerEmpty(it) })
    }

    override fun onInsert(decision: Decision, position: Int) {
        decisionsViewModel.insertDecision(decision)
    }

    fun setModalBottomSheet(decisionBottomSheet: DecisionBottomSheet) {
        this.myDecisionBottomSheet = decisionBottomSheet
        myDecisionBottomSheet.setMyInsertListener(this)
    }

    override fun open(decision: Decision) {
        val intent = Intent(activity, ProsConsActivity::class.java)
        intent.putExtra("decision", decision)
        startActivity(intent)
    }
}