package com.galeriku.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.galeriku.app.R
import com.galeriku.app.model.Photo
import com.galeriku.app.model.PhotoSection
import com.galeriku.app.ui.components.PhotoGridItem
import com.galeriku.app.ui.components.SectionHeader

/**
 * Tab Library: grid foto dikelompokkan per bulan-tahun, terbaru di atas.
 * Terinspirasi tampilan tab Library pada galeri foto iPhone terbaru.
 */
@Composable
fun LibraryScreen(
    sections: List<PhotoSection>,
    isLoading: Boolean,
    onPhotoClick: (Photo) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int = 3
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            sections.isEmpty() -> {
                Text(
                    text = androidx.compose.ui.res.stringResource(R.string.empty_library),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    sections.forEach { section ->
                        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(columns) }) {
                            SectionHeader(title = section.title)
                        }
                        items(section.photos, key = { it.id }) { photo ->
                            PhotoGridItem(
                                photo = photo,
                                onClick = { onPhotoClick(photo) }
                            )
                        }
                    }
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(columns) }) {
                        androidx.compose.foundation.layout.Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                        )
                    }
                }
            }
        }
    }
}
