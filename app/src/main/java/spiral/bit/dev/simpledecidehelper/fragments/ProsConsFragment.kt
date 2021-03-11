package spiral.bit.dev.simpledecidehelper.fragments

import android.os.Bundle
import android.util.Log
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
import spiral.bit.dev.simpledecidehelper.viewmodels.MainViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ProsConsFragment : Fragment(R.layout.fragment_pros_cons), DeleteProsConsListener,
    InsertProsConsListener {

    private val prosConsBinding: FragmentProsConsBinding by viewBinding(FragmentProsConsBinding::bind)
    private val mainViewModel: MainViewModel by viewModels()

    lateinit var myProsConsBottomSheet: ProsConsBottomSheet

    private var listOfProsCons = arrayListOf<ProsConsItem>()

    @Inject
    lateinit var prosConsAdapter: ProsConsAdapter

    private var decision: Decision? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            decision = it.getSerializable("decision") as Decision?
            decision?.id?.let { it1 -> mainViewModel.setParentId(it1) }
        }

        myProsConsBottomSheet.setMyInsertListener(this)
        prosConsAdapter.setMyDeleteProsConsListener(this)

        mainViewModel.allProsCons.observe(viewLifecycleOwner) {
            listOfProsCons = it as ArrayList<ProsConsItem>
            prosConsAdapter.submitList(it)
        }

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        prosConsBinding.prosConsRv.apply {
            adapter = prosConsAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
        }

        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
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

        helper.attachToRecyclerView(prosConsBinding.prosConsRv)
    }

    override fun onDelete(prosConsItem: ProsConsItem, position: Int) {
        mainViewModel.deleteProsConsItem(prosConsItem)
        prosConsAdapter.notifyItemRemoved(position)
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
            mainViewModel.insertProsConsItem(
                it
            )
        }
        prosConsBinding.prosConsRv.smoothScrollToPosition(position)
    }

    fun setModalBottomSheet(prosConsBottomSheet: ProsConsBottomSheet) {
        this.myProsConsBottomSheet = prosConsBottomSheet
    }
}