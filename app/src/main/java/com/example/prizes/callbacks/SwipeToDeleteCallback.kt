package com.example.prizes.callbacks

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.prizes.R

abstract class SwipeToDeleteCallback(context: Context) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(
            context, R.drawable.ic_baseline_delete_24)?.also {
                it.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                        Color.WHITE,
                        BlendModeCompat.SRC_ATOP
                )
    }
    private val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
    private val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor(
            context.resources.getString(0 + R.color.pomegranate))

    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {
        if (viewHolder.absoluteAdapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float,
                             dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        drawDeleteBackground(itemView, dX, canvas)
        drawDeleteIcon(itemView, itemHeight, canvas)

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawDeleteBackground(itemView: View, dX: Float, canvas: Canvas) {
        background.color = backgroundColor
        background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
        )
        background.draw(canvas)
    }

    private fun drawDeleteIcon(itemView: View, itemHeight: Int, canvas: Canvas) {

        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        deleteIcon?.let{
            it.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            it.draw(canvas)
        }
    }
}