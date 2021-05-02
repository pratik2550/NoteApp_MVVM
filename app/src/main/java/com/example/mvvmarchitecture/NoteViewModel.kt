package com.example.mvvmarchitecture

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(application: Application): AndroidViewModel(application) {

    private val allNotes: LiveData<List<Note>>
    private val repository: NoteRepository

    init {
        val dao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }

    suspend fun insert(note: Note) {
        repository.insert(note)
    }

    suspend fun update(note: Note) {
        repository.update(note)
    }

    suspend fun delete(note: Note) {
        repository.delete(note)
    }

    suspend fun deleteAllNotes() {
        repository.deleteAll()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }
}