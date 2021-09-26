package com.rus_artur4ik.databasecoursework

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rus_artur4ik.databasecoursework.MainActivity.Companion.LOG_TAG
import com.rus_artur4ik.databasecoursework.databinding.FragmentSecondBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NewNoteFragment : Fragment() {

    val dbHelper by lazy { MainActivity.DBHelper(this.context); }

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cbContainer = view.findViewById<LinearLayout>(R.id.cb_container)

        binding.addTarget.setOnClickListener {
            cbContainer.addView(
                EditText(requireContext()).apply {
                    val lp = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    gravity = Gravity.CENTER
                    layoutParams = lp

                    hint = "Цель ${cbContainer.childCount + 1}"
                }
            )
        }

        binding.save.setOnClickListener {
            val title = binding.title.text.toString()
            val checkboxes = cbContainer.children.map {
                (it as EditText).text.toString()
            }.toList()

            addToDB(title, checkboxes)
            Snackbar.make(view, "Успешно добавлено", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addToDB(title: String, checkboxes: List<String>) {
        val db = dbHelper.writableDatabase
        val cv1 = ContentValues()
        Log.d(LOG_TAG, "--- Insert in table main: ---")

        // подготовим данные для вставки в виде пар: наименование столбца - значение
        cv1.put("title", title)
        // вставляем запись и получаем ее ID
        val rowID: Long = db.insert("main", null, cv1)
        Log.d(LOG_TAG, "row inserted to main, ID = $rowID")


        Log.d(LOG_TAG, "--- Insert in table checkboxes: ---")

        checkboxes.forEach {
            val cv2 = ContentValues()
            // подготовим данные для вставки в виде пар: наименование столбца - значение
            cv2.put("target_id", rowID)
            cv2.put("title", it)
            cv2.put("checked", "0")
            // вставляем запись и получаем ее ID
            val cbRowID: Long = db.insert("checkboxes", null, cv2)
            Log.d(LOG_TAG, "row inserted to checkboxes, ID = $cbRowID")
        }

    }
}