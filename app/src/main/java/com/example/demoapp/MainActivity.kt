package com.example.demoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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

        //val btnLogin =  findViewById<Button>(R.id.btnLogin)

        //val Intent = Intent(this, SecondActivity::class.java)
        //startActivity(Intent)

        var emailEntered = false
        val inputEmail = binding.etEmail.text.toString()
        binding.btnLogin.isEnabled = false

        binding.etEmail.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()){
                    emailEntered = true
                }
                else {
                    binding.etEmail.error = "Invalid Email"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.etPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.etPassword.text.toString().length >= 8 && emailEntered){
                    binding.btnLogin.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.btnLogin.setOnClickListener{

            if(binding.etEmail.text.toString() == email && binding.etPassword.text.toString() == pass){

                Toast.makeText(this, "login successful!!", Toast.LENGTH_SHORT).show()

                /* home page activity */

                val intent = Intent(this, Home_page::class.java)
                intent.putExtra("email", inputEmail)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "wrong details", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

