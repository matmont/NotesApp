package com.mont.notesapp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mont.notesapp.database.Note
import com.mont.notesapp.database.NoteDao
import kotlinx.coroutines.*

class MainActivityViewModel(private val database: NoteDao): ViewModel() {
    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        Log.i("MainActivityViewModel", "ViewModel initialised")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("MainActivityViewModel", "ViewModel cleared")
        job.cancel()
    }

    val notes = database.getAllNotesByPriorityOrder()
    
    fun insertNote(note: Note) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.insert(note)
            }
        }
    }

    fun deleteNote(note: Note) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.delete(note)
            }
        }
    }

    fun clearNotes() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.clearAll()
            }
        }
    }

    fun updateNote(note: Note) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.update(note)
            }
        }
    }
}