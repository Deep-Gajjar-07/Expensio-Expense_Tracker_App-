package com.example.expensio

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TransactionAdpatar(context: Context, private var TransactionList: List<Expensio>) :
    RecyclerView.Adapter<TransactionAdpatar.TViewHolder>() {

    val dbHelper = DBHelper(context)

    class TViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rvTitle: TextView = view.findViewById(R.id.txtTitle)
        val rvDescription: TextView = view.findViewById(R.id.txtDescription)
        val rvAmount: TextView = view.findViewById(R.id.txtAmount)
        val rvDate: TextView = view.findViewById(R.id.txtDate)
        val rvType: TextView = view.findViewById(R.id.txtType)
        val rvCategory: TextView = view.findViewById(R.id.txtCategory)
        val btnDelete: ImageView = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_list_layout, parent, false)
        return TViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return TransactionList.size
    }

    override fun onBindViewHolder(holder: TViewHolder, position: Int) {
        val i = TransactionList[position]
        holder.rvTitle.text = i.title
        holder.rvDescription.text = i.description
        holder.rvAmount.text = i.amount.toString()
        holder.rvDate.text = i.date
        holder.rvCategory.text = i.category
        holder.rvType.text = i.type

        holder.btnDelete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(holder.itemView.context)
            alertDialog.setTitle("Transaction Delete : ")
            alertDialog.setMessage("Are You Sure Want To Delete Transaction For : ${i.title} !")
            alertDialog.setIcon(R.drawable.icon_trash)

            alertDialog.setPositiveButton("Yes") { dialog, _ ->
                dbHelper.deleteTransaction(i.id)
                Toast.makeText(holder.itemView.context, "Deleted Successfully!", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
            }

            alertDialog.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            alertDialog.show()
        }
    }

    fun refreshTransaction(newTransaction: List<Expensio>) {
        TransactionList = newTransaction
        notifyDataSetChanged()
    }
}