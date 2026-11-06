package com.galeriku.app.model

import android.net.Uri

data class Photo(
    val id: Long,
    val uri: Uri,
    val name: String,
    val dateTakenMillis: Long,
    val bucketId: Long,
    val bucketName: String,
    val width: Int,
    val height: Int
)

data class PhotoSection(
    val title: String,
    val photos: List<Photo>
)

data class Album(
    val bucketId: Long,
    val name: String,
    val coverUri: Uri,
    val itemCount: Int
)
