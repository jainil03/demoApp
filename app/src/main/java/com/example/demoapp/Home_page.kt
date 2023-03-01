package com.example.demoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.demoapp.databinding.ActivityHomePageBinding
import com.example.demoapp.databinding.ActivityMainBinding
import com.example.demoapp.databinding.FragmentHomeBinding

class Home_page : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        replaceFragment(Home())

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val emailData = intent.getStringExtra("email")

        binding.bottomNavigationView.setOnItemSelectedListener{

            when(it.itemId){
                R.id.home -> replaceFragment(Home())
                R.id.Settings -> replaceFragment(Settings())

                else ->{

                }

            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}