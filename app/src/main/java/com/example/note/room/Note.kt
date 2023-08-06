package com.example.note.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes_table")
data class Note(
    var tittle: String,
    var content: String,
    var date: String,
    var priority: Int
):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
