package com.example.idecabe2.utils

object FirestoreTable {
    val PROJECT = "PROJECTS"
    val USERS = "USERS"
}

object FireStoreDocumentField{
    val DATE = "date"
    val USER_ID = "user_id"
}

object sharedPreferences {
    val LOCAL_SHARED_PREF = "local_shared_pref"
    val USER_SESSION = "user_session"
}

object FireDatabase {
    val IDECABE22 = "idecabe"
}

object FirebaseStorageConstants {
    val ROOT_DIRECTORY = "app"
    val PROJECT_ICON = "project_icon"
    val PROJECT_PHOTO = "project_photo"
}


enum class HomeTabs(val index: Int, val key: String){
    PROJECTS(0, "PROJECTS")
}



