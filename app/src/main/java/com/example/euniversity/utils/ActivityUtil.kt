package com.example.euniversity.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


object ActivityUtil {
    /**
     * 替换主布局文件中的FrameLayout，实现页面的动态加载功能
     * @param fragmentId 需要被替换FrameLayout的id
     * @param fragment 已经加载了需要被显示的布局文件的Fragment类
     * @param push 是否将操作加入返回栈
     */
    fun replaceFragment(activity: AppCompatActivity,fragmentId:Int,fragment:Fragment,push:Boolean){
        val transaction=activity.supportFragmentManager.beginTransaction()
        transaction.replace(fragmentId,fragment)

        /*是否将这次的操作加入栈中*/
        if(push){
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}