package com.mont.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes_table WHERE noteId = :id")
    suspend fun getNoteById(id: Long): Note?

    @Query("SELECT * FROM notes_table ORDER BY note_priority DESC")
    fun getAllNotesByPriorityOrder(): LiveData<List<Note>>

    @Query("DELETE FROM notes_table")
    suspend fun clearAll()

}