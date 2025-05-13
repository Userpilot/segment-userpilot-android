package com.userpilot.segment.sample.features.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.userpilot.segment.sample.databinding.ItemOptionBinding
import com.userpilot.segment.sample.features.main.models.Content

/**
 * Created by Motasem Hamed
 * on 01 Sep, 2024
 */

class OptionsAdapter(
    private var contents: List<Content>,
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<OptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        return OptionViewHolder(
            binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClickListener = onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(contents[position])
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    fun getItems(): List<Content> {
        return contents
    }
}
