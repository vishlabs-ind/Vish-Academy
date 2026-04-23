package com.rach.co.di

import com.google.firebase.firestore.FirebaseFirestore
import com.rach.co.homescreen.data.RepoImpl.NotesRepoImplemtation
import com.rach.co.homescreen.domain.Repo.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesModule {

    @Singleton
    @Provides
    fun noteRepoProvider(firestore: FirebaseFirestore): NoteRepository {

        return NotesRepoImplemtation(firestore)

    }



}