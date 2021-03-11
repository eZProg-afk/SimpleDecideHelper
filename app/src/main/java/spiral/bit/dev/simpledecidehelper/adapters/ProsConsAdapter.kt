package spiral.bit.dev.simpledecidehelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.listeners.DeleteProsConsListener
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem

class ProsConsAdapter : RecyclerView.Adapter<ProsConsAdapter.ProsConsViewHolder>() {

    lateinit var deleteProsConsListener: DeleteProsConsListener

    val diffCallback = object : DiffUtil.ItemCallback<ProsConsItem>() {
        override fun areItemsTheSame(oldItem: ProsConsItem, newItem: ProsConsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProsConsItem, newItem: ProsConsItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    inner class ProsConsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numValueTv: TextView = itemView.findViewById(R.id.num_value_tv)
        val titleOfTaskTv: TextView = itemView.findViewById(R.id.pros_cons_title)
        val deleteImg: ImageView = itemView.findViewById(R.id.delete_pros_cons_btn)
        val linearBack: LinearLayout = itemView.findViewById(R.id.num_block)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProsConsViewHolder {
        return ProsConsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pros_cons_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProsConsViewHolder, position: Int) {
        val prosConsItem = differ.currentList[position]
        holder.numValueTv.text = prosConsItem.weight.toString()
        holder.titleOfTaskTv.text = prosConsItem.title
        if (prosConsItem.isProf) holder.linearBack.setBackgroundResource(R.drawable.linear_back_pros)
        else holder.linearBack.setBackgroundResource(R.drawable.linear_back_cons)
        holder.deleteImg.setOnClickListener {
            deleteProsConsListener.onDelete(
                prosConsItem,
                position
            )
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<ProsConsItem>) = differ.submitList(list)

    fun setMyDeleteProsConsListener(deleteProsConsListener: DeleteProsConsListener) {
        this.deleteProsConsListener = deleteProsConsListener
    }
}