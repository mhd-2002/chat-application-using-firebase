package com.example.chatapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@Suppress("UNREACHABLE_CODE")
class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
         val pd = ProgressDialog(this)

        binding.btProfile.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }

        binding.btRegister.setOnClickListener {

            pd.setTitle("Loading...")
            pd.setMessage("please wait!")
            pd.create()
            pd.show()

            register()

        }

        binding.tvHaveAccount.setOnClickListener {

            startActivity(Intent(this, LogIn::class.java))
        }
    }

    private fun message(message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(this, message, duration).show()
    }

    private fun register() {

        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            message("please enter required fields")
        } else {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        message("successfully registered")

                        uploadImageToFirebaseStorage()
                        return@addOnCompleteListener
                    } else {
                        message(task.exception.toString())
                    }

                }.addOnFailureListener {
                    message("Failed due to ${it.message}")

                }
        }
    }

    private fun uploadImageToFirebaseStorage() {

        if (selectedUrl == null) return

        val fileName = UUID.randomUUID().toString()
        try {

            val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
            ref.putFile(selectedUrl!!)
                .addOnSuccessListener {
                    message("successfully uploaded image!!")

                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

                    ProgressDialog(this).dismiss()

                    startActivity(intent)

                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToDatabase(it.toString())

                    }

                }.addOnFailureListener {
                    message("Failed due to ${it.message}")

                }

        } catch (e: Exception) {
            message(e.message.toString())
        }


    }

    private fun saveUserToDatabase(uri: String) {
        val uid = FirebaseAuth.getInstance().uid

        val userName = binding.etName.text.toString()

        val user = uid?.let { User(it, userName, uri) }

        FirebaseDatabase.getInstance().getReference("/users/$uid").setValue(user)
            .addOnSuccessListener {
                message("finally we saved everything")

            }.addOnFailureListener {
                message(it.message.toString())
            }


    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onActivityResult(requestCode, resultCode, data)",
            "androidx.appcompat.app.AppCompatActivity"
        )
    )
    var selectedUrl: Uri? = null

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedUrl = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedUrl)


                binding.profileImage.setImageBitmap(bitmap)
                binding.btProfile.visibility = View.INVISIBLE


            } catch (e: Exception) {
                message(e.message.toString())
            }

        }

    }

}