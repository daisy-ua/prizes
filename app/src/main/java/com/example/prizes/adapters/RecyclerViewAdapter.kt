package com.example.prizes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prizes.callbacks.OnItemCheckListener
import com.example.prizes.data.entities.Prize
import com.example.prizes.databinding.ContainerPrizeListItemBinding
import com.example.prizes.view_models.MainViewModel

class RecyclerViewAdapter(private val viewModel: MainViewModel)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private var data: List<Prize>? = null
    private var states: MutableList<Boolean> = mutableListOf()
    private lateinit var onClickCallback: OnItemCheckListener

    inner class ViewHolder(val binding: ContainerPrizeListItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Prize, position: Int) {
            binding.apply {
                itemName.text = item.title
                itemPrice.text = item.price.toString()
                itemCheckBox.isChecked = states[position]
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ContainerPrizeListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding).also { states.add(false) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data?.let {
            val item = it[position]

            holder.bind(item, position)

            holder.itemView.setOnClickListener{
                with(holder.binding) {
                    if (itemCheckBox.isChecked)
                        onClickCallback.onItemUncheck(item, position)
                    else onClickCallback.onItemCheck(item, position)
                    performCheckBoxClick(holder, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    fun submitData(data: List<Prize>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun deleteItemAtPosition(position: Int) { //TODO: move viewmodel
        data?.get(position)?.also {
            if (states[position])
                viewModel.onItemUncheckCallback(it, position)

            states.removeAt(position)
            viewModel.deletePrize(it)

            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, data!!.size)
        }
    }

    fun performCheckBoxClick(holder: ViewHolder, position: Int) {
        states[position] = !states[position]
        holder.binding.itemCheckBox.isChecked = states[position]
    }


    fun setOnClickListener(listener: OnItemCheckListener) {
        onClickCallback = listener
    }
}