package com.example.chatapp

import android.net.Uri

data class User(val uid: String, val userName:String, val profileImageUrl: String){
constructor() : this ("" , "", "")

}
