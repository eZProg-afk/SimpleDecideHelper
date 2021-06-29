package spiral.bit.dev.simpledecidehelper.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.simpledecidehelper.databinding.DecisionItemBinding
import spiral.bit.dev.simpledecidehelper.listeners.DeleteListener
import spiral.bit.dev.simpledecidehelper.listeners.OpenListener
import spiral.bit.dev.simpledecidehelper.models.Decision

class DecisionAdapter :
    RecyclerView.Adapter<DecisionAdapter.DecisionViewHolder>() {

    lateinit var deleteListener: DeleteListener
    lateinit var openListener: OpenListener

    private val diffCallback = object : DiffUtil.ItemCallback<Decision>() {
        override fun areItemsTheSame(oldItem: Decision, newItem: Decision): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Decision, newItem: Decision): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    fun setMyDeleteListener(deleteListener: DeleteListener) {
        this.deleteListener = deleteListener
    }

    fun setMyOpenListener(openListener: OpenListener) {
        this.openListener = openListener
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    inner class DecisionViewHolder(private val itemBinding: DecisionItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(decision: Decision, position: Int) {
            itemBinding.decProgressText.text = decision.currentProgress.toString()
            itemBinding.decTitle.text = decision.title
            itemBinding.decDeleteImg.setOnClickListener {
                deleteListener.onDelete(
                    decision,
                    position
                )
            }
            itemView.setOnClickListener { openListener.open(decision) }
            itemBinding.cardView.setBackgroundColor(Color.parseColor(decision.color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DecisionViewHolder {
        return DecisionViewHolder(
            DecisionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DecisionViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Decision>) = differ.submitList(list)
}