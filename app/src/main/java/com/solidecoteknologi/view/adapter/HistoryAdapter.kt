package com.solidecoteknologi.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.solidecoteknologi.R
import com.solidecoteknologi.data.DataHistoryItem
import com.solidecoteknologi.databinding.CardHistoryBinding
import com.solidecoteknologi.utils.formatDate

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {
    private var data: List<DataHistoryItem> = listOf()
    class ListViewHolder(private val binding : CardHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun setItem(item: DataHistoryItem) {
            binding.apply {
                datetime.text = item.createdAt
                amount.text = item.weight
                type.text = item.category.name
                when (item.category.name){
                    "E-Waste" -> {cardType.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.ewaste))}
                    "Anorganik" -> {cardType.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.anorganik))}
                    "Organik" -> {cardType.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.organik))}
                }
            }

        }

    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = CardHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = data[position]
        holder.setItem(item)
    }

    fun submitList(list: List<DataHistoryItem>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return data.size
            }

            override fun getNewListSize(): Int {
                return list.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == list[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return data[oldItemPosition] == list[newItemPosition]
            }
        })
        data = list
        diffResult.dispatchUpdatesTo(this)
    }
}