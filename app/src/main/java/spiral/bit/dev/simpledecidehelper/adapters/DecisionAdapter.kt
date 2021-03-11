package spiral.bit.dev.simpledecidehelper.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.listeners.DeleteListener
import spiral.bit.dev.simpledecidehelper.listeners.OpenListener
import spiral.bit.dev.simpledecidehelper.models.Decision

class DecisionAdapter :
    RecyclerView.Adapter<DecisionAdapter.DecisionViewHolder>() {

    lateinit var deleteListener: DeleteListener
    lateinit var openListener: OpenListener

    val diffCallback = object : DiffUtil.ItemCallback<Decision>() {
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

    val differ = AsyncListDiffer(this, diffCallback)

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
        holder.itemView.setOnClickListener { openListener.open(decision) }
        holder.cardView.setBackgroundColor(Color.parseColor(decision.color))
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Decision>) = differ.submitList(list)
}