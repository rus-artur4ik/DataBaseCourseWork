package com.rus_artur4ik.databasecoursework

import android.database.Cursor
import android.util.Log

class DatabaseHelper(private val dbHelper: MainActivity.DBHelper) {

    fun getItemsFromDB(): List<TargetItem> {
        val result = mutableListOf<TargetItem>()
        val checkboxes = mutableListOf<TargetItem.Checkbox>()
        val db = dbHelper.writableDatabase

        val chCursor: Cursor = db.query("checkboxes", null, null, null, null, null, null)

        if (chCursor.moveToFirst()) {
            val idColIndex: Int = chCursor.getColumnIndex("id")
            val targetIdColIndex: Int = chCursor.getColumnIndex("target_id")
            val titleColIndex: Int = chCursor.getColumnIndex("title")
            val checkedColIndex: Int = chCursor.getColumnIndex("title")

            do {
                val checkbox = TargetItem.Checkbox(
                    chCursor.getInt(idColIndex),
                    chCursor.getInt(targetIdColIndex),
                    chCursor.getString(titleColIndex),
                    chCursor.getInt(checkedColIndex) != 0
                )
                checkboxes.add(checkbox)
                Log.d(
                    MainActivity.LOG_TAG,
                    checkbox.toString()
                )

            } while (chCursor.moveToNext())
        } else Log.d(MainActivity.LOG_TAG, "0 rows")
        chCursor.close()

        val mainCursor: Cursor = db.query("main", null, null, null, null, null, null)

        if (mainCursor.moveToFirst()) {
            val idColIndex: Int = mainCursor.getColumnIndex("id")
            val titleColIndex: Int = mainCursor.getColumnIndex("title")
            do {
                val id = mainCursor.getInt(idColIndex)
                val title = mainCursor.getString(titleColIndex).toString()
                val cbs = checkboxes.filter { it.target_id == id }

                val item = TargetItem(id, title, cbs)
                result.add(item)
                Log.d(
                    MainActivity.LOG_TAG,
                    item.toString()
                )

            } while (mainCursor.moveToNext())
        } else Log.d(MainActivity.LOG_TAG, "0 rows")
        mainCursor.close()

        return result
    }

    /**
     *     returns true if successful
     */
    fun removeItemFromDb(itemId: Int): Boolean {
        val db = dbHelper.writableDatabase
        db.delete("main", "id=$itemId", null)
        db.delete("checkboxes", "target_id=$itemId", null)
        return true
    }
}