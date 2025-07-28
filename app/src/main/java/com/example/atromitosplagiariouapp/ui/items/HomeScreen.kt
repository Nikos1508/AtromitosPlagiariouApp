package com.example.atromitosplagiariouapp.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atromitosplagiariouapp.ui.theme.AtromitosPlagiariouAppTheme

@Composable
fun HomeScreen(
    onNavigateToAnnouncements: () -> Unit,
    onNavigateToPrograms: () -> Unit,
    onNavigateToChampionship: () -> Unit
) {

    val items = listOf(
        Triple("Ανακοινώσεις", Icons.AutoMirrored.Filled.Announcement, onNavigateToAnnouncements),
        Triple("Προγράμματα", Icons.AutoMirrored.Filled.List, onNavigateToPrograms),
        Triple("Πρωτάθλημα", Icons.Default.EmojiEvents, onNavigateToChampionship)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items.forEach { (title, icon, onClick) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "$title Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewLight() {
    AtromitosPlagiariouAppTheme(darkTheme = false) {
        HomeScreen(
            onNavigateToAnnouncements = {},
            onNavigateToPrograms = {},
            onNavigateToChampionship = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewDark() {
    AtromitosPlagiariouAppTheme(darkTheme = true) {
        HomeScreen(
            onNavigateToAnnouncements = {},
            onNavigateToPrograms = {},
            onNavigateToChampionship = {}
        )
    }
}