package com.example.demoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.demoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var email = "maharshielectronics@gmail.com"
    private var pass = "123456789"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener(){
            val inputEmail = binding.etEmail.text.toString()
            val inputPass = binding.etPassword.text.toString()

            if(inputEmail.isEmpty() || inputPass.isEmpty()){
                Toast.makeText(this, "Please enter details correctly", Toast.LENGTH_SHORT).show()
            }
            else{
                if(binding.etEmail.text.toString().equals(email)
                    && binding.etPassword.text.toString().equals(pass)){

                    Toast.makeText(this, "login successful!!", Toast.LENGTH_SHORT).show()

                    // home page activity

                    val Intent = Intent(this, Home_page::class.java)
                    startActivity(Intent)
                }
                else{
                    Toast.makeText(this, "wrong details", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}