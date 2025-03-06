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
    var height = 400f
    var hasScored = false
    private var isTopPipe: Boolean = false

    private val paint = Paint().apply {
        color = Color.GREEN
    }

    constructor(context: Context, isTopPipe: Boolean) : super(context) {
        this.isTopPipe = isTopPipe
        height = if (isTopPipe) (300..700).random().toFloat() else 1500f - (300..700).random().toFloat()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        isTopPipe = false
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        isTopPipe = false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isTopPipe) {
            canvas.drawRect(pipeX, 0f, pipeX + width, height, paint) // Top pipe
        } else {
            canvas.drawRect(pipeX, height + 300, pipeX + width, 1500f, paint) // Bottom pipe
        }
    }

    fun movePipe() {
        pipeX -= 10f
        if (pipeX + width < 0) {
            pipeX = 1000f
            height = if (isTopPipe) (300..700).random().toFloat() else 1500f - (300..700).random().toFloat()
            hasScored = false
        }
        invalidate()
    }

    fun getBounds(): Rect {
        return if (isTopPipe) {
            Rect(pipeX.toInt(), 0, (pipeX + width).toInt(), height.toInt())
        } else {
            Rect(pipeX.toInt(), (height + 300).toInt(), (pipeX + width).toInt(), 1500)
        }
    }
}
