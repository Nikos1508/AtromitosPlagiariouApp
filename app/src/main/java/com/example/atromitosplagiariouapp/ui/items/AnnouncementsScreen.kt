package com.example.atromitosplagiariouapp.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.atromitosplagiariouapp.data.model.Announcements
import com.example.atromitosplagiariouapp.data.repositories.AnnouncementRepository
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun AnnouncementsScreen() {
    val repository = AnnouncementRepository

    var announcements by remember { mutableStateOf(listOf<Announcements>()) }
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var editingAnnouncement by remember { mutableStateOf<Announcements?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        announcements = repository.getAnnouncements()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                title = ""
                text = ""
                isEditing = false
                editingAnnouncement = null
                showDialog = true
            }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(announcements) { announcement ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            title = announcement.title
                            text = announcement.text
                            editingAnnouncement = announcement
                            isEditing = true
                            showDialog = true
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = announcement.title,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                scope.launch {
                                    repository.deleteAnnouncement(announcement)
                                    announcements = repository.getAnnouncements()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Διαγραφή ανακοίνωσης"
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = announcement.text)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (isEditing) "Επεξεργασία ανακοίνωσης" else "Νέα ανακοίνωση") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Τίτλος ανακοίνωσης") }
                    )
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Κυρίως ανακοίνωση") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        if (isEditing && editingAnnouncement != null) {
                            val updated = editingAnnouncement!!.copy(title = title, text = text)

                            repository.updateAnnouncement(editingAnnouncement!!, updated)
                            announcements = repository.getAnnouncements()
                        } else {
                            repository.addAnnouncement(title, text)
                            announcements = repository.getAnnouncements()
                        }
                        showDialog = false
                        title = ""
                        text = ""
                        isEditing = false
                        editingAnnouncement = null
                    }
                }) {
                    Text(if (isEditing) "Αποθήκευση" else "Προσθήκη")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    showDialog = false
                    isEditing = false
                    editingAnnouncement = null
                }) {
                    Text("Ακύρωση")
                }
            }
        )
    }
}