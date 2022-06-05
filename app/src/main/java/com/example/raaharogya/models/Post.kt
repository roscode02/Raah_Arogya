package com.example.raaharogya.models

import android.net.Uri

data class Post(
    val text: String = "",
    val createdBy: User = User(),
    val createdAt: Long = 0L,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String = "",
    val status: Int = 0,
    val likedBy: ArrayList<String> = ArrayList())