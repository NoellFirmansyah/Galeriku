package com.galeriku.app

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.galeriku.app.theme.GaleriKuTheme
import com.galeriku.app.ui.GaleriAppRoot
import com.galeriku.app.util.requiredMediaPermission
import com.galeriku.app.viewmodel.GalleryViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GalleryViewModel by viewModels()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onPermissionResult(granted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val alreadyGranted = ContextCompat.checkSelfPermission(
            this,
            requiredMediaPermission()
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.onPermissionResult(alreadyGranted)

        setContent {
            GaleriKuTheme {
                GaleriAppRoot(
                    viewModel = viewModel,
                    onRequestPermission = {
                        permissionLauncher.launch(requiredMediaPermission())
                    }
                )
            }
        }
    }
}
