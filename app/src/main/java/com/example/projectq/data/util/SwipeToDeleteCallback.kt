package com.example.projectq.data.util

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDeleteCallback(
    private val backgroundColor: Int,
    private val icon: Drawable?
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        // Create a new ColorDrawable with the background color each time
        val background = ColorDrawable(backgroundColor)
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(canvas)

        // Calculate position of delete icon
        icon?.let {
            val iconHeight = it.intrinsicHeight
            val iconWidth = it.intrinsicWidth
            val iconTop = itemView.top + (itemHeight - iconHeight) / 2
            val iconMargin = (itemHeight - iconHeight) / 2
            val iconLeft = itemView.right - iconMargin - iconWidth
            val iconRight = itemView.right - iconMargin
            val iconBottom = iconTop + iconHeight

            // Draw the delete icon
            it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            it.draw(canvas)
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}