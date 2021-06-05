package com.example.euniversity.utils

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


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

    /**
     * 判断多点击的组件是不是edittext，是的话不用隐藏否则则需要
     */
    fun isShouldHideInput(v: View?, event: MotionEvent):Boolean{
        if(v !=null &&(v is EditText)){
            val l = intArrayOf(0,0)
            v.getLocationInWindow(l);
            val left=l[0]
            val top =l[1]
            val bottom=top + v.height
            val right=left+v.width
            if(event.getX()>left && event.getY()<bottom
                && event.getY()>top && event.getX()<right){
                return false
            }else{
                return true
            }
        }
        return true
    }
}