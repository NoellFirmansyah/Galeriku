package com.galeriku.app.util

import com.galeriku.app.model.Photo
import com.galeriku.app.model.PhotoSection
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Kelompokkan foto per bulan-tahun, terbaru di atas - mirip tampilan tab Library.
 */
fun groupPhotosByMonth(photos: List<Photo>): List<PhotoSection> {
    val formatter = SimpleDateFormat("MMMM yyyy", Locale("id", "ID"))
    val calendar = Calendar.getInstance()

    return photos
        .groupBy { photo ->
            calendar.timeInMillis = photo.dateTakenMillis
            calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH)
        }
        .toSortedMap(compareByDescending { it })
        .map { (_, items) ->
            val sortedItems = items.sortedByDescending { it.dateTakenMillis }
            calendar.timeInMillis = sortedItems.first().dateTakenMillis
            val label = formatter.format(calendar.time)
                .replaceFirstChar { c -> c.uppercase() }
            PhotoSection(title = label, photos = sortedItems)
        }
}
