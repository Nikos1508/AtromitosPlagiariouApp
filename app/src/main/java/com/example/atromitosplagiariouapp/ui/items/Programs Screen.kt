package com.example.atromitosplagiariouapp.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.atromitosplagiariouapp.data.model.Programs
import com.example.atromitosplagiariouapp.data.repositories.ProgramRepository

@Composable
fun ProgramsScreen(
    programsByGroup: Map<String, List<Programs>> = emptyMap(),
    onUpdatePrograms: (Map<String, List<Programs>>) -> Unit
) {
    val allDays = listOf("Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή", "Σάββατο", "Κυριακή")

    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var selectedDay by remember { mutableStateOf<String?>(null) }
    var selectedProgram by remember { mutableStateOf<Programs?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showNewGroupDialog by remember { mutableStateOf(false) }

    var newGroupName by remember { mutableStateOf("") }
    var newGroupDay by remember { mutableStateOf(allDays.first()) }
    var newGroupStartTime by remember { mutableStateOf("") }
    var newGroupEndTime by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Εβδομαδιαία προγράμματα",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            if (programsByGroup.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Δεν βρέθηκαν προγράμματα / Φόρτωση προγραμμάτων",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    programsByGroup.forEach { (group, programs) ->
                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Κατηγορία: $group",
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                            .padding(vertical = 8.dp, horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Ημέρα",
                                            modifier = Modifier.weight(1f),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "Ώρα",
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.End,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    )

                                    allDays.forEach { day ->
                                        val programForDay = programs.find { it.day.equals(day, ignoreCase = true) }

                                        val timeText = if (programForDay != null) {
                                            "${programForDay.timestart} - ${programForDay.timeend}"
                                        } else {
                                            "-"
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp, horizontal = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = day,
                                                modifier = Modifier.weight(1f),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            if (timeText == "-") {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = "Προσθήκη προπόνησης",
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clickable {
                                                            selectedGroup = group
                                                            selectedDay = day
                                                            selectedProgram = null
                                                            showDialog = true
                                                        },
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            } else {
                                                Text(
                                                    text = timeText,
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clickable {
                                                            selectedGroup = group
                                                            selectedDay = day
                                                            selectedProgram = programForDay
                                                            showDialog = true
                                                        },
                                                    textAlign = TextAlign.End,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                                )
                                            }
                                        }

                                        HorizontalDivider(
                                            thickness = 0.5.dp,
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showNewGroupDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Προσθήκη κατηγορίας")
        }

        if (showDialog && selectedDay != null && selectedGroup != null) {
            ProgramEditDialog(
                initialProgram = selectedProgram,
                day = selectedDay!!,
                onDismiss = {
                    showDialog = false
                    selectedProgram = null
                },
                onSave = { startTime, endTime ->
                    val updatedMap = programsByGroup.toMutableMap().apply {
                        val groupPrograms = get(selectedGroup!!)!!.toMutableList()
                        val updatedProgram = selectedProgram?.copy(
                            timestart = startTime!!,
                            timeend = endTime!!
                        ) ?: Programs(
                            id = null,
                            day = selectedDay!!,
                            group = selectedGroup!!,
                            timestart = startTime!!,
                            timeend = endTime!!
                        )

                        groupPrograms.removeAll { it.day == selectedDay }
                        groupPrograms.add(updatedProgram)
                        this[selectedGroup!!] = groupPrograms.sortedWith(compareBy({ it.day }, { it.timestart }))
                    }

                    onUpdatePrograms(updatedMap)
                    showDialog = false
                },
                onBlank = {
                    val updatedMap = programsByGroup.toMutableMap().apply {
                        val groupPrograms = get(selectedGroup!!)!!.toMutableList()
                        groupPrograms.removeAll { it.day == selectedDay }
                        this[selectedGroup!!] = groupPrograms
                    }
                    onUpdatePrograms(updatedMap)
                    showDialog = false
                }
            )
        }

        if (showNewGroupDialog) {
            AlertDialog(
                onDismissRequest = { showNewGroupDialog = false },
                title = { Text("Προσθήκη κατηγορίας") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newGroupName,
                            onValueChange = { newGroupName = it },
                            label = { Text("Κατηγορία") }
                        )
                        DropdownMenuBox(
                            items = allDays,
                            selectedItem = newGroupDay,
                            onItemSelected = { newGroupDay = it }
                        )
                        OutlinedTextField(
                            value = newGroupStartTime,
                            onValueChange = { newGroupStartTime = it },
                            label = { Text("Ώρα έναρξης") }
                        )
                        OutlinedTextField(
                            value = newGroupEndTime,
                            onValueChange = { newGroupEndTime = it },
                            label = { Text("Ώρα λήξης") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newGroupName.isNotBlank()) {
                            val newProgram = Programs(
                                id = null,
                                day = newGroupDay,
                                group = newGroupName,
                                timestart = newGroupStartTime,
                                timeend = newGroupEndTime
                            )

                            val updatedMap = programsByGroup.toMutableMap().apply {
                                this[newGroupName] = listOf(newProgram)
                            }
                            onUpdatePrograms(updatedMap)

                            newGroupName = ""
                            newGroupDay = allDays.first()
                            newGroupStartTime = ""
                            newGroupEndTime = ""
                            showNewGroupDialog = false
                        }
                    }) {
                        Text("Προσθήκη")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNewGroupDialog = false }) {
                        Text("Ακύρωση")
                    }
                }
            )
        }
    }
}

@Composable
fun DropdownMenuBox(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            label = { Text("Ημέρα") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth().clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { day ->
                DropdownMenuItem(
                    text = { Text(day) },
                    onClick = {
                        onItemSelected(day)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProgramEditDialog(
    initialProgram: Programs?,
    day: String,
    onDismiss: () -> Unit,
    onSave: (String?, String?) -> Unit,
    onBlank: () -> Unit
) {
    var start by remember { mutableStateOf(initialProgram?.timestart ?: "") }
    var end by remember { mutableStateOf(initialProgram?.timeend ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row {
                TextButton(onClick = {
                    onSave(start, end)
                }) {
                    Text("Αποθήκευση")
                }
                TextButton(onClick = {
                    onBlank()
                }) {
                    Text("Καθάρισε")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Ακύρωση")
            }
        },
        title = {
            Text("Αλλαγή προγράμματος για την $day")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = start,
                    onValueChange = { start = it },
                    label = { Text("Ώρα έναρξης") }
                )
                OutlinedTextField(
                    value = end,
                    onValueChange = { end = it },
                    label = { Text("Ώρα λήξης") }
                )
            }
        }
    )
}

@Composable
fun ProgramsRoute() {
    var groupedPrograms by remember { mutableStateOf<Map<String, List<Programs>>>(emptyMap()) }

    LaunchedEffect(Unit) {
        val fetched = ProgramRepository.fetchProgramsFromSupabase().map {
            it.copy(day = it.day.trim().replaceFirstChar { c -> c.uppercaseChar() }.lowercase().replaceFirstChar { c -> c.uppercaseChar() })
        }

        groupedPrograms = fetched
            .groupBy { it.group }
            .mapValues { (_, programs) ->
                programs.sortedWith(compareBy({ it.day }, { it.timestart }))
            }
    }

    ProgramsScreen(
        programsByGroup = groupedPrograms,
        onUpdatePrograms = { updated ->
            groupedPrograms = updated
        }
    )
}