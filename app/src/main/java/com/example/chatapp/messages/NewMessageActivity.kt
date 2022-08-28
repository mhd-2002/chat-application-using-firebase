package com.example.chatapp.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.models.User
import com.example.chatapp.databinding.ActivityNewMessageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_row_new_message.view.*

@Suppress("CAST_NEVER_SUCCEEDS")
class NewMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Select User"

        fetchUsers()
    }

    companion object{
       const val USER_KEY = "user_key"
    }

    private fun fetchUsers() {

        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter = GroupAdapter<GroupieViewHolder>()

                snapshot.children.forEach {
                    Log.d("user", it.toString())
                    val user = it.getValue(User::class.java)
                    user?.let { it1 -> UserItem(it1) }?.let { it2 -> adapter.add(it2) }
                }

                adapter.setOnItemClickListener { item, view ->

                    val user = item as User

                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY ,user)
                    startActivity(intent)
                    finish()
                }

                binding.recyclerViewNewMessage.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}

class UserItem(private val user: User) : Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_id.text = user.userName

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.iv_profile)
    }

}