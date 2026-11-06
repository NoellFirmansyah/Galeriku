package com.galeriku.app.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.galeriku.app.model.Album
import com.galeriku.app.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Mengambil daftar foto langsung dari MediaStore, kompatibel dari Android 9 (API 28)
 * ke atas. Di API 28-32 kita pakai model penyimpanan lama (READ_EXTERNAL_STORAGE),
 * jadi query MediaStore standar ini sudah cukup tanpa perlu Scoped Storage khusus.
 */
class MediaStoreRepository(private val context: Context) {

    suspend fun loadPhotos(): List<Photo> = withContext(Dispatchers.IO) {
        val photos = mutableListOf<Photo>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        context.contentResolver.query(collection, projection, null, null, sortOrder)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateTakenCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val dateAddedCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val bucketIdCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val widthCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val dateTakenRaw = cursor.getLong(dateTakenCol)
                val dateAddedRaw = cursor.getLong(dateAddedCol) * 1000L
                val dateTaken = if (dateTakenRaw > 0) dateTakenRaw else dateAddedRaw
                val uri = ContentUris.withAppendedId(collection, id)

                photos += Photo(
                    id = id,
                    uri = uri,
                    name = cursor.getString(nameCol) ?: "",
                    dateTakenMillis = dateTaken,
                    bucketId = cursor.getLong(bucketIdCol),
                    bucketName = cursor.getString(bucketNameCol) ?: "Lainnya",
                    width = cursor.getInt(widthCol),
                    height = cursor.getInt(heightCol)
                )
            }
        }
        photos
    }

    suspend fun loadAlbums(photos: List<Photo>): List<Album> = withContext(Dispatchers.Default) {
        photos.groupBy { it.bucketId }
            .map { (bucketId, items) ->
                val sorted = items.sortedByDescending { it.dateTakenMillis }
                Album(
                    bucketId = bucketId,
                    name = sorted.first().bucketName,
                    coverUri = sorted.first().uri,
                    itemCount = sorted.size
                )
            }
            .sortedByDescending { it.itemCount }
    }
}
