package com.userpilot.segment.sample.base.adatper

import androidx.recyclerview.widget.RecyclerView
import com.userpilot.segment.sample.databinding.ItemPropertyBinding

/**
 * Created by Motasem Hamed
 * on 12 Sep, 2024
 */

class PropertyViewHolder(
    private val binding: ItemPropertyBinding,
    private val propertiesAdapterInterface: PropertiesAdapterInterface,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.ivEdit.setOnClickListener {
            propertiesAdapterInterface.onEditProperty(bindingAdapterPosition)
        }
        binding.ivDelete.setOnClickListener {
            propertiesAdapterInterface.onDeleteProperty(bindingAdapterPosition)
        }
    }

    fun bind(title: String, value: String) {
        itemView.apply {
            binding.tvTitle.text = title
            binding.tvValue.text = value
        }
    }
}
