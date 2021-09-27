package com.rus_artur4ik.databasecoursework

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rus_artur4ik.databasecoursework.databinding.FragmentSecondBinding
import com.rus_artur4ik.databasecoursework.db.DatabaseHelper


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NewNoteFragment : Fragment() {

    private val dbHelper by lazy { DatabaseHelper.getDatabase(requireContext()) }

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

        binding.addProgress.setOnClickListener {
            cbContainer.addView(
                NewProgressbarField(requireContext()).apply {
                    val lp = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    gravity = Gravity.CENTER
                    layoutParams = lp
                }
            )
        }

        binding.save.setOnClickListener {
            val title = binding.title.text.toString()

            val checkboxes = mutableListOf<String>()
            val progressbars = mutableListOf<TargetItem.ProgressBar>()

            cbContainer.children.forEach { view ->
                when (view){
                    is EditText -> {
                        checkboxes.add(view.text.toString())
                    }
                    is NewProgressbarField -> {
                        progressbars.add(
                            TargetItem.ProgressBar(
                                0,
                                0,
                                view.getTitle(),
                                view.getMaxProgress()!!,
                                view.getProgress()!!
                            )
                        )
                    }
                }
            }

            dbHelper.addNoteToDB(title, checkboxes, progressbars)

            Snackbar.make(view, "Успешно добавлено", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}