package com.example.note.room

import android.icu.text.CaseMap.Title
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun Insert (note: Note)

    @Query("UPDATE notes_table SET tittle = :title ,content = :content,priority =:priority WHERE id =:id")
    fun Update(id: Int,title: String,content: String, priority:Int)

    @Delete
    suspend fun Delete(note: Note)

    @Query("DELETE FROM notes_table")
    fun DeleteALl()

    @Query("DELETE FROM notes_table WHERE id = :id")
    fun DeletenoteAt(id:Int)
    @Query(" SELECT * FROM notes_table ORDER BY id ASC")
    fun getAllNotes() : LiveData<MutableList<Note>>
}