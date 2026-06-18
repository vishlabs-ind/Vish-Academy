package com.rach.co.ytLive

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveClassRepository @Inject constructor(){

    private val firestore = FirebaseFirestore.getInstance()

    fun observeLiveClass(
        onUpdate: (LiveClass?) -> Unit
    ) {

        firestore
            .collection("youtube class live")
            .document("yt class live")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e("LIVE_CLASS", "Firestore Listener Error: ${error.message}", error)

                    onUpdate(null)
                    return@addSnapshotListener
                }

                if (snapshot == null ) {

                    Log.d("LIVE_CLASS", "Snapshot is NULL")
                    onUpdate(null)
                    return@addSnapshotListener
                }

                if (!snapshot.exists()) {
                    Log.d("LIVE_CLASS", "Document does not exist")
                    onUpdate(null)
                    return@addSnapshotListener
                }
                Log.d("LIVE_CLASS", "Raw Firestore Data = ${snapshot.data}")
                Log.d("LIVE_CLASS", "isLive raw = ${snapshot.getBoolean("isLive")}")

                val liveClass = LiveClass(
                    isLive = snapshot.getBoolean("isLive") ?: false,
                    link = snapshot.getString("link") ?: "",
                    title = snapshot.getString("title") ?: "",
                    thumbnail = snapshot.getString("thumbnail") ?: ""
                )

                Log.d("LIVE_CLASS", "Mapped Object = $liveClass")

                onUpdate(liveClass)
            }
    }
}