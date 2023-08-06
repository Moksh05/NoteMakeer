package com.example.note

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.note.Constants.Constants
import com.example.note.adapters.NoteAdapter
import com.example.note.databinding.ActivityMainBinding

import com.example.note.room.Note
import com.example.note.room.NoteDatabase
import com.example.note.room.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), NoteAdapter.NotesOnClicklistner {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NoteAdapter
    private lateinit var viewModel: NoteViewModel
    private lateinit var database: NoteDatabase

    private val UpdateNote =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Constants.UPDATE_REQ) {
                val title = result.data?.getStringExtra(Constants.Tittle)
                val content = result.data?.getStringExtra(Constants.Content)
                val date = result.data?.getStringExtra(Constants.Date)
                val priority = result.data?.getIntExtra(Constants.Priority.toString(), 0)
                val id = result.data?.getIntExtra(Constants.id.toString(), 0)
                val choicedel = result.data?.getBooleanExtra(Constants.Delete.toString(),false)
                if (title != null && content != null && date != null && priority != null) {
                    val note = Note(title, content, date, priority)
                    note.id = id!!
                    if(choicedel!!){
                        viewModel.DeleteNote(note)
                    }else{
                        viewModel.UpdateNote(note)
                    }

                }
            } else {
                Log.e("DataError", "Some data was missing or null ")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        viewModel.allNotes.observe(this) { list ->
            if (list != null) {
                adapter.Updatelist(list)
            }
        }

        database = NoteDatabase.getDatabase(this)


    }

    private fun initializeUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(this)
        binding.recyclerView.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Log.d("beforefinish3", "working")
                if (result.resultCode == Activity.RESULT_OK) {
                    val title = result.data?.getStringExtra(Constants.Tittle)

                    val content = result.data?.getStringExtra(Constants.Content)
                    val date = result.data?.getStringExtra(Constants.Date)
                    val priority = result.data?.getIntExtra(Constants.Priority.toString(), 0)

                    if (title != null && content != null && date != null && priority != null) {
                        val note = Note(title, content, date, priority)
                        try {
                            viewModel.InsertNote(note)
                            Log.d(
                                "workingafterreceiving3",
                                "working $title $content $priority $date"
                            )
                        } catch (e: Exception) {
                            Log.e("InsertNoteError", "Error inserting note: ${e.message}")
                        }
                    } else {
                        Log.e(
                            "DataError",
                            "Some data was missing or null $title $content $priority $date"
                        )
                    }


                }
            }

        binding.addButton.setOnClickListener {
            val intent = Intent(this, add_edit_notes::class.java)
            Log.d("beforefinish2", "working")
            getContent.launch(intent)

        }

        binding.closeFilter.setOnClickListener {
            binding.closeFilter.isVisible = false
            adapter.filterlistOption(-1)
            binding.filterGreen.setBackgroundResource(R.drawable.white)
            binding.filterRed.setBackgroundResource(R.drawable.white)
            binding.filterYellow.setBackgroundResource(R.drawable.white)
            binding.filterBlue.setBackgroundResource(R.drawable.white)
        }
        binding.filterBlue.setOnClickListener {
            binding.filterBlue.setBackgroundResource(R.drawable.whiteselected)
            binding.filterBlue.setTypeface(Typeface.DEFAULT_BOLD)
            binding.filterGreen.setBackgroundResource(R.drawable.white)
            binding.filterRed.setBackgroundResource(R.drawable.white)
            binding.filterYellow.setBackgroundResource(R.drawable.white)
            binding.closeFilter.isVisible = true

            adapter.filterlistOption(0)
        }
        binding.filterGreen.setOnClickListener {
            binding.filterBlue.setBackgroundResource(R.drawable.white)
            binding.filterGreen.setBackgroundResource(R.drawable.whiteselected)
            binding.filterGreen.setTypeface(Typeface.DEFAULT_BOLD)
            binding.filterRed.setBackgroundResource(R.drawable.white)
            binding.filterYellow.setBackgroundResource(R.drawable.white)
            binding.closeFilter.isVisible = true

            adapter.filterlistOption(3)
        }
        binding.filterRed.setOnClickListener {
            binding.filterBlue.setBackgroundResource(R.drawable.white)
            binding.filterGreen.setBackgroundResource(R.drawable.white)
            binding.filterRed.setBackgroundResource(R.drawable.whiteselected)
            binding.filterRed.setTypeface(Typeface.DEFAULT_BOLD)
            binding.filterYellow.setBackgroundResource(R.drawable.white)
            binding.closeFilter.isVisible = true

            adapter.filterlistOption(1)
        }
        binding.filterYellow.setOnClickListener {
            binding.filterBlue.setBackgroundResource(R.drawable.white)
            binding.filterGreen.setBackgroundResource(R.drawable.white)
            binding.filterRed.setBackgroundResource(R.drawable.white)
            binding.filterYellow.setBackgroundResource(R.drawable.whiteselected)
            binding.filterYellow.setTypeface(Typeface.DEFAULT_BOLD)
            binding.closeFilter.isVisible = true

            adapter.filterlistOption(2)
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    adapter.filterlist(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterlist(newText)
                }
                return true
            }

        })



        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val removeditem = adapter.getNoteat(viewHolder.adapterPosition)
                viewModel.DeleteNote(adapter.getNoteat(viewHolder.adapterPosition))

                Snackbar.make(parent, binding.recyclerView, "Note Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO",
                        View.OnClickListener {
                            viewModel.InsertNote(removeditem)
                        }).show()
            }

        }).attachToRecyclerView(binding.recyclerView)
    }

    override fun onItemclicked(note: Note) {
        Log.d("noteclicked", "working")
        val intent = Intent(this, add_edit_notes::class.java)
        intent.putExtra(Constants.Tittle, note.tittle)
        intent.putExtra(Constants.Content, note.content)
        intent.putExtra(Constants.Date, note.date)
        intent.putExtra(Constants.Priority.toString(), note.priority)
        Log.d("noteclicked priority", "working ${note.priority}")
        intent.putExtra(Constants.id.toString(), note.id)
        Log.d("noteclicked id", "working ${note.id}")
        UpdateNote.launch(intent)
    }

    override fun onlongItemclicked(note: Note, cardView: CardView) {
        Toast.makeText(
            this,
            "Has to add functionality yet On long touch open dialog of delete all and delete this with bottom dialog thing\nand has to set the delte note from add edit acitivity",
            Toast.LENGTH_SHORT
        ).show()

        val Bottomsheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.main_delete_dialog,null)
        Bottomsheet.setContentView(view)
        Bottomsheet.show()

        val thisnote = view.findViewById<Button>(R.id.delete_thisnote)
        val allnote = view.findViewById<Button>(R.id.delete_allNotes)

        thisnote.setOnClickListener {
            viewModel.DeleteNote(note)
            Bottomsheet.dismiss()
        }
        allnote.setOnClickListener {
            viewModel.DeleteAll()
            Bottomsheet.dismiss()
        }

        Bottomsheet.setContentView(view)
        Bottomsheet.show()
    }


}




