package com.gateway.android.ui

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.TextView
import com.gateway.android.R
import kotlinx.android.synthetic.main.view_text_input.view.*

class TextInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    var text: String?
        get() = editText.text.toString()
        set(value) = editText.setText(value)

    var hint: String?
        get() = inputLayout.hint.toString()
        set(value) {
            inputLayout.hint = value
        }

    fun setOnEditorActionListener(listener: TextView.OnEditorActionListener) {
        editText.setOnEditorActionListener(listener)
    }

    fun addInputFilter(inputFilter: InputFilter) {
        editText.filters = editText.filters.plus(inputFilter)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_text_input, this, true)
        extractAttributes(context, attrs)
    }

    private fun extractAttributes(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.TextInputView, 0, 0).apply {
            try {
                inputLayout.hint = getString(R.styleable.TextInputView_android_hint)
                editText.inputType = getInt(R.styleable.TextInputView_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
                editText.imeOptions = getInt(R.styleable.TextInputView_android_imeOptions, EditorInfo.IME_ACTION_UNSPECIFIED)
                val maxLength = getInt(R.styleable.TextInputView_android_maxLength, -1)
                if (maxLength != -1) {
                    addInputFilter(InputFilter.LengthFilter(maxLength))
                }
            } finally {
                recycle()
            }
        }
    }
}
