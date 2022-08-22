package com.example.chatapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {

    lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.backToRegister.setOnClickListener {

            finish()
        }

    }
    private fun message(message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(this, message, duration).show()
    }

    private fun logIn() {

        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            message("please enter required fields")
        } else {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        message("successfully LogIn")

                        val intent = Intent(this, LatestMessagesActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

                        startActivity(intent)
                        return@addOnCompleteListener
                    } else {
                        message(task.exception.toString())
                    }

                }.addOnFailureListener {
                    message("Failed due to ${it.message}")

                }
        }
    }
}