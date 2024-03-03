package com.solidecoteknologi.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.solidecoteknologi.R
import com.solidecoteknologi.data.DataCategoryItem
import com.solidecoteknologi.databinding.LayoutDropdownBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var onClick: ((DataCategoryItem) -> Unit)? = null
    private var selectedItemIndex: Int = RecyclerView.NO_POSITION

    private val differCallback = object : DiffUtil.ItemCallback<DataCategoryItem>(){
        override fun areItemsTheSame(oldItem: DataCategoryItem, newItem: DataCategoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataCategoryItem, newItem: DataCategoryItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    class ViewHolder (val binding : LayoutDropdownBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: DataCategoryItem, isSelected: Boolean){
            binding.apply {
                textview.text = item.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutDropdownBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() : Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.setItem(item, position == selectedItemIndex)
        holder.binding.cardText.setOnClickListener {
            onClick?.invoke(item)
        }
    }
    fun setData(data: MutableList<DataCategoryItem>) {
        differ.submitList(data)
    }

}