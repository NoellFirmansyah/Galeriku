package com.galeriku.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.galeriku.app.R
import com.galeriku.app.model.Photo
import com.galeriku.app.ui.components.GaleriSearchField
import com.galeriku.app.ui.components.PhotoGridItem
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Tab Search: kolom pencarian besar + hasil grid foto, mirip tab Search Photos.
 * Pencarian dilakukan berdasarkan nama file dan nama bulan/tahun pengambilan.
 */
@Composable
fun SearchScreen(
    photos: List<Photo>,
    onPhotoClick: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val monthFormatter = remember { SimpleDateFormat("MMMM yyyy", Locale("id", "ID")) }

    val results = remember(query, photos) {
        if (query.isBlank()) {
            emptyList()
        } else {
            val lower = query.trim().lowercase(Locale.getDefault())
            photos.filter { photo ->
                photo.name.lowercase(Locale.getDefault()).contains(lower) ||
                    photo.bucketName.lowercase(Locale.getDefault()).contains(lower) ||
                    monthFormatter.format(photo.dateTakenMillis).lowercase(Locale.getDefault()).contains(lower)
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.tab_search),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        GaleriSearchField(
            query = query,
            onQueryChange = { query = it },
            placeholder = stringResource(R.string.search_hint),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 12.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                query.isBlank() -> {
                    Text(
                        text = "Ketik untuk mencari foto berdasarkan nama file, album, atau bulan.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(32.dp)
                    )
                }

                results.isEmpty() -> {
                    Text(
                        text = "Tidak ada hasil untuk \"$query\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(32.dp)
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(results, key = { it.id }) { photo ->
                            PhotoGridItem(photo = photo, onClick = { onPhotoClick(photo) })
                        }
                    }
                }
            }
        }
    }
}
