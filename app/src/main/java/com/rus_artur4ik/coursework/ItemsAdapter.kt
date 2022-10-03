package com.rus_artur4ik.coursework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class ItemsAdapter(
    private val onCardLongClick: (Int, View) -> Unit,
    private val onCheckedChangeListener: (Int, Boolean) -> Unit,
    private val onProgressBarClickListener: (Int) -> Unit
): RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {
    var items: List<TargetItem> = listOf()

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
        items[position].subtargets.forEach { sub ->
            when (sub) {
                is TargetItem.Checkbox -> {
                    val v = createCheckboxView(holder.checkboxesList, sub.title, sub.isChecked)
                    v.setOnCheckedChangeListener { _, checked ->
                        onCheckedChangeListener(sub.id, checked)
                    }
                    holder.checkboxesList.addView(v)
                }
                is TargetItem.ProgressBar -> {
                    val v = createProgressbarView(holder.checkboxesList, sub.title, sub.maxProgress, sub.progress)
                    v.setOnClickListener {
                        onProgressBarClickListener(sub.id)
                    }
                    holder.checkboxesList.addView(v)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    private fun createCheckboxView(parent: ViewGroup, text: String, isChecked: Boolean): CheckBox {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkbox, parent, false)
            .apply {
                val v = findViewById<CheckBox>(R.id.checkBox)
                v.text = text
                v.isChecked = isChecked
            } as CheckBox
    }

    private fun createProgressbarView(parent: ViewGroup,
                                      title: String,
                                      maxProgress: Int,
                                      progress: Int): View {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progressbar, parent, false)
            .apply {
                findViewById<TextView>(R.id.title).text = title
                findViewById<TextView>(R.id.progress).text = "$progress/$maxProgress"
                findViewById<ProgressBar>(R.id.progressBar).progress =
                    getProgressPercent(maxProgress, progress)
            }
    }

    private fun getProgressPercent(maxProgress: Int, progress: Int): Int
        = ((progress.toFloat()/maxProgress)*100).toInt()
}