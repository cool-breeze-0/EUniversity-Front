package com.example.euniversity.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


object KeyBoardUtil {
    /**
     * 自动关闭软键盘
     */
    fun closeKeybord(activity: Activity) {
        val imm=activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }
}