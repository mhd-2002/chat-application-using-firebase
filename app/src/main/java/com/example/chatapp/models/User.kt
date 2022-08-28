package com.example.chatapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String, val userName:String, val profileImageUrl: String):Parcelable{
constructor() : this ("" , "", "")

}
