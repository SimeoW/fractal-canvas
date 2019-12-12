package com.simewu.fractalcanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast


class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var fractal: Fractal = Fractal()

    private val paint =
        Paint().apply {
            isAntiAlias = false
            color = Color.RED
        }

    private var xoff_ = 1.toDouble()
    private var yoff_ = 1.toDouble()
    private var _xoff = 1.toDouble()
    private var _yoff = 1.toDouble()
    private var __xoff = 1.toDouble()
    private var __yoff = 1.toDouble()

    private val INVALID_POINTER_ID = -1
    private var mPosX: Double = 0.toDouble()
    private var mPosY: Double = 0.toDouble()
    private var mLastTouchX: Double = 0.toDouble()
    private var mLastTouchY: Double = 0.toDouble()
    private var mLastGestureX: Double = 0.toDouble()
    private var mLastGestureY: Double = 0.toDouble()
    private var mActivePointerId = INVALID_POINTER_ID
    private var mScaleDetector = ScaleGestureDetector(getContext(), ScaleListener())
    private var mScaleFactor = 1.toDouble()


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev)

        val action = ev.action
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScaleDetector.isInProgress) {
                    val x = ev.x
                    val y = ev.y

                    mLastTouchX = x.toDouble()
                    mLastTouchY = y.toDouble()
                    mActivePointerId = ev.getPointerId(0)
                }
            }
            MotionEvent.ACTION_POINTER_1_DOWN -> {
                if (mScaleDetector.isInProgress) {
                    val gx = mScaleDetector.focusX
                    val gy = mScaleDetector.focusY
                    mLastGestureX = gx.toDouble()
                    mLastGestureY = gy.toDouble()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress) {
                    val pointerIndex = ev.findPointerIndex(mActivePointerId)
                    val x = ev.getX(pointerIndex)
                    val y = ev.getY(pointerIndex)
                    val dx = x - mLastTouchX
                    val dy = y - mLastTouchY
                    mPosX += dx
                    mPosY += dy
                    invalidate()
                    mLastTouchX = x.toDouble()
                    mLastTouchY = y.toDouble()
                } else {
                    val gx = mScaleDetector.focusX
                    val gy = mScaleDetector.focusY
                    val gdx = gx - mLastGestureX
                    val gdy = gy - mLastGestureY
                    mPosX += gdx
                    mPosY += gdy
                    invalidate()
                    mLastGestureX = gx.toDouble()
                    mLastGestureY = gy.toDouble()
                }
            }
            MotionEvent.ACTION_UP -> {
                mActivePointerId = INVALID_POINTER_ID
            }
            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = INVALID_POINTER_ID
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex =
                    ev.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mLastTouchX = ev.getX(newPointerIndex).toDouble()
                    mLastTouchY = ev.getY(newPointerIndex).toDouble()
                    mActivePointerId = ev.getPointerId(newPointerIndex)
                } else {
                    val tempPointerIndex = ev.findPointerIndex(mActivePointerId)
                    mLastTouchX = ev.getX(tempPointerIndex).toDouble()
                    mLastTouchY = ev.getY(tempPointerIndex).toDouble()
                }
            }
        }
        __xoff = _xoff
        __yoff = _yoff
        _xoff = mLastGestureX - mPosX
        _yoff = mLastGestureY - mPosY
        xoff_ += (_xoff - __xoff) / fractal.scale
        yoff_ += (_yoff - __yoff) / fractal.scale
        //fractal.xoff = (fractal.xoff * 1 + xoff_) / 2
        //fractal.yoff = (fractal.yoff * 1 + yoff_) / 2
        //fractal.scale = (fractal.scale * 3 + mScaleFactor) / 4
        fractal.xoff = xoff_
        fractal.yoff = yoff_
        fractal.scale = mScaleFactor

        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            //Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();
            //mScaleFactor = Math.max(0.1.toDouble(), Math.min(mScaleFactor, 1.toDouble()))
            invalidate()
            return true
        }
    }

    // Called when the view should render its content.
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        render(canvas, 20)
    }

    fun render(canvas: Canvas, resolution: Int) {
        var _x : Double
        var _y : Double
        var maxWH = Math.max(canvas.width, canvas.height)
        for (y in 0 until canvas.height step resolution) {
            for (x in 0 until canvas.width step resolution) {
                _x = ((x - canvas.width / 2) / fractal.scale  + fractal.xoff) / maxWH * 8
                _y = ((y - canvas.height / 2) / fractal.scale + fractal.yoff) / maxWH * 8
                paint.color = getColor(_x, _y)
                canvas.drawRect(
                    x.toFloat(), y.toFloat(),
                    (x + resolution).toFloat(), (y + resolution).toFloat(), paint
                )
            }
        }
        paint.color = Color.WHITE
        canvas.drawLine(
            (canvas.width / 2).toFloat(), 0f,
            (canvas.width / 2).toFloat(), canvas.height.toFloat(),
            paint
        )
        canvas.drawLine(
            0f, canvas.height / 2f,
            canvas.width.toFloat(), canvas.height / 2f,
            paint
        )
    }

    private fun print(msg: String) {
        //com.simewu.fractalcanvas.scaleFactorText.setText(msg)
        Log.d("simeon", msg)
    }

    fun getColor(x: Double, y: Double): Int {
        var num = fractal.eval(x, y)

        if(num == fractal.iterations) {
            return Color.WHITE
        } else if(num == 1){
            return Color.GRAY
        } else if(num == 2){
            return Color.BLUE
        } else {
            return Color.BLACK
        }

        var hue = ((num * 100) % 360 + 360) % 360
        var sat = 1
        var value = 1

        return Color.HSVToColor(floatArrayOf(hue.toFloat(), sat.toFloat(), value.toFloat()))
    }
}