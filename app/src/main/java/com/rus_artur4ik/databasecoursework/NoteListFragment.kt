package com.rus_artur4ik.databasecoursework

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
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
class NoteListFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    private val dbHelper by lazy { DatabaseHelper(DBHelper(this.context)) }

    private val onItemLongClick: (Int, View) -> Unit = { id, _ ->
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление карточки")
            .setMessage("Вы действительно хотите удалить эту карточку?")
            .setPositiveButton("Да") { dialog, _ ->
                dbHelper.removeItemFromDb(id)
                notifyDataChanged()
                dialog.dismiss()
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    val adapter by lazy { ItemsAdapter(
        onCardLongClick = onItemLongClick
    ) }


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

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        notifyDataChanged()
    }

    private fun notifyDataChanged() {
        val items = dbHelper.getItemsFromDB()
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}