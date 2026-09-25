package com.example.myancast.data.repository

import com.example.myancast.data.local.LibraryDao
import com.example.myancast.data.local.LibraryEntity
import com.example.myancast.domain.model.PlaybackProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLibraryRepository(
    private val dao: LibraryDao
) : LibraryRepository {

    override fun subscribedIds(): Flow<Set<String>> =
        dao.byType(LibraryEntity.TYPE_SUB).map { list ->
            list.mapTo(mutableSetOf()) { it.id }
        }

    override suspend fun subscribe(podcastId: String) {
        dao.upsert(
            LibraryEntity(
                id = podcastId,
                type = LibraryEntity.TYPE_SUB,
                podcastId = podcastId,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun unsubscribe(podcastId: String) {
        dao.delete(podcastId, LibraryEntity.TYPE_SUB)
    }

    override fun history(): Flow<List<PlaybackProgress>> =
        dao.byType(LibraryEntity.TYPE_HISTORY).map { list ->
            list.map { it.toProgress() }
        }

    override fun lastPlayed(): Flow<PlaybackProgress?> =
        dao.lastPlayed().map { it?.toProgress() }

    override suspend fun saveProgress(progress: PlaybackProgress) {
        dao.upsert(
            LibraryEntity(
                id = progress.episodeId,
                type = LibraryEntity.TYPE_HISTORY,
                podcastId = progress.podcastId,
                positionMs = progress.positionMs,
                durationMs = progress.durationMs,
                updatedAt = progress.updatedAt,
                episodeTitle = progress.episodeTitle,
                podcastTitle = progress.podcastTitle,
                coverUrl = progress.coverUrl
            )
        )
    }
}

private fun LibraryEntity.toProgress() = PlaybackProgress(
    episodeId = id,
    podcastId = podcastId,
    positionMs = positionMs,
    durationMs = durationMs,
    updatedAt = updatedAt,
    episodeTitle = episodeTitle,
    podcastTitle = podcastTitle,
    coverUrl = coverUrl
)