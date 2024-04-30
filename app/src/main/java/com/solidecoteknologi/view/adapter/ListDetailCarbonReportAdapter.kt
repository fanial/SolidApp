package com.solidecoteknologi.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.solidecoteknologi.data.DataItemDetailReport
import com.solidecoteknologi.databinding.ListDetailReportBinding

class ListDetailCarbonReportAdapter : RecyclerView.Adapter<ListDetailCarbonReportAdapter.ListViewHolder>() {
    private var data: List<DataItemDetailReport> = listOf()

    class ListViewHolder(private val binding : ListDetailReportBinding): RecyclerView.ViewHolder(binding.root){
        fun setItem(item: DataItemDetailReport) {
            binding.apply {
                type.text = item.category
                qty.text = item.carbon.toString()
            }

        }

    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ListDetailReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = data[position]
        holder.setItem(item)
    }

    fun submitList(list: List<DataItemDetailReport>) {
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