package com.example.expensio

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class HomePage : Fragment() {

    private lateinit var list: RecyclerView
    private lateinit var adapter: TransactionAdpatar
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = view.findViewById(R.id.transition_list)
        list.setHasFixedSize(true)

        val totalIncome = view.findViewById<TextView>(R.id.txtTotalIncome)
        val totalExpense = view.findViewById<TextView>(R.id.txtTotalExpense)
        val totalBalance = view.findViewById<TextView>(R.id.txtBalance)

        dbHelper = DBHelper(requireContext())

        val piechart = view.findViewById<PieChart>(R.id.pieChart)

        val categoryTotals = dbHelper.getCategoryTotals()
        val entries = ArrayList<PieEntry>()

        for ((category,total) in categoryTotals){
            entries.add(PieEntry(total.toFloat(),category))
        }

        val dataset = PieDataSet(entries, "")
        dataset.colors = listOf(Color.RED, Color.BLUE, Color.LTGRAY, Color.GRAY, Color.MAGENTA)
        dataset.valueTextColor = Color.WHITE
        dataset.valueTextSize = 16f
        dataset.selectionShift = 8f

        piechart.data = PieData(dataset)
        piechart.centerText = "Expense Graph"
        piechart.animateY(1000)
        piechart.invalidate()
        piechart.setDrawEntryLabels(false)
        piechart.setCenterTextColor(Color.WHITE)
        piechart.setCenterTextSize(16f)
        piechart.setHoleColor(Color.parseColor("#155446"))
        piechart.description.isEnabled = false

        piechart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if(e is PieEntry){
                    Toast.makeText(requireContext(),"${e.label} : ${e.value}",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected() {

            }

        })

        val legend = piechart.legend
        legend.textColor = Color.WHITE
        legend.textSize = 15f
        legend.setDrawInside(false)
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

        val (income, expense, balance) = dbHelper.calculateTotals()

        totalIncome.text = income.toString()
        totalExpense.text = expense.toString()
        totalBalance.text = balance.toString()

        adapter = TransactionAdpatar(requireContext(), dbHelper.getLastTransaction())

        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        adapter.refreshTransaction(dbHelper.getLastTransaction())
    }

}
