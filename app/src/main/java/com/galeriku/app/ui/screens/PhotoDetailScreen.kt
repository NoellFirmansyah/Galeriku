package com.galeriku.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.galeriku.app.model.Photo
import com.galeriku.app.ui.components.ZoomableAsyncImage
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.ExperimentalFoundationApi

/**
 * Viewer foto layar penuh: swipe antar foto, pinch zoom, dan toolbar atas/bawah
 * (kembali, favorit, bagikan, hapus) - mirip viewer foto galeri iPhone terbaru.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoDetailScreen(
    photos: List<Photo>,
    initialIndex: Int,
    favoriteIds: Set<Long>,
    onBack: () -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onDelete: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    if (photos.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = initialIndex.coerceIn(0, photos.lastIndex)
    ) { photos.size }

    var showDeleteConfirm by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("EEEE, d MMMM yyyy • HH:mm", Locale("id", "ID")) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val photo = photos[page]
            ZoomableAsyncImage(
                model = photo.uri,
                contentDescription = photo.name
            )
        }

        val currentPhoto = photos[pagerState.currentPage]
        val isFavorite = favoriteIds.contains(currentPhoto.id)

        // Toolbar atas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
            }
            Text(
                text = dateFormatter.format(currentPhoto.dateTakenMillis),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
            IconButton(onClick = { onToggleFavorite(currentPhoto.id) }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorit",
                    tint = if (isFavorite) MaterialTheme.colorScheme.error else Color.White
                )
            }
        }

        // Toolbar bawah
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { sharePhoto(context, currentPhoto) }) {
                Icon(Icons.Filled.Share, contentDescription = "Bagikan", tint = Color.White)
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(Icons.Filled.Delete, contentDescription = "Hapus", tint = Color.White)
            }
        }
    }

    if (showDeleteConfirm) {
        val currentPhoto = photos[pagerState.currentPage]
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Hapus foto ini?") },
            text = { Text("Foto akan dihapus secara permanen dari perangkat.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    onDelete(currentPhoto)
                }) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Batal") }
            }
        )
    }
}

private fun sharePhoto(context: android.content.Context, photo: Photo) {
    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(android.content.Intent.EXTRA_STREAM, photo.uri)
        addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(android.content.Intent.createChooser(intent, "Bagikan foto"))
}
