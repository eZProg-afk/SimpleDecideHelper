package spiral.bit.dev.simpledecidehelper.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.adapters.ProsConsAdapter
import spiral.bit.dev.simpledecidehelper.databinding.FragmentProsConsBinding
import spiral.bit.dev.simpledecidehelper.listeners.DeleteProsConsListener
import spiral.bit.dev.simpledecidehelper.listeners.InsertProsConsListener
import spiral.bit.dev.simpledecidehelper.models.Decision
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem
import spiral.bit.dev.simpledecidehelper.other.ProsConsBottomSheet
import spiral.bit.dev.simpledecidehelper.other.checkRecyclerProsCons
import spiral.bit.dev.simpledecidehelper.viewmodels.ProsConsViewModel
import java.util.*

@AndroidEntryPoint
class ProsConsFragment : Fragment(R.layout.fragment_pros_cons), DeleteProsConsListener,
    InsertProsConsListener {

    private val prosConsViewModel: ProsConsViewModel by viewModels()
    private val prosConsBinding: FragmentProsConsBinding by viewBinding(
        FragmentProsConsBinding::bind
    )

    private val myProsConsBottomSheet by lazy { ProsConsBottomSheet() }
    private val prosConsAdapter by lazy { ProsConsAdapter() }

    private var listOfProsCons = arrayListOf<ProsConsItem>()
    private var decision: Decision? = null
    private val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN
                or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val sourcePosition = viewHolder.adapterPosition
            val targetPosition = target.adapterPosition
            Collections.swap(listOfProsCons, sourcePosition, targetPosition)
            prosConsAdapter.notifyItemMoved(sourcePosition, targetPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        arguments?.let {
            decision = it.getSerializable("decision") as Decision?
            decision?.id?.let { it1 -> prosConsViewModel.setParentId(it1) }
        }

        myProsConsBottomSheet.setMyInsertListener(this)
        prosConsAdapter.setMyDeleteProsConsListener(this)

        prosConsViewModel.allProsCons.observe(viewLifecycleOwner) {
            listOfProsCons = it as ArrayList<ProsConsItem>
            prosConsAdapter.submitList(it)
            it checkRecyclerProsCons (prosConsBinding)
        }

        prosConsBinding.prosConsRv.apply {
            adapter = prosConsAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            helper.attachToRecyclerView(this)
        }
    }

    override fun onDelete(prosConsItem: ProsConsItem, position: Int) {
        prosConsViewModel.deleteProsConsItem(prosConsItem)
        prosConsAdapter.notifyItemRemoved(position)
        prosConsViewModel.allProsCons.observe(viewLifecycleOwner) {
            it checkRecyclerProsCons (prosConsBinding)
        }
    }

    override fun onInsert(prosConsItem: ProsConsItem, position: Int) {
        decision?.id?.let {
            ProsConsItem(
                prosConsItem.id,
                it,
                prosConsItem.title,
                prosConsItem.isProf,
                prosConsItem.weight
            )
        }?.let {
            prosConsViewModel.insertProsConsItem(
                it
            )
        }
        prosConsBinding.prosConsRv.smoothScrollToPosition(position)
    }
}