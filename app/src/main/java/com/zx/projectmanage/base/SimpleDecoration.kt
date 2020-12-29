package com.zx.projectmanage.base

import android.R.attr.dividerHeight
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.zx.projectmanage.R
import com.zx.zxutils.util.ZXSystemUtil


class SimpleDecoration(var context: Context, @ColorRes var color: Int = R.color.light_gray, var height: Int = 1) : ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = ContextCompat.getColor(context, color)
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = height //类似加了一个bottom padding
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        val left = parent.paddingLeft + ZXSystemUtil.dp2px(10f)
        val right = parent.width - parent.paddingRight - ZXSystemUtil.dp2px(10f)
        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom.toFloat()
            val bottom = view.bottom + height
            c.drawRect(left.toFloat(), top, right.toFloat(), bottom.toFloat(), paint)
        }
    }
}