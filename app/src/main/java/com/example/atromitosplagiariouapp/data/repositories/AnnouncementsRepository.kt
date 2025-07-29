package com.example.atromitosplagiariouapp.data.repositories

import com.example.atromitosplagiariouapp.data.model.Announcements
import com.example.atromitosplagiariouapp.data.network.SupabaseClient.client
import io.github.jan.supabase.postgrest.from

object AnnouncementRepository {

    suspend fun addAnnouncement(title: String, text: String): Announcements {
        val newAnnouncement = Announcements(id = null, title = title, text = text)

        client.from("announcements").insert(newAnnouncement)

        return newAnnouncement
    }

    suspend fun getAnnouncements(): List<Announcements> {
        return client.from("announcements")
            .select()
            .decodeList<Announcements>()
    }

    suspend fun deleteAnnouncement(announcement: Announcements) {
        announcement.id?.let {
            client.from("announcements").delete {
                filter { eq("id", it) }
            }
        }
    }

    suspend fun updateAnnouncement(old: Announcements, updated: Announcements) {
        updated.id?.let {
            client.from("announcements").update(updated) {
                filter { eq("id", it) }
            }
        }
    }
}