package com.galeriku.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.galeriku.app.data.MediaStoreRepository
import com.galeriku.app.model.Album
import com.galeriku.app.model.Photo
import com.galeriku.app.model.PhotoSection
import com.galeriku.app.util.groupPhotosByMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MediaStoreRepository(application)

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos.asStateFlow()

    private val _sections = MutableStateFlow<List<PhotoSection>>(emptyList())
    val sections: StateFlow<List<PhotoSection>> = _sections.asStateFlow()

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasPermission = MutableStateFlow(false)
    val hasPermission: StateFlow<Boolean> = _hasPermission.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<Long>>(emptySet())
    val favoriteIds: StateFlow<Set<Long>> = _favoriteIds.asStateFlow()

    private val _deleteError = MutableStateFlow<String?>(null)
    val deleteError: StateFlow<String?> = _deleteError.asStateFlow()

    fun toggleFavorite(photoId: Long) {
        _favoriteIds.value = _favoriteIds.value.toMutableSet().apply {
            if (!add(photoId)) remove(photoId)
        }
    }

    /**
     * Mencoba hapus foto lewat MediaStore. Di Android 9-10 ini hanya berhasil untuk
     * file yang dibuat/dimiliki aplikasi ini sendiri tanpa izin WRITE_EXTERNAL_STORAGE
     * tambahan - batasan ini kita laporkan lewat [deleteError], bukan dipaksakan.
     */
    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                try {
                    getApplication<Application>().contentResolver.delete(photo.uri, null, null) > 0
                } catch (e: SecurityException) {
                    false
                } catch (e: Exception) {
                    false
                }
            }
            if (success) {
                refresh()
            } else {
                _deleteError.value =
                    "Tidak bisa menghapus foto ini. Perangkatmu mungkin perlu izin penyimpanan tambahan."
            }
        }
    }

    fun clearDeleteError() {
        _deleteError.value = null
    }

    fun onPermissionResult(granted: Boolean) {
        _hasPermission.value = granted
        if (granted) refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            val loadedPhotos = repository.loadPhotos()
            _photos.value = loadedPhotos
            _sections.value = groupPhotosByMonth(loadedPhotos)
            _albums.value = repository.loadAlbums(loadedPhotos)
            _isLoading.value = false
        }
    }

    fun photoAt(id: Long): Photo? = _photos.value.firstOrNull { it.id == id }
}
