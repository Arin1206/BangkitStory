package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.loginwithanimation.R

class MyRegister : AppCompatButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var txt: Int = 0
    private var enabled: Drawable
    private var disabled: Drawable

    init {
        txt = ContextCompat.getColor(context, android.R.color.background_light)
        enabled = ContextCompat.getDrawable(context, R.drawable.button) as Drawable
        disabled = ContextCompat.getDrawable(context, R.drawable.button_disabled) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabled else disabled
        setTextColor(txt)
        textSize = 18f
        gravity = Gravity.CENTER
        text = if (isEnabled) "Register" else "Required"
    }
}