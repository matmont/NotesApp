package com.mont.notesapp.recyclerview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.mont.notesapp.MainActivityViewModel
import com.mont.notesapp.R
import com.mont.notesapp.database.Note

class SwipeToDeleteCallback(private val floatingActionButton: ExtendedFloatingActionButton, context: Context, private val viewModel:MainActivityViewModel):ItemTouchHelper.SimpleCallback(0, (ItemTouchHelper.LEFT)) {

    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)
    private val deleteIconIntrinsicWidth = deleteIcon!!.intrinsicWidth
    private val deleteIconIntrinsicHeight = deleteIcon!!.intrinsicHeight
    private var snackbar: Snackbar =
        Snackbar.make(floatingActionButton, R.string.note_deleted_snackbar_label, Snackbar.LENGTH_LONG)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val oldNote = viewModel.notes.value!![position]
        val id = oldNote.noteId
        val title = oldNote.noteTitle
        val message = oldNote.noteMessage
        val priority = oldNote.notePriority

        viewModel.deleteNote(viewModel.notes.value!![position])

        snackbar.setAction(R.string.new_note_success_snackbar_action_label) {
            val newNote = Note()
            newNote.noteId = id
            newNote.noteTitle = title
            newNote.noteMessage = message
            newNote.notePriority = priority
            viewModel.insertNote(newNote)
        }
        snackbar.anchorView = floatingActionButton
        snackbar.show()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        Log.i("SwipeToDeleteCallback", "onChildDraw --- dx:$dX - dy:$dY")
        Log.i("SwipeToDeleteCallback", "itemView boundaries --- top")
        val itemView = viewHolder.itemView
        val isCanceled = dX == 0f && !isCurrentlyActive
        val itemHeight = itemView.bottom - itemView.top

        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }
        // Draw the red delete background
        background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - deleteIconIntrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - deleteIconIntrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - deleteIconIntrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + deleteIconIntrinsicHeight

        // Draw the delete icon
        deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}