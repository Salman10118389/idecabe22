package com.example.idecabe2.data.model

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*
@Parcelize
@Keep
data class Project(
    var id: String = "",
    var user_id: String = "",
    var name_project:String = "",
    var label : String = "",
    var mask: String = "",
    var tags: List<String> = arrayListOf(),
    var images: List<String> = arrayListOf(),
    @ServerTimestamp
    var date: Date = Date()
) : Parcelable
