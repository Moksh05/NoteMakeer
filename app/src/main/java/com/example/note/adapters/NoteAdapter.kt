package com.example.note.adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.room.Note

class NoteAdapter(val listener: NotesOnClicklistner) :
    RecyclerView.Adapter<NoteAdapter.NoteviewHolder>() {


    private val Notelist = mutableListOf<Note>()
    private val Fulllist = mutableListOf<Note>()

    inner class NoteviewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val notes_layout = view.findViewById<CardView>(R.id.note_item)
        val title = view.findViewById<TextView>(R.id.title)
        val priority = view.findViewById<ImageView>(R.id.priority_color)
        val content = view.findViewById<TextView>(R.id.note_content)
        val date = view.findViewById<TextView>(R.id.note_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteviewHolder {
        return NoteviewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        )
    }

    fun getNoteat(position: Int): Note {
        return Notelist[position]
    }

    override fun onBindViewHolder(holder: NoteviewHolder, position: Int) {
        val currentNote = Notelist[position]
        holder.title.text = currentNote.tittle
        holder.title.isSelected = true
        holder.content.isSelected = true
        holder.content.text = currentNote.content
        holder.date.text = currentNote.date
        Log.d(
            "onBind Note priority",
            "working ${currentNote.id} has the ${currentNote.priority} with data ${currentNote.tittle}"
        )
        when (currentNote.priority) {
            1 -> holder.priority.setBackgroundResource(R.drawable.red)
            2 -> holder.priority.setBackgroundResource(R.drawable.yellow)
            3 -> holder.priority.setBackgroundResource(R.drawable.green)
            0 -> holder.priority.setBackgroundResource(R.drawable.blue)
        }

        holder.notes_layout.setOnClickListener {
            listener.onItemclicked(Notelist[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onlongItemclicked(
                Notelist[holder.adapterPosition],
                holder.notes_layout
            )
            true
        }


    }

    fun filterlistOption(priority: Int) {
        if (priority == -1){
            Notelist.clear()
            Notelist.addAll(Fulllist)
        }else{
            Notelist.clear()

            for (item in Fulllist) {
                if (item.priority == priority
                ) {
                    Notelist.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun filterlist(search: String) {
        Notelist.clear()

        for (item in Fulllist) {
            if (item.tittle?.lowercase()
                    ?.contains(search.lowercase()) == true || item.content?.lowercase()
                    ?.contains(search.lowercase()) == true
            ) {
                Notelist.add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun Updatelist(newList: MutableList<Note>) {
        Fulllist.clear()
        Fulllist.addAll(newList)


        Notelist.clear()
        Notelist.addAll(newList)

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return Notelist.size
    }

    interface NotesOnClicklistner {
        fun onItemclicked(note: Note)
        fun onlongItemclicked(note: Note, cardView: CardView)
    }

}
