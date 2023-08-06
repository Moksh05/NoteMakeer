package com.example.note.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {

    val allNotes : LiveData<MutableList<Note>>
    val repository: NoteRepository

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }

    fun InsertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.Insert(note)
    }
    fun DeleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.Delete(note)
    }
    fun UpdateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.Update(note)
    }
    fun DeleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.DeleteAll()
    }
    fun DeleteNoteAT(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.DeleteNoteAt(note)
    }

}