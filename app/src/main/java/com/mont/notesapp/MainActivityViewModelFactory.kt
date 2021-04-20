package com.mont.notesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mont.notesapp.database.NoteDao
import java.lang.IllegalArgumentException

class MainActivityViewModelFactory(private val database: NoteDao):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}