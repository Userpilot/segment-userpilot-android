package com.userpilot.segment.sample.features.main.adapter

import androidx.recyclerview.widget.RecyclerView
import com.userpilot.segment.sample.databinding.ItemOptionBinding
import com.userpilot.segment.sample.features.main.models.Content

/**
 * Created by Motasem Hamed
 * on 01 Sep, 2024
 */

class OptionViewHolder(
    private val binding: ItemOptionBinding,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.clContent.setOnClickListener { onItemClickListener(bindingAdapterPosition) }
    }

    fun bind(content: Content) {
        itemView.apply {
            binding.tvOptionTitle.text = content.getTitle(itemView.context)
        }
    }
}
