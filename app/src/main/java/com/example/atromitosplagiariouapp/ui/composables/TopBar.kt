package com.example.atromitosplagiariouapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.atromitosplagiariouapp.R
import com.example.atromitosplagiariouapp.ui.theme.AtromitosPlagiariouAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.atromitos_plagiariou),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        modifier = Modifier
            .padding(WindowInsets.statusBars.asPaddingValues())
            .fillMaxWidth(),
        navigationIcon = if (canNavigateBack) {
            {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        } else {
            {}
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AppTopBarPreviewLight() {
    AtromitosPlagiariouAppTheme(darkTheme = false) {
        AppTopBar(
            title = "Ατρόμητος Πλαγιαρίου App",
            canNavigateBack = true,
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppTopBarPreviewDark() {
    AtromitosPlagiariouAppTheme(darkTheme = true) {
        AppTopBar(
            title = "Ατρόμητος Πλαγιαρίου App",
            canNavigateBack = true,
            onBackClick = {}
        )
    }
}
