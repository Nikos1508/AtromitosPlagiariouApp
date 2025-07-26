package com.example.atromitosplagiariouapp.data.network

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Ind6aWlkeml1YXh4eWtucW1zYnp6Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc1MzUyNzAzNywiZXhwIjoyMDY5MTAzMDM3fQ.JZva98NhzKqz7NehjGjQSIgeLvZR1r9hzoZxnLv-nc8",
        supabaseKey = "https://wziidziuaxxyknqmsbzz.supabase.co"
    ) {
        install(Auth)
        install(Postgrest)
    }
}