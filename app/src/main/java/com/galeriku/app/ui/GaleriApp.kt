package com.galeriku.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.galeriku.app.model.Album
import com.galeriku.app.model.Photo
import com.galeriku.app.ui.components.BottomNavBar
import com.galeriku.app.ui.components.GaleriTab
import com.galeriku.app.ui.screens.AlbumsScreen
import com.galeriku.app.ui.screens.LibraryScreen
import com.galeriku.app.ui.screens.PermissionScreen
import com.galeriku.app.ui.screens.PhotoDetailScreen
import com.galeriku.app.ui.screens.SearchScreen
import com.galeriku.app.viewmodel.GalleryViewModel

/** Layar yang sedang tampil penuh di atas tab (viewer foto). */
private sealed class DetailTarget {
    data class FromPhotos(val photos: List<Photo>, val index: Int) : DetailTarget()
}

@Composable
fun GaleriAppRoot(
    viewModel: GalleryViewModel,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasPermission by viewModel.hasPermission.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sections by viewModel.sections.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val albums by viewModel.albums.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val deleteError by viewModel.deleteError.collectAsState()

    var selectedTab by rememberSaveable { mutableStateOf(GaleriTab.LIBRARY) }
    var detailTarget by remember { mutableStateOf<DetailTarget?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(deleteError) {
        deleteError?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearDeleteError()
        }
    }

    if (!hasPermission) {
        PermissionScreen(onRequestPermission = onRequestPermission, modifier = modifier.fillMaxSize())
        return
    }

    val currentDetail = detailTarget
    if (currentDetail is DetailTarget.FromPhotos) {
        PhotoDetailScreen(
            photos = currentDetail.photos,
            initialIndex = currentDetail.index,
            favoriteIds = favoriteIds,
            onBack = { detailTarget = null },
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            onDelete = { viewModel.deletePhoto(it) },
            modifier = modifier.fillMaxSize()
        )
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            BottomNavBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())) {
            when (selectedTab) {
                GaleriTab.LIBRARY -> LibraryScreen(
                    sections = sections,
                    isLoading = isLoading,
                    onPhotoClick = { photo ->
                        val index = photos.indexOfFirst { it.id == photo.id }
                        detailTarget = DetailTarget.FromPhotos(photos, index.coerceAtLeast(0))
                    }
                )

                GaleriTab.ALBUMS -> AlbumsScreen(
                    albums = albums,
                    onAlbumClick = { album: Album ->
                        val albumPhotos = photos.filter { it.bucketId == album.bucketId }
                        if (albumPhotos.isNotEmpty()) {
                            detailTarget = DetailTarget.FromPhotos(albumPhotos, 0)
                        }
                    }
                )

                GaleriTab.SEARCH -> SearchScreen(
                    photos = photos,
                    onPhotoClick = { photo ->
                        val index = photos.indexOfFirst { it.id == photo.id }
                        detailTarget = DetailTarget.FromPhotos(photos, index.coerceAtLeast(0))
                    }
                )
            }
        }
    }
}
