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

}