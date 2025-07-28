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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProgramsScreen(
    programs: Map<String, Map<String, String>> = emptyMap()
) {
    val focusManager = LocalFocusManager.current

    val programsState = remember { mutableStateOf(programs) }

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    fun updateProgramTime(category: String, day: String, newTime: String) {
        val current = programsState.value.toMutableMap()
        val catSchedule = current[category]?.toMutableMap() ?: mutableMapOf()
        catSchedule[day] = newTime
        current[category] = catSchedule
        programsState.value = current
    }

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
        items(programsState.value.entries.toList()) { entry ->
            val category = entry.key
            val schedule = entry.value

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Day",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "Time",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                daysOfWeek.forEachIndexed { index, day ->

                    var isEditing by remember { mutableStateOf(false) }

                    var time by remember { mutableStateOf(schedule[day]?.takeIf { it.isNotBlank() } ?: "-") }

                    val rowBackgroundColor = if (index % 2 == 0)
                        MaterialTheme.colorScheme.surface
                    else
                        MaterialTheme.colorScheme.surfaceVariant

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(rowBackgroundColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = day,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(enabled = !isEditing) {
                                        isEditing = true
                                    }
                                    .background(
                                        if (isEditing) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                if (isEditing) {
                                    TextField(
                                        value = time.takeIf { it != "-" } ?: "",
                                        onValueChange = { newValue -> time = newValue },
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .onFocusChanged { focusState ->
                                                if (!focusState.isFocused) {
                                                    isEditing = false
                                                    updateProgramTime(
                                                        category,
                                                        day,
                                                        time.takeIf { it.isNotBlank() && it != "-" } ?: "-"
                                                    )
                                                }
                                            },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                isEditing = false
                                                focusManager.clearFocus()
                                                updateProgramTime(
                                                    category,
                                                    day,
                                                    time.takeIf { it.isNotBlank() && it != "-" } ?: "-"
                                                )
                                            }
                                        )
                                    )
                                } else {
                                    Text(
                                        text = time,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }

                        if (index < daysOfWeek.size - 1) {
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramsScreenPreviewLight() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        val samplePrograms = mapOf(
            "K12" to mapOf(
                "Monday" to "17:00 - 18:30",
                "Tuesday" to "-",
                "Wednesday" to "16:30 - 18:00",
                "Thursday" to "-",
                "Friday" to "17:00 - 18:30",
                "Saturday" to "-",
                "Sunday" to "-"
            ),
            "K14" to mapOf(
                "Monday" to "18:00 - 19:30",
                "Tuesday" to "18:00 - 19:30",
                "Wednesday" to "-",
                "Thursday" to "18:00 - 19:30",
                "Friday" to "-",
                "Saturday" to "-",
                "Sunday" to "-"
            )
        )
        ProgramsScreen(programs = samplePrograms)
    }
}

@Preview(showBackground = true)
@Composable
fun ProgramsScreenPreviewDark() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        val samplePrograms = mapOf(
            "K12" to mapOf(
                "Monday" to "17:00 - 18:30",
                "Tuesday" to "-",
                "Wednesday" to "16:30 - 18:00",
                "Thursday" to "-",
                "Friday" to "17:00 - 18:30",
                "Saturday" to "-",
                "Sunday" to "-"
            ),
            "K14" to mapOf(
                "Monday" to "18:00 - 19:30",
                "Tuesday" to "18:00 - 19:30",
                "Wednesday" to "-",
                "Thursday" to "18:00 - 19:30",
                "Friday" to "-",
                "Saturday" to "-",
                "Sunday" to "-"
            )
        )
        ProgramsScreen(programs = samplePrograms)
    }
}