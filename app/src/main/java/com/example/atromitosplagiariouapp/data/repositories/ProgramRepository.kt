package com.example.atromitosplagiariouapp.data.repositories

import android.util.Log
import com.example.atromitosplagiariouapp.data.model.Programs
import com.example.atromitosplagiariouapp.data.network.SupabaseClient.client
import io.github.jan.supabase.postgrest.from

object ProgramRepository {

    suspend fun fetchProgramsFromSupabase(): List<Programs> {
        return try {
            val result = client
                .from("programs")
                .select()
                .decodeList<Programs>()

            Log.d("SupabaseFetch", "Fetched ${result.size} programs")

            result
        } catch (e: Exception) {

            Log.e("SupabaseFetch", "Error: ${e.message}", e)

            emptyList()
        }
    }

}