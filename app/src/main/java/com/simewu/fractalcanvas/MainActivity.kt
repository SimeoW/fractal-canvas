package com.simewu.fractalcanvas

import android.content.res.Resources
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
    fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toggleMenu()
        toggleMenu()

        val button = findViewById<FloatingActionButton>(R.id.menuButton)
        button.setOnClickListener {
            toggleMenu()
        }
        (findViewById<ImageView>(R.id.presetsBtn)).setOnTouchListener { v: View, event -> optionTouch(v, event) }
        (findViewById<ImageView>(R.id.resolutionBtn)).setOnTouchListener { v: View, event -> optionTouch(v, event) }
        (findViewById<ImageView>(R.id.equationBtn)).setOnTouchListener { v: View, event -> optionTouch(v, event) }
        (findViewById<ImageView>(R.id.detailBtn)).setOnTouchListener { v: View, event -> optionTouch(v, event) }
        (findViewById<ImageView>(R.id.iterationsBtn)).setOnTouchListener { v: View, event -> optionTouch(v, event) }
        (findViewById<ImageView>(R.id.colorBtn)).setOnTouchListener { v: View, event -> optionTouch(v, event) }
        (findViewById<ImageView>(R.id.saveBtn)).setOnTouchListener { v: View, event -> optionTouch(v, event) }
        (findViewById<ImageView>(R.id.exportBtn)).setOnTouchListener { v: View, event -> optionTouch(v, event) }
    }

    private fun optionTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.animate()
                    .alpha(0.5f)
                    .translationY(-60.toDp().toFloat())
                    .setDuration(100)
            }
            MotionEvent.ACTION_UP -> {
                v.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(100)
                    .start()
                optionSelect(v)
            }
            MotionEvent.ACTION_CANCEL -> {
                v.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(100)
                    .start()
            }
        }

        return true
    }

    private fun toggleMenu() {
        val button = findViewById<FloatingActionButton>(R.id.menuButton)
        val menu = findViewById<HorizontalScrollView>(R.id.menuBar)
        if (menu.translationY == 0f) {
            menu.animate()
                .translationY(menu.height.toFloat())
                .setDuration(300)
                .start()
            button.animate()
                .translationY(menu.height.toFloat())
                .setDuration(300)
                .start()
        } else {
            menu.animate()
                .translationY(0f)
                .setDuration(300)
                .start()
            button.animate()
                .translationY(0f)
                .setDuration(300)
                .start()
        }
    }

    fun optionSelect(v: View) {
        val button = findViewById<FloatingActionButton>(R.id.menuButton)
        val menu = findViewById<HorizontalScrollView>(R.id.menuBar)
        if (menu.translationY == 0f) {
            menu.animate()
                .translationY(menu.height.toFloat())
                .setDuration(300)
                .start()
            button.animate()
                .translationY(menu.height.toFloat())
                .setDuration(300)
                .start()
        } else {
            menu.animate()
                .translationY(0f)
                .setDuration(300)
                .start()
            button.animate()
                .translationY(0f)
                .setDuration(300)
                .start()
        }
    }
}
