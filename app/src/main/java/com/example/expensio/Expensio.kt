package com.example.expensio

data class Expensio(
    val id : Int,
    val title : String,
    val type :String,
    val description : String,
    val amount : Int,
    val category: String,
    val date: String,
)
