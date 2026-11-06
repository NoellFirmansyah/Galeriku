package com.galeriku.app.ui.components

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

/**
 * Gambar yang bisa di-pinch zoom & digeser, dipakai di tampilan foto layar penuh.
 *
 * CATATAN: implementasi pinch-zoom manual ini bisa sedikit "berebut" gesture dengan
 * swipe HorizontalPager di sekitarnya (keduanya mendeteksi gesture multi-sentuh).
 * Untuk hasil produksi yang lebih mulus, pertimbangkan library khusus seperti
 * "Telephoto" (me.saket.telephoto) sebagai pengganti komponen ini.
 */
@Composable
fun ZoomableAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    var scale by remember(model) { mutableStateOf(1f) }
    var offsetX by remember(model) { mutableStateOf(0f) }
    var offsetY by remember(model) { mutableStateOf(0f) }

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            )
            .pointerInput(model) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(1f, 5f)
                    if (scale == 1f) {
                        offsetX = 0f
                        offsetY = 0f
                    } else {
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
            }
    )
}
