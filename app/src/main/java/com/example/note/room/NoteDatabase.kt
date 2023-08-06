package com.example.note.room

import android.content.Context
import android.os.Build.VERSION
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Note::class) , version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object{
        private var INSTANCE : NoteDatabase?= null

        fun getDatabase (context: Context): NoteDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,NoteDatabase::class.java,"notes_databse").build()
                INSTANCE = instance
                instance
            }

        }
    }
}