package com.mont.notesapp.custompriorityview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.mont.notesapp.R

class PriorityView(context: Context, attrs: AttributeSet): View(context, attrs) {
    var itemPriority: Int = 1
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var shadowPaint = Paint(0).apply {
        color = Color.argb(250, 80, 80, 80)
        maskFilter = BlurMaskFilter(2f, BlurMaskFilter.Blur.NORMAL)
    }

    private var priorityIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(255, 119, 0)
        style = Paint.Style.FILL
    }

    private var priorityEmptyIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(0, 71, 119)
        style = Paint.Style.FILL
    }

    private var priorityIndicatorRadiusRatio = 0.1

    // Retrieving attributes from XML declaration
    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.PriorityView, 0, 0).apply {
            try {
                itemPriority = getInteger(R.styleable.PriorityView_item_priority, 1)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val quarter = width/3
        canvas?.apply {
            for (i in 0..2) {
                drawCircle((quarter/2 + i*quarter).toFloat(), height/2.toFloat() + height/400, width*priorityIndicatorRadiusRatio.toFloat(), shadowPaint)
                if (i+1 <= itemPriority) {
                    drawCircle((quarter/2 + i*quarter).toFloat(), height/2.toFloat(), width*priorityIndicatorRadiusRatio.toFloat(), priorityIndicatorPaint)
                } else {
                    drawCircle((quarter/2 + i*quarter).toFloat(), height/2.toFloat(), width*priorityIndicatorRadiusRatio.toFloat(), priorityEmptyIndicatorPaint)
                }

            }

        }
    }


}