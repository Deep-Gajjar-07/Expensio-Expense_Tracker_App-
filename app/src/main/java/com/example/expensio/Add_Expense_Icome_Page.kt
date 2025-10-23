package com.example.expensio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Add_Expense_Icome_Page : Fragment() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var btnCancel: AppCompatButton
    private lateinit var btnAdd: AppCompatButton
    private lateinit var amount: EditText
    private lateinit var title: EditText
    private lateinit var desc: EditText

    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add__expense__icome__page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroup = view.findViewById(R.id.radioGroupType)

        btnAdd = view.findViewById(R.id.btnAddTransaction)
        btnCancel = view.findViewById(R.id.btnCancelTransaction)
        amount = view.findViewById(R.id.editAmount)
        title = view.findViewById(R.id.editTitle)
        desc = view.findViewById(R.id.editDesc)

        val spinner = view.findViewById<Spinner>(R.id.SpnCategory)

        dbHelper = DBHelper(requireContext())

        val categories = listOf(
            "Food & Drinks",
            "Entertainment",
            "Groceries",
            "Transport",
            "Salary",
            "Other"
        )

        val SpnAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        SpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = SpnAdapter

        btnCancel.setOnClickListener {
            clearRecords()
            Toast.makeText(requireContext(), "canceled!", Toast.LENGTH_SHORT).show()
        }

        btnAdd.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val selectedRadio = view.findViewById<RadioButton>(selectedId)
            val type = selectedRadio.text.toString()

            if (title.text.isEmpty()) {
                title.error = "Please Enter Some Title!"
            } else if (amount.text.isEmpty() || amount.text.equals(0)) {
                amount.error = "Please Enter Valid Amount!"
            } else if (desc.text.isEmpty()) {
                desc.setText("Description Not Added")
            } else {

                val EditTitle = title.text.toString().trim()
                val EditAmount = amount.text.toString().trim()
                val EditDesc = desc.text.toString().trim()
                val date =
                    LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")).toString()
                val category = spinner.selectedItem.toString()

                val isInserted =
                    dbHelper.addTransaction(EditTitle, EditAmount, EditDesc, type, category,date)

                if (isInserted) {
                    Toast.makeText(requireContext(), "Transaction Added!", Toast.LENGTH_SHORT)
                        .show()
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.frame_container, HomePage())?.commit()
                    clearRecords()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error During Inserting Transaction! \nTry Again...",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

    }

    private fun clearRecords() {
        title.text.clear()
        desc.text.clear()
        amount.text.clear()
        radioGroup.clearCheck()
    }

}