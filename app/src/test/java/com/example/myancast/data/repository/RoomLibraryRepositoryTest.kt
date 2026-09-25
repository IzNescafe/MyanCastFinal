package com.example.myancast.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myancast.data.local.AppDatabase
import com.example.myancast.data.repository.RoomLibraryRepository
import com.example.myancast.domain.model.PlaybackProgress
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RoomLibraryRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repo: RoomLibraryRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repo = RoomLibraryRepository(db.libraryDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    // ─── Subscribe ───

    @Test
    fun `subscribe adds podcast to subscribedIds`() = runTest {
        repo.subscribe("podcast_1")
        val ids = repo.subscribedIds().first()
        assertEquals(setOf("podcast_1"), ids)
    }

    @Test
    fun `unsubscribe removes podcast from subscribedIds`() = runTest {
        repo.subscribe("podcast_1")
        repo.subscribe("podcast_2")
        repo.unsubscribe("podcast_1")
        val ids = repo.subscribedIds().first()
        assertEquals(setOf("podcast_2"), ids)
    }

    @Test
    fun `subscribedIds empty when nothing subscribed`() = runTest {
        val ids = repo.subscribedIds().first()
        assertTrue(ids.isEmpty())
    }

    // ─── History ───

    @Test
    fun `saveProgress adds to history`() = runTest {
        val progress = PlaybackProgress(
            episodeId = "ep_1",
            podcastId = "pod_1",
            positionMs = 5000L,
            durationMs = 10000L,
            updatedAt = System.currentTimeMillis()
        )
        repo.saveProgress(progress)
        val history = repo.history().first()
        assertEquals(1, history.size)
        assertEquals("ep_1", history[0].episodeId)
        assertEquals(5000L, history[0].positionMs)
    }

    @Test
    fun `saveProgress upserts same episode`() = runTest {
        val progress1 = PlaybackProgress(
            episodeId = "ep_1",
            podcastId = "pod_1",
            positionMs = 5000L,
            durationMs = 10000L,
            updatedAt = 1L
        )
        val progress2 = progress1.copy(positionMs = 8000L, updatedAt = 2L)
        repo.saveProgress(progress1)
        repo.saveProgress(progress2)
        val history = repo.history().first()
        assertEquals(1, history.size)
        assertEquals(8000L, history[0].positionMs)
    }

    @Test
    fun `lastPlayed returns latest by updatedAt`() = runTest {
        repo.saveProgress(
            PlaybackProgress(
                episodeId = "ep_old",
                podcastId = "pod_1",
                positionMs = 1000L,
                durationMs = 10000L,
                updatedAt = 1L
            )
        )
        repo.saveProgress(
            PlaybackProgress(
                episodeId = "ep_new",
                podcastId = "pod_1",
                positionMs = 5000L,
                durationMs = 10000L,
                updatedAt = 2L
            )
        )
        val last = repo.lastPlayed().first()
        assertNotNull(last)
        assertEquals("ep_new", last!!.episodeId)
    }

    @Test
    fun `lastPlayed returns null when history empty`() = runTest {
        val last = repo.lastPlayed().first()
        assertNull(last)
    }

    // ─── PlaybackProgress computed ───

    @Test
    fun `fraction computed correctly`() {
        val p = PlaybackProgress(
            episodeId = "ep_1",
            podcastId = "pod_1",
            positionMs = 5000L,
            durationMs = 10000L,
            updatedAt = 0L
        )
        assertEquals(0.5f, p.fraction)
    }

    @Test
    fun `fraction returns 0 when duration 0`() {
        val p = PlaybackProgress(
            episodeId = "ep_1",
            podcastId = "pod_1",
            positionMs = 5000L,
            durationMs = 0L,
            updatedAt = 0L
        )
        assertEquals(0f, p.fraction)
    }

    @Test
    fun `isFinished true at 95 percent or more`() = runTest {
        val p = PlaybackProgress(
            episodeId = "ep_1",
            podcastId = "pod_1",
            positionMs = 9600L,
            durationMs = 10000L,
            updatedAt = 0L
        )
        assertTrue(p.isFinished)
    }

    @Test
    fun `isFinished false below 95 percent`() = runTest {     
        val p = PlaybackProgress(
            episodeId = "ep_1",
            podcastId = "pod_1",
            positionMs = 5000L,
            durationMs = 10000L,
            updatedAt = 0L
        )
        assertEquals(false, p.isFinished)
    }
}