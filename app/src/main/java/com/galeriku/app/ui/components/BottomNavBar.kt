package com.galeriku.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(
    selectedTab: GaleriTab,
    onTabSelected: (GaleriTab) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        GaleriTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            val tint = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .selectable(selected = isSelected, onClick = { onTabSelected(tab) })
                    .padding(horizontal = 20.dp, vertical = 4.dp)
            ) {
                Icon(imageVector = tab.icon, contentDescription = tab.label, tint = tint)
                Text(
                    text = tab.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = tint
                )
            }
        }
    }
}
