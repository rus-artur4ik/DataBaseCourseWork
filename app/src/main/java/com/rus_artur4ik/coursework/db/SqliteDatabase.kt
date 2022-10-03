package com.rus_artur4ik.coursework.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.rus_artur4ik.coursework.MainActivity
import com.rus_artur4ik.coursework.TargetItem

class SqliteDatabase: Database {

    private lateinit var dbHelper: DBHelper

    override fun initDatabase(context: Context): Database {
        dbHelper = DBHelper(context)
        return this
    }

    override fun getItemsFromDB(): List<TargetItem> {
        val db = dbHelper.writableDatabase

        val subtargets = mutableListOf<TargetItem.Subtarget>()
        subtargets.addAll(getCheckboxes(db))
        subtargets.addAll(getProgressBars(db))

        return getNotes(db, subtargets)
    }

    override fun changeCheckboxState(id: Int, isChecked: Boolean) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues()
        cv.put("checked", if (isChecked) 1 else 0)
        db.update("checkboxes", cv,"id=$id", null)
    }

    override fun changeProgressbarState(id: Int, progress: Int) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues()
        cv.put("progress", progress)
        db.update("progressbars", cv,"id=$id", null)
    }

    /**
     *     returns true if successful
     */
    override fun removeItemFromDb(itemId: Int): Boolean {
        val db = dbHelper.writableDatabase
        db.delete("main", "id=$itemId", null)
        db.delete("checkboxes", "target_id=$itemId", null)
        db.delete("progressbars", "target_id=$itemId", null)
        return true
    }

    override fun addNoteToDB(title: String, checkboxes: List<String>, progressbars: List<TargetItem.ProgressBar>) {
        val db = dbHelper.writableDatabase
        val cv1 = ContentValues()

        cv1.put("title", title)
        val rowID: Long = db.insert("main", null, cv1)

        checkboxes.forEach {
            val cv2 = ContentValues()
            cv2.put("target_id", rowID)
            cv2.put("title", it)
            cv2.put("checked", "0")
            db.insert("checkboxes", null, cv2)
        }

        progressbars.forEach {
            val cv2 = ContentValues()
            cv2.put("target_id", rowID)
            cv2.put("title", it.title)
            cv2.put("progress", it.progress)
            cv2.put("max_progress", it.maxProgress)
            db.insert("progressbars", null, cv2)
        }
    }

    private fun getCheckboxes(db: SQLiteDatabase): List<TargetItem.Checkbox> {
        val chCursor: Cursor = db.query("checkboxes", null, null, null, null, null, null)
        val checkboxes = mutableListOf<TargetItem.Checkbox>()

        if (chCursor.moveToFirst()) {
            val idColIndex: Int = chCursor.getColumnIndex("id")
            val targetIdColIndex: Int = chCursor.getColumnIndex("target_id")
            val titleColIndex: Int = chCursor.getColumnIndex("title")
            val checkedColIndex: Int = chCursor.getColumnIndex("checked")

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
        return checkboxes
    }

    private fun getProgressBars(db: SQLiteDatabase): List<TargetItem.ProgressBar> {
        val chCursor: Cursor = db.query("progressbars", null, null, null, null, null, null)
        val items = mutableListOf<TargetItem.ProgressBar>()

        if (chCursor.moveToFirst()) {
            val idColIndex: Int = chCursor.getColumnIndex("id")
            val targetIdColIndex: Int = chCursor.getColumnIndex("target_id")
            val titleColIndex: Int = chCursor.getColumnIndex("title")
            val maxProgressColIndex: Int = chCursor.getColumnIndex("max_progress")
            val progressColIndex: Int = chCursor.getColumnIndex("progress")

            do {
                val progressbar = TargetItem.ProgressBar(
                    chCursor.getInt(idColIndex),
                    chCursor.getInt(targetIdColIndex),
                    chCursor.getString(titleColIndex),
                    chCursor.getInt(maxProgressColIndex),
                    chCursor.getInt(progressColIndex),
                )
                items.add(progressbar)
                Log.d(
                    MainActivity.LOG_TAG,
                    progressbar.toString()
                )

            } while (chCursor.moveToNext())
        } else Log.d(MainActivity.LOG_TAG, "0 rows")
        chCursor.close()
        return items
    }

    private fun getNotes(db: SQLiteDatabase, subtargets: List<TargetItem.Subtarget>): List<TargetItem>{
        val mainCursor: Cursor = db.query("main", null, null, null, null, null, null)
        val result = mutableListOf<TargetItem>()

        if (mainCursor.moveToFirst()) {
            val idColIndex: Int = mainCursor.getColumnIndex("id")
            val titleColIndex: Int = mainCursor.getColumnIndex("title")
            do {
                val id = mainCursor.getInt(idColIndex)
                val title = mainCursor.getString(titleColIndex).toString()
                val subs = subtargets.filter { it.target_id == id }

                val item = TargetItem(id, title, subs)
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

    private class DBHelper(context: Context?)
        : SQLiteOpenHelper(context, "myDB", null, 1) {

        override fun onCreate(db: SQLiteDatabase) {
            // создаем таблицу с полями
            db.execSQL(
                "create table main ("
                        + "id integer primary key autoincrement,"
                        + "title text"
                        + ");"
            )

            db.execSQL(
                "create table checkboxes ("
                        + "id integer primary key autoincrement,"
                        + "target_id integer,"
                        + "title text,"
                        + "checked integer"
                        + ");"
            )

            db.execSQL(
                "create table progressbars ("
                        + "id integer primary key autoincrement,"
                        + "target_id integer,"
                        + "title text,"
                        + "max_progress integer,"
                        + "progress integer"
                        + ");"
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    }
}