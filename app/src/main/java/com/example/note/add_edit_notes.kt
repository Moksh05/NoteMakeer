package com.example.note

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.note.Constants.Constants
import com.example.note.databinding.ActivityAddEditNotesBinding
import com.example.note.databinding.ActivityMainBinding
import com.example.note.room.Note
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.security.cert.Extension
import java.text.SimpleDateFormat
import java.util.Date

class add_edit_notes : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditNotesBinding
    var activebutton = ""
    var olddate = ""
    var oldid = -1
    var deletenote = false
    var isUpdate = false
    lateinit var tittle: String
    lateinit var content: String
    lateinit var formatter: SimpleDateFormat
    var priority = 0
    var isbold = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.add_edit_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(Constants.id.toString())) {
            Log.d("noteclicked has id", "working")
            oldid = intent.getIntExtra(Constants.id.toString(), 0)
            Log.d("noteclicked has id with", "working ${oldid}")
            olddate = intent.getStringExtra(Constants.Date).toString()
            val oldpriority = intent.getIntExtra(Constants.Priority.toString(), 0)
            Log.d("noteclicked old priority", "working ${oldpriority}")
            binding.editTittle.setText(intent.getStringExtra(Constants.Tittle).toString())
            binding.editText.setText(intent.getStringExtra(Constants.Content).toString())
            when (oldpriority) {
                1 -> setpriorityred()
                2 -> setpriorityyellow()
                3 -> setprioritygreen()
                0 -> setpriorityblue()
            }

            Log.d("noteclicked is updated?", "working $isUpdate")
            isUpdate = true

        } else {
            supportActionBar?.title = "Add Note"
        }

        binding.doneButton.setOnClickListener {
            setValues()
            saveNote()
        }
        binding.redPriority.setOnClickListener {
            setpriorityred()
        }
        binding.bluePriority.setOnClickListener {
            setpriorityblue()
        }
        binding.greenButton.setOnClickListener {
            setprioritygreen()
        }
        binding.yellowPriority.setOnClickListener {
            setpriorityyellow()
        }
        var bold = false
        var italicc = "off"
        var underlinec = "off"
        binding.boldButton.setOnClickListener {

            if (bold) {
                bold = !bold
                binding.boldButton.setBackgroundResource(R.drawable.red2)
                binding.editText.setOnKeyListener { v, keyCode, event ->
                    val inputText = binding.editText.text.toString()

                    // Create a SpannableString with bold formatting
                    val spannable = SpannableString(inputText)
                    val boldSpan = StyleSpan(Typeface.BOLD)
                    spannable.setSpan(boldSpan, 0, spannable.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)


                    // Set the formatted SpannableString back to EditText
                    binding.editText.setText(spannable)
                    false
                }

            } else {
                binding.boldButton.setBackgroundColor(Color.TRANSPARENT)
                bold = !bold
               binding.editText.setOnKeyListener { v, keyCode, event ->
                   val inputText = binding.editText.text.toString()

                   // Create a SpannableString with normal (non-bold) formatting
                   val spannable = SpannableString(inputText)
                   val normalSpan = StyleSpan(Typeface.NORMAL)
                   spannable.setSpan(
                       normalSpan,
                       0,
                       spannable.length,
                       SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                   )

                   // Set the formatted SpannableString back to EditText
                   binding.editText.setText(spannable)

                   false
               }

            }

        }

        binding.underline.setOnClickListener {
            if (underlinec == "off") {
                binding.underline.setBackgroundResource(R.drawable.red2)
//                val inputText = binding.editText.text.toString()
//
//                // Create a SpannableString with underline formatting
//                val spannable = SpannableString(inputText)
//                spannable.setSpan(UnderlineSpan(), 0, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//                // Set the formatted SpannableString back to EditText
//                binding.editText.setText(spannable)
//
//                false
            } else {
                binding.underline.setBackgroundColor(Color.TRANSPARENT)
                binding.editText.setTypeface(Typeface.DEFAULT)
                underlinec = "off"
            }

        }

        binding.italic.setOnClickListener {
            if (italicc == "off") {
                binding.italic.setBackgroundResource(R.drawable.red2)
                binding.editText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC)
                italicc = "on"
            } else {
                binding.italic.setBackgroundColor(Color.TRANSPARENT)
                binding.editText.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
                italicc = "off"
            }

        }
    }


    private fun setpriorityyellow() {
        binding.yellowPriority.setImageResource(R.drawable.done_button)
        binding.bluePriority.setImageResource(0)
        binding.redPriority.setImageResource(0)
        binding.greenButton.setImageResource(0)
        activebutton = "yellow"
    }

    private fun setprioritygreen() {
        binding.greenButton.setImageResource(R.drawable.done_button)
        binding.bluePriority.setImageResource(0)
        binding.yellowPriority.setImageResource(0)
        binding.redPriority.setImageResource(0)
        activebutton = "green"
    }

    private fun setpriorityblue() {
        binding.bluePriority.setImageResource(R.drawable.done_button)
        binding.redPriority.setImageResource(0)
        binding.yellowPriority.setImageResource(0)
        binding.greenButton.setImageResource(0)
        activebutton = "blue"
    }

    private fun setpriorityred() {
        binding.redPriority.setImageResource(R.drawable.done_button)
        binding.bluePriority.setImageResource(0)
        binding.yellowPriority.setImageResource(0)
        binding.greenButton.setImageResource(0)
        activebutton = "red"
    }

    fun setValues() {
        tittle = binding.editTittle.text.trim().toString()
        content = binding.editText.text.trim().toString()
        priority = 0
        formatter = SimpleDateFormat("EEE, d MMM YYYY HH:mm a")
        if (activebutton == "red") {
            priority = 1
        } else if (activebutton == "yellow") {
            priority = 2
        } else if (activebutton == "green") {
            priority = 3
        } else {
            priority = 0
        }

    }

    private fun saveNote() {

        if (tittle.trim().isNotEmpty() and content.trim().isNotEmpty()) {
           if(deletenote){
               setResult(Constants.UPDATE_REQ, Intent().apply {
                   putExtra(Constants.Tittle, tittle)
                   Log.d("beforedeltepassed", "working $tittle")
                   Log.d("beforedeltepassed", "working ${formatter.format(Date())}")
                   putExtra(Constants.Content, content)
                   Log.d("beforedeltepassed", "working $content $tittle")
                   putExtra(Constants.Priority.toString(), priority)
                   Log.d("beforedeltepassed", "working $priority")
                   putExtra(Constants.Date, olddate)
                   putExtra(Constants.id.toString(), oldid)
                   putExtra(Constants.Delete.toString(),true)
               })
           }else if (isUpdate and !deletenote) {
                Log.d("noteclicked for sending", "working")
                setResult(Constants.UPDATE_REQ, Intent().apply {
                    putExtra(Constants.Tittle, tittle)
                    Log.d("beforefinishupdate", "working $tittle")
                    Log.d("beforefinishupdate", "working ${formatter.format(Date())}")
                    putExtra(Constants.Content, content)
                    Log.d("beforefinishupdate", "working $content $tittle")
                    putExtra(Constants.Priority.toString(), priority)
                    Log.d("beforefinishupdate", "working $priority")
                    putExtra(Constants.Date, olddate)
                    putExtra(Constants.id.toString(), oldid)
                    putExtra(Constants.Delete.toString(),false)
                })
            } else {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(Constants.Tittle, tittle)
                    Log.d("beforefinish", "working $tittle")
                    putExtra(Constants.Date, formatter.format(Date()))
                    Log.d("beforefinish", "working ${formatter.format(Date())}")
                    putExtra(Constants.Content, content)
                    Log.d("beforefinish", "working $content $tittle $priority")
                    putExtra(Constants.Priority.toString(), priority)
                })
            }


            Log.d("beforefinish", "working")
            finish()

        } else {
            Toast.makeText(
                this@add_edit_notes,
                "Please Enter Tittle and Description",
                Toast.LENGTH_LONG
            ).show()
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_notes_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_button -> {
                setValues()
                val BottomSheetDialog = BottomSheetDialog(this)
                val view = layoutInflater.inflate(R.layout.delete_dialog,null)

                val yesButton = view.findViewById<Button>(R.id.yes_button)
                val noButton = view.findViewById<Button>(R.id.No_button)

                yesButton.setOnClickListener {
                    deletenote=true
                    saveNote()
                    BottomSheetDialog.dismiss()
                }
                noButton.setOnClickListener {
                    BottomSheetDialog.dismiss()
                }
                BottomSheetDialog.setContentView(view)
                BottomSheetDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }



}