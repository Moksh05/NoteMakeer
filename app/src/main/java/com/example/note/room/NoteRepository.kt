package com.example.note.room

import android.provider.ContactsContract.Intents.Insert
import androidx.lifecycle.LiveData
import androidx.room.Update

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes : LiveData<MutableList<Note>> = noteDao.getAllNotes()

    suspend fun Insert(note: Note){
        noteDao.Insert(note)
    }

    suspend fun Delete(note: Note){
        noteDao.Delete(note)
    }

    suspend fun DeleteAll(){
        noteDao.DeleteALl()
    }

    suspend fun DeleteNoteAt(note: Note){
        noteDao.DeletenoteAt(note.id)
    }
    suspend fun Update(note: Note){
        noteDao.Update(note.id,note.tittle,note.content,note.priority)
    }

}