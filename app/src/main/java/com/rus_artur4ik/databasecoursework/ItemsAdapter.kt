package com.rus_artur4ik.databasecoursework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(
    var items: List<TargetItem> = listOf(),
    private val onCardLongClick: (Int, View) -> Unit
): RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleTextView = itemView.findViewById<TextView>(R.id.title)
        val checkboxesList = itemView.findViewById<LinearLayout>(R.id.checkboxesList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.setOnLongClickListener{
            onCardLongClick(items[position].id, it)
            true
        }

        holder.titleTextView.text = items[position].title

        holder.checkboxesList.removeAllViews()
        items[position].subtargets.forEach {
            val v = createCheckboxView(holder.checkboxesList, it.title, it.isChecked)
            holder.checkboxesList.addView(v)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    private fun createCheckboxView(parent: ViewGroup, text: String, isChecked: Boolean): View {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkbox, parent, false)
            .apply {
                val v = findViewById<CheckBox>(R.id.checkBox)
                v.text = text
                v.isChecked = isChecked
            }
    }
}