package com.rus_artur4ik.coursework

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class NewProgressbarField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val view: View = inflate(context, R.layout.view_new_progressbar_field, this)
    private val title: TextView by lazy { view.findViewById(R.id.title) }
    private val progress: TextView by lazy { view.findViewById(R.id.currentProgress) }
    private val maxProgress: TextView by lazy { view.findViewById(R.id.maxProgress) }

    fun getTitle(): String = title.text.toString()
    fun getProgress(): Int? = progress.text.toString().toIntOrNull()
    fun getMaxProgress(): Int? = maxProgress.text.toString().toIntOrNull()
}