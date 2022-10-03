package com.rus_artur4ik.coursework.db

import android.content.Context
import com.rus_artur4ik.coursework.TargetItem

class DatabaseHelper {
    companion object {
        fun getDatabase(context: Context): Database {
            return SqliteDatabase().initDatabase(context)
        }
    }
}

interface Database{

    fun initDatabase(context: Context): Database

    fun getItemsFromDB(): List<TargetItem>

    fun changeCheckboxState(id: Int, isChecked: Boolean)

    fun changeProgressbarState(id: Int, progress: Int)

    /**
     *     returns true if successful
     */
    fun removeItemFromDb(itemId: Int): Boolean

    fun addNoteToDB(title: String, checkboxes: List<String>, progressbars: List<TargetItem.ProgressBar>)
}