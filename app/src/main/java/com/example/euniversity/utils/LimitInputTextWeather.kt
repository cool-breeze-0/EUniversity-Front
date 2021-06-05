package com.example.euniversity.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class LimitInputTextWeather(editText: EditText) : TextWatcher{
    private var et:EditText
    private val regex="[\u4E00-\u9FA5\u0020]"
    init {
        this.et=editText
    }


    override fun afterTextChanged(s: Editable?) {
        val str=s.toString()
        val inputStr=clearLimitStr( str)
        et.removeTextChangedListener(this)
        s?.replace(0,s.length,inputStr?.trim())
        et.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    private fun clearLimitStr( str: String): String? {
        return str.replace(regex.toRegex(), "")
    }
}