package com.example.atromitosplagiariouapp.data.repositories

import android.util.Log
import com.example.atromitosplagiariouapp.data.model.Championship
import com.example.atromitosplagiariouapp.data.network.SupabaseClient.client
import io.github.jan.supabase.postgrest.from


object ChampionshipRepository {

    suspend fun fetchChampionshipFromSupabase(): List<Championship> {
        return try {
            val result = client
                .from("championship")
                .select()
                .decodeList<Championship>()

            Log.d("SupabaseFetch", "Fetched ${result.size} championship rows")

            result
        } catch (e: Exception) {
            Log.e("SupabaseFetch", "Error: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun addTeam(team: Championship) {
        try {
            client
                .from("championship")
                .insert(team)
            Log.d("SupabaseAdd", "Team added: ${team.team}")
        } catch (e: Exception) {
            Log.e("SupabaseAdd", "Error: ${e.message}", e)
        }
    }

    suspend fun updateTeam(team: Championship) {
        try {
            team.id?.let { id ->
                client
                    .from("championship")
                    .update(team) {
                        filter { eq("id", id) }
                    }
                Log.d("SupabaseUpdate", "Team updated: ${team.team}")
            } ?: run {
                Log.e("SupabaseUpdate", "Update failed: team ID is null")
            }
        } catch (e: Exception) {
            Log.e("SupabaseUpdate", "Error: ${e.message}", e)
        }
    }

    suspend fun deleteTeam(teamId: Int) {
        try {
            client
                .from("championship")
                .delete {
                    filter { eq("id", teamId) }
                }
            Log.d("SupabaseDelete", "Team deleted: $teamId")
        } catch (e: Exception) {
            Log.e("SupabaseDelete", "Error: ${e.message}", e)
        }
    }

}