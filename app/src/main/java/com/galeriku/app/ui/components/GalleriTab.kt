package com.galeriku.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class GaleriTab(val route: String, val label: String, val icon: ImageVector) {
    LIBRARY("library", "Library", Icons.Filled.PhotoLibrary),
    ALBUMS("albums", "Album", Icons.Filled.Collections),
    SEARCH("search", "Cari", Icons.Filled.Search)
}
