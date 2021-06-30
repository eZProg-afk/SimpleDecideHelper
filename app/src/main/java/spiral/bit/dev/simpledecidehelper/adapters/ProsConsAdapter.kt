package spiral.bit.dev.simpledecidehelper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.simpledecidehelper.R
import spiral.bit.dev.simpledecidehelper.databinding.ProsConsItemBinding
import spiral.bit.dev.simpledecidehelper.listeners.DeleteProsConsListener
import spiral.bit.dev.simpledecidehelper.models.ProsConsItem

class ProsConsAdapter : RecyclerView.Adapter<ProsConsAdapter.ProsConsViewHolder>() {

    lateinit var deleteProsConsListener: DeleteProsConsListener

    private val diffCallback = object : DiffUtil.ItemCallback<ProsConsItem>() {
        override fun areItemsTheSame(oldItem: ProsConsItem, newItem: ProsConsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProsConsItem, newItem: ProsConsItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    inner class ProsConsViewHolder(private val itemBinding: ProsConsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(prosConsItem: ProsConsItem, position: Int) {
            itemBinding.numValueTv.text = prosConsItem.weight.toString()
            itemBinding.prosConsTitle.text = prosConsItem.title
            if (prosConsItem.isProf) itemBinding.numBlock.setBackgroundResource(R.drawable.linear_back_pros)
            else itemBinding.numBlock.setBackgroundResource(R.drawable.linear_back_cons)
            itemBinding.deleteProsConsBtn.setOnClickListener {
                deleteProsConsListener.onDelete(
                    prosConsItem,
                    position
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProsConsViewHolder {
        return ProsConsViewHolder(
            ProsConsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProsConsViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<ProsConsItem>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    fun setMyDeleteProsConsListener(deleteProsConsListener: DeleteProsConsListener) {
        this.deleteProsConsListener = deleteProsConsListener
    }
}