package com.rus_artur4ik.coursework

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rus_artur4ik.coursework.databinding.FragmentFirstBinding
import com.rus_artur4ik.coursework.db.DatabaseHelper

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoteListFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    private val dbHelper by lazy { DatabaseHelper.getDatabase(requireContext()) }

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

    private val onFieldChecked: (Int, Boolean) -> Unit = { id, checked ->
        dbHelper.changeCheckboxState(id, checked)
    }

    private val onProgressBarClicked: (Int) -> Unit = { id ->
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_set_progressbar, null)
        AlertDialog.Builder(requireContext())
            .setTitle("Изменить значение прогресса")
            .setView(v)
            .setPositiveButton("Да") { dialog, _ ->
                val progress = v.findViewById<EditText>(R.id.newProgress).text.toString().toIntOrNull()
                progress?.let {
                    dbHelper.changeProgressbarState(id, it)
                    notifyDataChanged()
                    dialog.dismiss()
                } ?: Toast.makeText(context, "Неверные введенные данные", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private val adapter by lazy { ItemsAdapter(
        onCardLongClick = onItemLongClick,
        onCheckedChangeListener = onFieldChecked,
        onProgressBarClickListener = onProgressBarClicked
    ) }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun notifyDataChanged() {
        val items = dbHelper.getItemsFromDB()
        adapter.items = items
        binding.emptyText.isVisible = items.isEmpty()
        binding.emptyArrow.isVisible = items.isEmpty()
        adapter.notifyDataSetChanged()
    }
}