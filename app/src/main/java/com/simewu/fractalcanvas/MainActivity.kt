package com.simewu.fractalcanvas

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.max


class MainActivity : AppCompatActivity() {

    private var fractalBackup: Fractal = Fractal()
    fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
    fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toggleMenu()

        val button = findViewById<FloatingActionButton>(R.id.menuButton)
        button.setOnClickListener {
            toggleMenu()
        }
        (findViewById<ImageView>(R.id.renderBtn)).setOnTouchListener { v: View, event -> optionTouch(v, event) }
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
        /*if (menu.translationY == 0f) {
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
        }*/
        val canvas = findViewById<CustomView>(R.id.canvas)
        fractalBackup = canvas.fractal.clone()

        if(v.id == R.id.renderBtn) {
            val tempResolution = canvas.fractal.resolution
            canvas.fractal.resolution = 1
            canvas.invalidate()
            canvas.fractal.resolution = tempResolution

        } else if(v.id == R.id.presetsBtn) {
            Toast.makeText(applicationContext, "Feature not implemented. Check back soon.", Toast.LENGTH_LONG).show()

        } else if(v.id == R.id.equationBtn) {
            Toast.makeText(applicationContext, "Feature not implemented. Check back soon.", Toast.LENGTH_LONG).show()

        } else if(v.id == R.id.detailBtn) {
            makeSliderDialog("Detail", canvas.fractal.detail * -2.0 - 1, canvas.fractal.detail * 2.0 + 1, canvas.fractal.detail.toDouble(), 1000) { n ->
                canvas.fractal.detail = n
                canvas.invalidate()
            }
        } else if(v.id == R.id.resolutionBtn) {
            makeSliderDialog("Resolution", 1.0, max(100, canvas.fractal.resolution * 2).toDouble(), canvas.fractal.resolution.toDouble(), 100) { n ->
                canvas.fractal.resolution = n
                canvas.invalidate()
            }

        } else if(v.id == R.id.iterationsBtn) {
            makeSliderDialog("Iterations", 2.0, max(100, canvas.fractal.iterations * 2).toDouble(), canvas.fractal.iterations.toDouble(), 1) { n ->
                canvas.fractal.iterations = n
                canvas.invalidate()
            }
        } else if(v.id == R.id.colorBtn) {
            Toast.makeText(applicationContext, "Feature not implemented. Check back soon.", Toast.LENGTH_LONG).show()

        } else if(v.id == R.id.saveBtn) {
            Toast.makeText(applicationContext, "Feature not implemented. Check back soon.", Toast.LENGTH_LONG).show()

        } else if(v.id == R.id.exportBtn) {
            Toast.makeText(applicationContext, "Feature not implemented. Check back soon.", Toast.LENGTH_LONG).show()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    // From, to, initial value, precision (100 = two decimal places), lambda function to handle the change
    fun makeSliderDialog(title: String, from_: Double, to_: Double, value_: Double, precision: Int, func: (n: Int) -> Unit) {
        var from = (from_ * precision).toInt()
        var to = (to_ * precision).toInt()
        var value = (value_ * precision).toInt()

        val alert = AlertDialog.Builder(this)
        alert.setCancelable(false)
        alert.setTitle(title)
        val linear = LinearLayout(this)
        linear.orientation = LinearLayout.VERTICAL
        linear.setPadding(50, 50, 50, 50)

        val editText = EditText(this)
        editText.setRawInputType(Configuration.KEYBOARDHIDDEN_YES)
        editText.setEms(4)
        editText.layoutParams = FrameLayout.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT)
        editText.gravity = Gravity.CENTER

        val seekBar = SeekBar(this)
        seekBar.min = from
        seekBar.max = to

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                var n = 0
                try {
                    n = (editText.text.toString().toDouble() * precision).toInt()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Unknown number.", Toast.LENGTH_LONG).show()
                    return
                }
                if(n < from) return
                if(n > to) seekBar.max = n
                seekBar.progress = n
                editText.setSelection(editText.text.toString().length)

                func(n / precision)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) { // TODO Auto-generated method stub
                editText.setText((progress / precision.toDouble()).toString())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        editText.setText((value / precision.toFloat()).toString())


        linear.addView(seekBar)
        linear.addView(editText)
        alert.setView(linear)

        alert.setPositiveButton("Ok"){ dialog, id -> }
        alert.setNegativeButton("Cancel") { dialog, id -> }

        val dialog = alert.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.argb(128, 255,255, 255)))

        val positiveBtn =
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveBtn.setOnClickListener {
            Toast.makeText(applicationContext, "OK Pressed", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
        val negativeBtn =
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeBtn.setOnClickListener {
            val canvas = findViewById<CustomView>(R.id.canvas)
            canvas.fractal = fractalBackup.clone()
            canvas.invalidate()
            dialog.dismiss()
        }

    }
}
