package com.mont.notesapp.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mont.notesapp.R
import com.mont.notesapp.custompriorityview.PriorityView
import com.mont.notesapp.database.Note

class NoteAdapter(private var onItemClickCallback: (Note) -> Unit): ListAdapter<Note, NoteAdapter.ViewHolder>(NoteDiffCallback()) {

    class ViewHolder(view: View, onItemClickCallback: (Note) -> Unit): RecyclerView.ViewHolder(view) {
        private var currentNote:Note? = null
        private val noteTitle: TextView = view.findViewById(R.id.list_item_title)
        private val priorityIndicator: PriorityView = view.findViewById(R.id.priority_indicator)

        init {
            view.setOnClickListener {
                currentNote?.let {
                    onItemClickCallback(it)
                }
            }
        }

        fun bind(note: Note) {
            currentNote = note
            noteTitle.text = note.noteTitle
            priorityIndicator.itemPriority = note.notePriority
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
        return ViewHolder(view, onItemClickCallback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class NoteDiffCallback:DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

}