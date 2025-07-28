package com.example.atromitosplagiariouapp.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.atromitosplagiariouapp.data.model.Programs
import com.example.atromitosplagiariouapp.data.repositories.ProgramRepository
import com.example.atromitosplagiariouapp.ui.theme.AtromitosPlagiariouAppTheme

@Composable
fun ProgramsScreen(
    programEntries: List<Programs> = emptyList()
) {
    val focusManager = LocalFocusManager.current
    var programsState by remember { mutableStateOf(programEntries) }

    val daysOfWeek = listOf("Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή", "Σάββατο", "Κυριακή")

    fun updateProgramTime(id: Long, newStart: String, newEnd: String) {
        programsState = programsState.map {
            if (it.id == id) it.copy(timeStart = newStart, timeEnd = newEnd) else it
        }
    }

    val groupedPrograms = programsState.groupBy { it.group }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(groupedPrograms.entries.toList()) { (groupName, entries) ->
            val displayGroup = if (groupName == "K12") "K12 | 10–12 ετών" else groupName

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = displayGroup,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Day",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        "Time",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                daysOfWeek.forEachIndexed { index, day ->
                    val entry = entries.find { it.day == day }

                    var isEditing by remember { mutableStateOf(false) }
                    var startTime by remember { mutableStateOf(entry?.timeStart ?: "") }
                    var endTime by remember { mutableStateOf(entry?.timeEnd ?: "") }

                    val rowColor = if (index % 2 == 0)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(rowColor, shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = day,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(enabled = entry != null && !isEditing) {
                                        isEditing = true
                                    }
                                    .background(
                                        if (isEditing) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        else Color.Transparent,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                if (entry == null) {
                                    Text(
                                        "-",
                                        textAlign = TextAlign.End,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                } else if (isEditing) {
                                    Column {
                                        TextField(
                                            value = startTime,
                                            onValueChange = { startTime = it },
                                            label = { Text("Start") },
                                            singleLine = true,
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                            ),
                                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        TextField(
                                            value = endTime,
                                            onValueChange = { endTime = it },
                                            label = { Text("End") },
                                            singleLine = true,
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                            ),
                                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                                            keyboardActions = KeyboardActions(
                                                onDone = {
                                                    isEditing = false
                                                    focusManager.clearFocus()
                                                    updateProgramTime(entry.id, startTime, endTime)  // <-- id is Long
                                                }
                                            )
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "${entry.timeStart} - ${entry.timeEnd}",
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgramsRoute() {
    var programs by remember { mutableStateOf<List<Programs>>(emptyList()) }

    LaunchedEffect(Unit) {
        val fetched = ProgramRepository.fetchProgramsFromSupabase()
        println("Fetched programs: $fetched") // Debug print
        programs = fetched
    }

    ProgramsScreen(programEntries = programs)
}



@Preview(showBackground = true)
@Composable
fun ProgramsScreenPreviewLight() {
    val samplePrograms = listOf(
        Programs(1L, "K12", "Δευτέρα", "10:00", "12:00"),
        Programs(2L, "K12", "Τρίτη", "10:00", "12:00"),
        Programs(3L, "K12", "Τετάρτη", "10:00", "12:00"),
        Programs(4L, "Adult", "Δευτέρα", "18:00", "20:00"),
        Programs(5L, "Adult", "Τετάρτη", "18:00", "20:00"),
        Programs(6L, "Adult", "Παρασκευή", "18:00", "20:00"),
    )
    AtromitosPlagiariouAppTheme(darkTheme = false) {
        ProgramsScreen(programEntries = samplePrograms)
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramsScreenPreviewDark() {
    val samplePrograms = listOf(
        Programs(1L, "K12", "Δευτέρα", "10:00", "12:00"),
        Programs(2L, "K12", "Τρίτη", "10:00", "12:00"),
        Programs(3L, "K12", "Τετάρτη", "10:00", "12:00"),
        Programs(4L, "Adult", "Δευτέρα", "18:00", "20:00"),
        Programs(5L, "Adult", "Τετάρτη", "18:00", "20:00"),
        Programs(6L, "Adult", "Παρασκευή", "18:00", "20:00"),
    )
    AtromitosPlagiariouAppTheme(darkTheme = true) {
        ProgramsScreen(programEntries = samplePrograms)
    }
}