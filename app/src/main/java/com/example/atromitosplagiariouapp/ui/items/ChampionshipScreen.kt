package com.example.atromitosplagiariouapp.ui.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.atromitosplagiariouapp.data.model.Championship
import com.example.atromitosplagiariouapp.ui.theme.AtromitosPlagiariouAppTheme

@Composable
fun ChampionshipScreen(championshipList: List<Championship>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Βαθμολογία Πρωταθλήματος",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Ομάδα", modifier = Modifier.weight(2f), color = MaterialTheme.colorScheme.secondary)
            Text("Βαθ.", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.secondary)
            Text("Γκολ", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.secondary)
            Text("Νίκ.", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.secondary)
            Text("Ισοπ.", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.secondary)
            Text("Ήττ.", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.secondary)
        }

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outline)

        LazyColumn {
            items(championshipList) { team ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(team.team, modifier = Modifier.weight(2f))
                    Text(team.points.toString(), modifier = Modifier.weight(1f))
                    Text(team.goals, modifier = Modifier.weight(1f))
                    Text(team.wins.toString(), modifier = Modifier.weight(1f))
                    Text(team.draws.toString(), modifier = Modifier.weight(1f))
                    Text(team.loses.toString(), modifier = Modifier.weight(1f))
                }
                HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}


fun sampleChampionshipData(): List<Championship> {
    return listOf(
        Championship(id = 1, team = "Ατρόμητος", points = 30, goals = "25-10", wins = 9, draws = 3, loses = 2),
        Championship(id = 2, team = "Θύελλα", points = 28, goals = "22-12", wins = 8, draws = 4, loses = 2),
        Championship(id = 3, team = "Δόξα", points = 26, goals = "20-14", wins = 8, draws = 2, loses = 4),
    )
}


@Preview(showBackground = true)
@Composable
fun ChampionshipScreenPreviewLight() {
    AtromitosPlagiariouAppTheme(darkTheme = false) {
        ChampionshipScreen(championshipList = sampleChampionshipData())
    }
}

@Preview(showBackground = true)
@Composable
fun ChampionshipScreenPreviewDark() {
    AtromitosPlagiariouAppTheme(darkTheme = true) {
        ChampionshipScreen(championshipList = sampleChampionshipData())
    }
}