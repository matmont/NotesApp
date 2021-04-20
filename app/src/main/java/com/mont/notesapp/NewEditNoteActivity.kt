package com.mont.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import com.mont.notesapp.databinding.ActivityNewEditNoteBinding

class NewEditNoteActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding:ActivityNewEditNoteBinding
    private var isForEditing: Boolean = false
    private var noteId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.newNoteButton.setOnClickListener(this)

        // Check if that Activity is launched for editing a Note
        if (intent.hasExtra(NOTE_ID_KEY)) {
            isForEditing = true
            noteId = intent.getLongExtra(NOTE_ID_KEY, -1)
            val title = intent.getStringExtra(NOTE_TITLE_KEY)
            val message = intent.getStringExtra(NOTE_MESSAGE_KEY)
            val priority = intent.getIntExtra(NOTE_PRIORITY_KEY, -1)
            binding.newNoteTitleTextInputField.text = SpannableStringBuilder(title)
            binding.newNoteMessageTextInputField.text = SpannableStringBuilder(message)
            binding.newNotePrioritySlider.value = priority.toFloat()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.new_note_button -> {
                var allOk = true
                // Check if title is inserted
                val title = binding.newNoteTitleTextInputField.text.toString()
                if (title == "") {
                    allOk = false
                    binding.newNoteTitleTextInputLayout.error = getString(R.string.new_edit_note_title_error_message)
                } else {
                    binding.newNoteTitleTextInputLayout.error = null
                }

                // Check if message is inserted
                val message = binding.newNoteMessageTextInputField.text.toString()
                if (message == "" || message.length > binding.newNoteMessageTextInputLayout.counterMaxLength) {
                    allOk = false
                    binding.newNoteMessageTextInputLayout.error = getString(R.string.new_edit_note_message_error_message)
                } else {
                    binding.newNoteMessageTextInputLayout.error = null
                }

                // Retrieve priority
                val priority = binding.newNotePrioritySlider.value.toInt()

                if (allOk) {
                    val intent = Intent()
                    if (isForEditing) {
                        intent.putExtra(NOTE_ID_KEY, noteId)
                    }
                    intent.putExtra(NOTE_TITLE_KEY, title)
                    intent.putExtra(NOTE_MESSAGE_KEY, message)
                    intent.putExtra(NOTE_PRIORITY_KEY, priority)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}