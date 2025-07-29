package com.example.atromitosplagiariouapp.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.atromitosplagiariouapp.data.model.Championship
import com.example.atromitosplagiariouapp.data.repositories.ChampionshipRepository
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ChampionshipScreen() {
    val coroutineScope = rememberCoroutineScope()

    var championshipData by remember { mutableStateOf<List<Championship>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTeam by remember { mutableStateOf<Championship?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        championshipData = ChampionshipRepository.fetchChampionshipFromSupabase()
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Βαθμολογία Πρωταθλήματος",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalScrollTable(
                championshipData,
                onTeamClick = { team ->
                    selectedTeam = team
                    showDialog = true
                },
                onDeleteTeam = { team ->
                    team.id?.let { id ->
                        coroutineScope.launch {
                            ChampionshipRepository.deleteTeam(id)
                            championshipData = ChampionshipRepository.fetchChampionshipFromSupabase()
                        }
                    }
                }
            )

            Button(
                onClick = {
                    selectedTeam = Championship(0, "", 0, "0-0", 0, 0, 0)
                    showDialog = true
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Προσθήκη Ομάδας")
            }
        }
    }

    if (showDialog && selectedTeam != null) {
        EditTeamDialog(
            team = selectedTeam!!,
            onDismiss = { showDialog = false },
            onSave = { updatedTeam ->
                coroutineScope.launch {
                    if (updatedTeam.id == 0) {
                        ChampionshipRepository.addTeam(updatedTeam)
                    } else {
                        ChampionshipRepository.updateTeam(updatedTeam)
                    }

                    championshipData = ChampionshipRepository.fetchChampionshipFromSupabase()
                    showDialog = false
                }
            }
        )
    }
}

@Composable
fun HorizontalScrollTable(
    data: List<Championship>,
    onTeamClick: (Championship) -> Unit,
    onDeleteTeam: (Championship) -> Unit
) {
    val columnTitles = listOf("Ομάδα", "Βαθμοί", "Αγώνες", "Γκολ", "Νίκες", "Ισοπαλίες", "Ήττες")

    Box(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                columnTitles.forEach { title ->
                    Text(
                        text = title,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )

            data.forEachIndexed { index, team ->
                val matches = team.wins + team.draws + team.loses
                val backgroundColor = if (index % 2 == 0) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface

                val rowItems = listOf(
                    team.team,
                    team.points.toString(),
                    matches.toString(),
                    team.goals,
                    team.wins.toString(),
                    team.draws.toString(),
                    team.loses.toString()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onTeamClick(team) },
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowItems.forEach { cell ->
                            Text(
                                text = cell,
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(8.dp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { onDeleteTeam(team) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
fun EditTeamDialog(team: Championship, onDismiss: () -> Unit, onSave: (Championship) -> Unit) {
    var name by remember { mutableStateOf(team.team) }
    var points by remember { mutableStateOf(team.points.toString()) }
    var goals by remember { mutableStateOf(team.goals) }
    var wins by remember { mutableStateOf(team.wins.toString()) }
    var draws by remember { mutableStateOf(team.draws.toString()) }
    var loses by remember { mutableStateOf(team.loses.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Επεξεργασία Ομάδας") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Ομάδα") })
                OutlinedTextField(value = points, onValueChange = { points = it }, label = { Text("Βαθμοί") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = goals, onValueChange = { goals = it }, label = { Text("Goal") })
                OutlinedTextField(value = wins, onValueChange = { wins = it }, label = { Text("Νίκες") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = draws, onValueChange = { draws = it }, label = { Text("Ισοπαλίες") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = loses, onValueChange = { loses = it }, label = { Text("Ήττες") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    team.copy(
                        team = name,
                        points = points.toIntOrNull() ?: team.points,
                        goals = goals,
                        wins = wins.toIntOrNull() ?: team.wins,
                        draws = draws.toIntOrNull() ?: team.draws,
                        loses = loses.toIntOrNull() ?: team.loses
                    )
                )
            }) {
                Text("Αποθήκευση")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Άκυρο")
            }
        }
    )
}