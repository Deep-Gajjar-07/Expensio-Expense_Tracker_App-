package com.example.expensio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val bottom_menu = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottom_menu.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.bottom_add -> {
                    replaceFragment(Add_Expense_Icome_Page())
                    true
                }


                R.id.bottom_home -> {
                    replaceFragment(HomePage())
                    true
                }

                R.id.bottom_list -> {
                    replaceFragment(All_Transaction_List())
                    true
                }

                else -> false

            }
        }
        replaceFragment(HomePage())

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

}
