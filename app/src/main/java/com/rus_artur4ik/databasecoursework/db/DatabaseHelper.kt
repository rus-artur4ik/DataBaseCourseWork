package com.rus_artur4ik.databasecoursework.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.rus_artur4ik.databasecoursework.TargetItem

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