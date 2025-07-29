package com.userpilot.segment.sample.base.adatper

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.userpilot.segment.sample.databinding.ItemPropertyBinding
import com.userpilot.segment.sample.extensions.getKeyValueByIndex
import com.userpilot.segment.sample.features.identify.Properties

/**
 * Created by Motasem Hamed
 * on 12 Sep, 2024
 */

class PropertiesAdapter(
    private var properties: Properties,
    private val propertiesAdapterInterface: PropertiesAdapterInterface,
) : RecyclerView.Adapter<PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        return PropertyViewHolder(
            binding = ItemPropertyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            propertiesAdapterInterface
        )
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties.getKeyValueByIndex(position)
        holder.bind(property?.first ?: "", property?.second.toString())
    }

    override fun getItemCount(): Int {
        return properties.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onNewProperty(title: String, value: String) {
        properties[title] = value
        notifyDataSetChanged()
    }

    fun getProperties(): Properties {
        return properties
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteProperty(position: Int) {
        properties.getKeyValueByIndex(position)?.let {
            properties.remove(it.first)
            notifyDataSetChanged()
        }
    }
}

interface PropertiesAdapterInterface {
    fun onEditProperty(position: Int)
    fun onDeleteProperty(position: Int)
}
