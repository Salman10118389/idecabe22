package com.example.idecabe2.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class ProjectPhoto(
    var id: String = "",
    var user_id: String = "",
    var parent_user_id: String = ""
) : Parcelable