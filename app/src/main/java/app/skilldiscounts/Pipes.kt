package app.skilldiscounts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class Pipe : View {
    var pipeX = 1000f // Initial position
    var width = 200f
    var height = 0f
    var hasScored = false
    private var isTopPipe: Boolean = false
    private val gap = 400f

    private var screenHeight = 1500f

    private val paint = Paint().apply {
        color = Color.GREEN
    }

    constructor(context: Context, isTopPipe: Boolean) : super(context) {
        this.isTopPipe = isTopPipe
        initializeHeight()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        isTopPipe = false
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        isTopPipe = false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenHeight = h.toFloat()
    }

    private fun initializeHeight() {
        height = if (isTopPipe) 400f else 400f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isTopPipe) {
            canvas.drawRect(pipeX, 0f, pipeX + width, height, paint)
        } else {
            canvas.drawRect(pipeX, screenHeight - height, pipeX + width, screenHeight, paint)
        }
    }

    fun movePipe() {
        pipeX -= 10f
        invalidate()
    }

    fun getBounds(): Rect {
        return if (isTopPipe) {
            Rect(pipeX.toInt(), 0, (pipeX + width).toInt(), height.toInt())
        } else {
            Rect(pipeX.toInt(), (screenHeight - height).toInt(), (pipeX + width).toInt(), screenHeight.toInt())
        }
    }
}