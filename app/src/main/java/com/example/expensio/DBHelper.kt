package com.example.expensio

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_NAME = "Expensio"
        private val TABLE_NAME = "Transactions"
        private val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTbl = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT,
            amount REAL,
            type TEXT,
            category TEXT,
            date TEXT,
            description TEXT)
        """.trimIndent()

        db?.execSQL(createTbl)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTransaction(
        title: String,
        amount: String,
        desc: String,
        type: String,
        cate: String,
        date: String
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("amount", amount)
            put("type", type)
            put("description", desc)
            put("date", date)
            put("category", cate)
        }

        val result = db.insert(TABLE_NAME, null, values)

        return result != -1L;

    }

    fun getAllTransactions(): List<Expensio> {
        val db = readableDatabase
        val transactions = mutableListOf<Expensio>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY id DESC", null)

        while (cursor.moveToNext()) {
            val transaction = Expensio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                date = cursor.getString(cursor.getColumnIndexOrThrow("date")),
                type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount")),
                category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
            )
            transactions.add(transaction)
        }
        cursor.close()
        return transactions
    }

    fun getLastTransaction(): List<Expensio> {
        val db = readableDatabase
        val transactions = mutableListOf<Expensio>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY id DESC LIMIT 1", null)

        while (cursor.moveToNext()) {
            val transaction = Expensio(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                date = cursor.getString(cursor.getColumnIndexOrThrow("date")),
                type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount")),
                category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
            )
            transactions.add(transaction)
        }
        cursor.close()
        return transactions
    }

    fun calculateTotals(): Triple<Double, Double, Double> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT type,amount FROM $TABLE_NAME", null)

        var totalIncome = 0.0
        var totalExpense = 0.0

        while (cursor.moveToNext()) {
            val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
            val amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"))

            if (type == "Income") {
                totalIncome += amount
            } else if (type == "Expense") {
                totalExpense += amount
            }

        }

        cursor.close()

        val balance = totalIncome - totalExpense

        return Triple(totalIncome, totalExpense, balance)

    }

    fun deleteTransaction(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "id = ?", arrayOf(id.toString()))
    }

    fun getCategoryTotals(): Map<String, Double> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT category,SUM(amount) FROM $TABLE_NAME WHERE type='Expense' GROUP BY category",
            null
        )

        val result = mutableMapOf<String, Double>()
        while (cursor.moveToNext()) {
            val category = cursor.getString(0)
            val total = cursor.getInt(1)
            result[category] = total.toDouble()
        }
        cursor.close()
        return result

    }

}