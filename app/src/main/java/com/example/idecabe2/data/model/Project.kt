package com.example.idecabe2.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*
@Parcelize
data class Project(
    var id: String = "",
    var user_id: String = "",
    var name_project:String = "",
    var label : String = "",
    var mask: String = "",
    @ServerTimestamp
    var date: Date = Date()
) : Parcelable
