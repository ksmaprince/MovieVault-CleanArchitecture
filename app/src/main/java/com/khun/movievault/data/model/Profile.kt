package com.khun.movievault.data.model

data class Profile(
    val profileId: Long = 0,
    val fullName: String = "",
    val contactNo: String = "",
    //Default Profile Image Holder
    val imageUrl: String? = null
)
