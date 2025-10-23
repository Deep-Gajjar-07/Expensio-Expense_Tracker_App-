package com.example.expensio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class All_Transaction_List : Fragment() {

    private lateinit var adapter : TransactionAdpatar
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all__transaction__list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allList = view.findViewById<RecyclerView>(R.id.allTransactions)
        dbHelper = DBHelper(requireContext())

        adapter = TransactionAdpatar(requireContext(), dbHelper.getAllTransactions())
        allList.adapter = adapter

        allList.layoutManager = LinearLayoutManager(requireContext())
        allList.setHasFixedSize(true)

    }

    override fun onResume() {
        super.onResume()
        adapter.refreshTransaction(dbHelper.getAllTransactions())
    }

}