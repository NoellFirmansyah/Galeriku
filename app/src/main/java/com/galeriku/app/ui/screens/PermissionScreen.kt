package com.galeriku.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.galeriku.app.R

@Composable
fun PermissionScreen(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.PhotoLibrary,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = stringResource(R.string.permission_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.permission_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(24.dp))
        Button(onClick = onRequestPermission) {
            Text(text = stringResource(R.string.permission_grant))
        }
    }
}
