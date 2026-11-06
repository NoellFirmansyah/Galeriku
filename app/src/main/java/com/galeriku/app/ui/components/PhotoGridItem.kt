package com.galeriku.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.galeriku.app.model.Photo
import androidx.compose.foundation.ExperimentalFoundationApi

/**
 * Satu ubin foto di grid Library/Album - persegi, rapat seperti grid Photos.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoGridItem(
    photo: Photo,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    AsyncImage(
        model = photo.uri,
        contentDescription = photo.name,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f)
            .clip(RectangleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    )
}
