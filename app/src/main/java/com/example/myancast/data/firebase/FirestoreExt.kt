// data/firebase/FirestoreExt.kt
package com.example.myancast.data.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun <T : Any> QuerySnapshot.toListWithIds(
    clazz: Class<T>,
    withId: (T, String) -> T
): List<T> = documents.mapNotNull { doc ->
    doc.toObject(clazz)?.let { withId(it, doc.id) }
}

fun <T : Any> DocumentSnapshot.toObjectWithId(
    clazz: Class<T>,
    withId: (T, String) -> T
): T? = toObject(clazz)?.let { withId(it, id) }

fun <T : Any> Query.snapshotFlow(
    clazz: Class<T>,
    withId: (T, String) -> T
): Flow<List<T>> = callbackFlow {
    val listener = addSnapshotListener { snapshot, error ->
        if (error != null) {
            close(error)
            return@addSnapshotListener
        }
        trySend(snapshot?.toListWithIds(clazz, withId) ?: emptyList())
    }
    awaitClose { listener.remove() }
}