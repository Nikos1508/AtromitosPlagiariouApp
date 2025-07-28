package com.example.atromitosplagiariouapp.data.repositories

import com.example.atromitosplagiariouapp.data.model.Programs
import com.example.atromitosplagiariouapp.data.network.SupabaseClient.client
import io.github.jan.supabase.postgrest.from

object ProgramRepository {

    suspend fun fetchProgramsFromSupabase(): List<Programs> {
        return try {
            client
                .from("programs")
                .select()
                .decodeList<Programs>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}