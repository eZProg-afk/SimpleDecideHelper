package spiral.bit.dev.simpledecidehelper.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.simpledecidehelper.databinding.DecisionItemBinding
import spiral.bit.dev.simpledecidehelper.listeners.ComplDeleteListener
import spiral.bit.dev.simpledecidehelper.models.CompletedDecision

class ComplDecisionAdapter :
    RecyclerView.Adapter<ComplDecisionAdapter.DecisionViewHolder>() {

    lateinit var deleteListener: ComplDeleteListener

    private val diffCallback = object : DiffUtil.ItemCallback<CompletedDecision>() {
        override fun areItemsTheSame(
            oldItem: CompletedDecision,
            newItem: CompletedDecision
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CompletedDecision,
            newItem: CompletedDecision
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    fun setMyDeleteListener(deleteListener: ComplDeleteListener) {
        this.deleteListener = deleteListener
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    inner class DecisionViewHolder(private val itemBinding: DecisionItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(completedDecision: CompletedDecision, position: Int) {
            itemBinding.decProgressText.text = completedDecision.currentProgress.toString()
            itemBinding.decTitle.text = completedDecision.title
            itemBinding.decDeleteImg.setOnClickListener {
                deleteListener.onDelete(completedDecision, position)
            }
            itemBinding.cardView.setBackgroundColor(Color.parseColor(completedDecision.color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DecisionViewHolder {
        return DecisionViewHolder(
            DecisionItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DecisionViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<CompletedDecision>) = differ.submitList(list)
}