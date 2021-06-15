package spiral.bit.dev.simpledecidehelper.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.listeners.ComplDeleteListener
import spiral.bit.dev.simpledecidehelper.listeners.ComplOpenListener
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

    inner class DecisionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textProgressTv: TextView = itemView.findViewById(R.id.dec_progress_text)
        val titleDecisionTv: TextView = itemView.findViewById(R.id.dec_title)
        val deleteDecisionImg: ImageView = itemView.findViewById(R.id.dec_delete_img)
        val cardView: ConstraintLayout = itemView.findViewById(R.id.card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DecisionViewHolder {
        return DecisionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.decision_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DecisionViewHolder, position: Int) {
        val decision = differ.currentList[position]
        holder.textProgressTv.text = "${decision.currentProgress}%"
        holder.titleDecisionTv.text = decision.title
        holder.deleteDecisionImg.setOnClickListener { deleteListener.onDelete(decision, position) }
        holder.cardView.setBackgroundColor(Color.parseColor(decision.color))
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<CompletedDecision>) = differ.submitList(list)
}