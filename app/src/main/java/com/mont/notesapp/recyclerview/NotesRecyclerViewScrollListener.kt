package com.mont.notesapp.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class NotesRecyclerViewScrollListener(private val extended_fab:ExtendedFloatingActionButton): RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        // Log.i("ScrollListener", "Scroll vertical direction: $dy")
        if (dy > 0) {
            extended_fab.shrink()
        } else if (dy < 0) {
            extended_fab.extend()
        }
    }
}