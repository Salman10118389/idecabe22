package com.example.idecabe2.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(
    var user_id: String = "",
    val full_name: String = "",
    val email: String = "",
    @ServerTimestamp
    val date: Date = Date()
) : Parcelable
