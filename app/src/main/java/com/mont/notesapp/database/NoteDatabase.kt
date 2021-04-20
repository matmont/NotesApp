package com.mont.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1,  exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract val noteDatabaseDao: NoteDao

    companion object {
        // Volatile make sure that INSTANCE is always up to date for all threads (never be cached)
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "notes_database").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}