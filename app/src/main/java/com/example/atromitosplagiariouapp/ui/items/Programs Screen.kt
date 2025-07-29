package com.example.atromitosplagiariouapp.ui.items

import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.atromitosplagiariouapp.data.model.Programs
import com.example.atromitosplagiariouapp.data.repositories.ProgramRepository

@Composable
fun ProgramsScreen(
    programsByGroup: Map<String, List<Programs>> = emptyMap(),
    onUpdatePrograms: (Map<String, List<Programs>>) -> Unit
){
    val allDays = listOf("Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή", "Σάββατο", "Κυριακή")

    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var selectedDay by remember { mutableStateOf<String?>(null) }
    var selectedProgram by remember { mutableStateOf<Programs?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Weekly Programs",
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
                    "No programs found.",
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
                                    text = "Group: $group",
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
                                        text = "Day",
                                        modifier = Modifier.weight(1f),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Time",
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

                                    Log.d("ProgramsScreen", "Day: $day, Time: $timeText")

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
                                                contentDescription = "Add Time",
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
    }
}

@Composable
fun RowScope.TableCell(text: String, weight: Float, isHeader: Boolean = false) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp),
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal
    )
}


@Composable
fun ProgramsRoute() {
    var groupedPrograms by remember { mutableStateOf<Map<String, List<Programs>>>(emptyMap()) }


    LaunchedEffect(Unit) {
        val fetched = ProgramRepository.fetchProgramsFromSupabase().map {
            it.copy(day = it.day.trim().replaceFirstChar { c -> c.uppercaseChar() }.lowercase().replaceFirstChar { c -> c.uppercaseChar() })
        }

        Log.d("ProgramsRoute", "Fetched ${fetched.size} programs")

        fetched.forEach {
            Log.d("SupabaseData", "Raw Day: '${it.day}', Group: '${it.group}', Time: ${it.timestart} - ${it.timeend}")
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
                    Text("Save")
                }
                TextButton(onClick = {
                    onBlank()
                }) {
                    Text("Set Blank")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text("Edit Program for $day")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = start,
                    onValueChange = { start = it },
                    label = { Text("Start Time") }
                )
                OutlinedTextField(
                    value = end,
                    onValueChange = { end = it },
                    label = { Text("End Time") }
                )
            }
        }
    )
}