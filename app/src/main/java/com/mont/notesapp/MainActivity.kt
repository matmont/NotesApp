package com.mont.notesapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.mont.notesapp.database.Note
import com.mont.notesapp.database.NoteDatabase
import com.mont.notesapp.databinding.ActivityMainBinding
import com.mont.notesapp.recyclerview.NoteAdapter
import com.mont.notesapp.recyclerview.NotesRecyclerViewScrollListener
import com.mont.notesapp.recyclerview.SwipeToDeleteCallback

const val NEW_EDIT_NOTE_REQUEST_CODE = 1
const val NOTE_TITLE_KEY = "note_title"
const val NOTE_MESSAGE_KEY = "note_message"
const val NOTE_PRIORITY_KEY = "note_priority"
const val NOTE_ID_KEY = "note_id"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        /* View Model */
        val viewModelFactory = MainActivityViewModelFactory(NoteDatabase.getInstance(this).noteDatabaseDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        viewModel.clearNotes()
        for (i in 0..10) {
            val note = Note()
            note.noteTitle = "Title $i"
            note.noteMessage = "Message $i"
            note.notePriority = 1
            viewModel.insertNote(note)
        }
        /* Recycler View */
        val recyclerView = binding.notesRecyclerView
        adapter = NoteAdapter {note -> onListItemClick(note)}
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(NotesRecyclerViewScrollListener(binding.fabAddNote))
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(binding.fabAddNote,this, viewModel))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        viewModel.notes.observe(this, { newList ->
            adapter.submitList(newList)
        })

        binding.fabAddNote.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fab_add_note -> {
                val intent = Intent(this, NewEditNoteActivity::class.java)
                startActivityForResult(intent, NEW_EDIT_NOTE_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            NEW_EDIT_NOTE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        var isForEditing = false
                        var noteId: Long? = null
                        if (it.hasExtra(NOTE_ID_KEY)) {
                            isForEditing = true
                            noteId = it.getLongExtra(NOTE_ID_KEY, -1)
                        }
                        val title = it.getStringExtra(NOTE_TITLE_KEY)
                        val message = it.getStringExtra(NOTE_MESSAGE_KEY)
                        val priority = it.getIntExtra(NOTE_PRIORITY_KEY, 1)
                        val note = Note()
                        note.noteTitle = title!!
                        note.noteMessage = message!!
                        note.notePriority = priority

                        if (isForEditing) {
                            note.noteId = noteId!!
                            viewModel.updateNote(note)
                        } else {
                            viewModel.insertNote(note)
                        }


                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Snackbar.make(binding.fabAddNote, R.string.new_note_error_snackbar_label, Snackbar.LENGTH_SHORT).setAnchorView(binding.fabAddNote).show()
                }
            }
        }
    }

    private fun onListItemClick(note: Note){
        val intent = Intent(this, NewEditNoteActivity::class.java)
        intent.putExtra(NOTE_ID_KEY, note.noteId)
        intent.putExtra(NOTE_TITLE_KEY, note.noteTitle)
        intent.putExtra(NOTE_MESSAGE_KEY, note.noteMessage)
        intent.putExtra(NOTE_PRIORITY_KEY, note.notePriority)
        startActivityForResult(intent, NEW_EDIT_NOTE_REQUEST_CODE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.main_menu_clear_all -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete all notes?")
                    .setMessage("With this action you'll lost all the saved notes.")
                    .setNegativeButton("CANCEL") { _, _ ->
                        Snackbar.make(binding.fabAddNote, R.string.new_note_error_snackbar_label, Snackbar.LENGTH_SHORT).setAnchorView(binding.fabAddNote).show()
                        return@setNegativeButton
                    }
                    .setPositiveButton("CONFIRM") {_, _ ->
                        viewModel.clearNotes()
                        Snackbar.make(binding.fabAddNote, "All notes deleted", Snackbar.LENGTH_SHORT).setAnchorView(binding.fabAddNote).show()
                        return@setPositiveButton
                    }
                    .show()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}