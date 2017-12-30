package com.perqin.sleeprecord.utils.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.perqin.sleeprecord.R

/**
 * Created on 12/29/17.
 *
 * @author perqin
 */
class DurationView : View {
    var min: Int = 0
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }
    var max: Int = 0
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }
    var durationStart: Int = 0
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }
    var durationEnd: Int = 0
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }
    var color: Int = Color.BLACK
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }
    private val paint = Paint()

    constructor(context: Context) : super(context) {
        initView(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun onDraw(canvas: Canvas) {
        val top = paddingTop
        val bottom = height - paddingBottom
        val left = paddingLeft + (if (min < max && min <= durationStart) {
            1.0F * (durationStart - min) * (width - paddingLeft - paddingRight) / (max - min)
        } else 0F)
        val right = paddingLeft + (if (min < max && min <= durationStart && durationStart <= durationEnd) {
            1.0F * (durationEnd - min) * (width - paddingLeft - paddingRight) / (max - min)
        } else 0F)
        paint.color = color
        canvas.drawRect(left, top.toFloat(), right, bottom.toFloat(), paint)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.DurationView, defStyleAttr, defStyleRes)
        try {
            min = a.getInteger(R.styleable.DurationView_min, 0)
            max = a.getInteger(R.styleable.DurationView_max, 0)
            durationStart = a.getInteger(R.styleable.DurationView_durationStart, 0)
            durationEnd = a.getInteger(R.styleable.DurationView_durationEnd, 0)
            color = a.getColor(R.styleable.DurationView_color, Color.BLACK)
        } finally {
            a.recycle()
        }
        paint.style = Paint.Style.FILL
    }
}
