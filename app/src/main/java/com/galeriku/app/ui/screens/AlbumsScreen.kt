package com.galeriku.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.galeriku.app.R
import com.galeriku.app.model.Album

/**
 * Tab Album: grid cover album (dikelompokkan dari folder/bucket penyimpanan),
 * mirip tab Albums pada galeri foto iPhone terbaru.
 */
@Composable
fun AlbumsScreen(
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (albums.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_albums),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )
            return@Box
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(albums, key = { it.bucketId }) { album ->
                AlbumCard(album = album, onClick = { onAlbumClick(album) })
            }
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}

@Composable
private fun AlbumCard(album: Album, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        AsyncImage(
            model = album.coverUri,
            contentDescription = album.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = album.name,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1
        )
        Text(
            text = "${album.itemCount} foto",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
