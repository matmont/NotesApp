package com.mont.notesapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note (
    @PrimaryKey(autoGenerate = true) var noteId: Long = 0,
    @ColumnInfo(name = "note_title") var noteTitle: String = "",
    @ColumnInfo(name = "note_message") var noteMessage: String = "",
    @ColumnInfo(name = "note_priority") var notePriority: Int = 1
    )