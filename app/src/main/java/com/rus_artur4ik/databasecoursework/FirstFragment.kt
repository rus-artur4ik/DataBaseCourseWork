package com.rus_artur4ik.databasecoursework

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rus_artur4ik.databasecoursework.MainActivity.Companion.LOG_TAG
import com.rus_artur4ik.databasecoursework.MainActivity.DBHelper
import com.rus_artur4ik.databasecoursework.TargetItem.*
import com.rus_artur4ik.databasecoursework.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val dbHelper by lazy { DBHelper(this.context); }

    val adapter by lazy { ItemsAdapter() }


//    private val items = listOf<TargetItem>(
//        TargetItem(1, "Купить телевизор", listOf(true to "Заработать 50000 рублей или долларов", false to "Сходить в магазин", false to "PROFIT")),
//        TargetItem(2, "Купить телевизор", listOf(true to "Заработать 50000", false to "Сходить в магазин", false to "PROFIT")),
//        TargetItem(3, "Купить телевизор", listOf(true to "Заработать 50000", false to "Сходить в магазин", false to "PROFIT")),
//        TargetItem(4, "Купить телевизор", listOf(true to "Заработать 50000", false to "Сходить в магазин", false to "PROFIT", true to "Заработать 50000", false to "Сходить в магазин")),
//        TargetItem(5, "Купить телефон", listOf(true to "Заработать 50000", false to "Сходить в магазин", false to "PROFIT")),
//    )

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemsRecycler.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.itemsRecycler.adapter = adapter

        binding.fab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        val items = getItemsFromDB()
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getItemsFromDB(): List<TargetItem> {
        val result = mutableListOf<TargetItem>()
        val checkboxes = mutableListOf<Checkbox>()
        val db = dbHelper.writableDatabase
        Log.d(LOG_TAG, "--- Rows in mytable: ---")

        val chCursor: Cursor = db.query("checkboxes", null, null, null, null, null, null)

        if (chCursor.moveToFirst()) {
            val idColIndex: Int = chCursor.getColumnIndex("id")
            val targetIdColIndex: Int = chCursor.getColumnIndex("target_id")
            val titleColIndex: Int = chCursor.getColumnIndex("title")
            val checkedColIndex: Int = chCursor.getColumnIndex("title")

            do {
                val checkbox = Checkbox(
                    chCursor.getInt(idColIndex),
                    chCursor.getInt(targetIdColIndex),
                    chCursor.getString(titleColIndex),
                    chCursor.getInt(checkedColIndex) != 0
                )
                checkboxes.add(checkbox)
                Log.d(
                    LOG_TAG,
                    checkbox.toString()
                )

            } while (chCursor.moveToNext())
        } else Log.d(LOG_TAG, "0 rows")
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
                    LOG_TAG,
                    item.toString()
                )

            } while (mainCursor.moveToNext())
        } else Log.d(LOG_TAG, "0 rows")
        mainCursor.close()

        return result
    }
}