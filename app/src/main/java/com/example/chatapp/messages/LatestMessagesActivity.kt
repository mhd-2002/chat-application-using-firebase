package com.example.chatapp.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.chatapp.R
import com.example.chatapp.registerClasses.SignUp
import com.example.chatapp.databinding.ActivityLatestMessagesBinding
import com.google.firebase.auth.FirebaseAuth

class LatestMessagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLatestMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatestMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAuth()

    }

    private fun checkAuth() {

        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {

            goToActivity(SignUp::class.java , true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_message -> {

                goToActivity(NewMessageActivity::class.java , false)
            }

            R.id.menu_sign_out -> {

                FirebaseAuth.getInstance().signOut()
                goToActivity(NewMessageActivity::class.java , true)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun <T> goToActivity(java: Class<T>, flag: Boolean) {

        val intent = Intent(this, java)
        if (flag) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}