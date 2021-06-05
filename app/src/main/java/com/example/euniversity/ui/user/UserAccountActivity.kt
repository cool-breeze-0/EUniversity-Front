package com.example.euniversity.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.euniversity.R
import com.example.euniversity.utils.ActivityUtil
import com.example.euniversity.utils.KeyBoardUtil

class UserAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_account_activity)
        //根据跳转过来的活动发送的数据确定加载哪个碎片
        val userAccountFragment=intent.getStringExtra("userAccountFragment")
        when(userAccountFragment) {
            "login"->ActivityUtil.replaceFragment(this, R.id.userAccountFragment, UserLoginFragment(), false)
            "changePassword"->ActivityUtil.replaceFragment(this,R.id.userAccountFragment,
                UserChangePasswordFragment(),false)
        }
    }
    fun onClick(v:View){
        when(v.id){
            //返回按钮功能与返回键一致
            R.id.userAccountBackButton->{
                finish()
            }
        }
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev?.action== MotionEvent.ACTION_DOWN){
            val v=currentFocus
            if(KeyBoardUtil.isShouldHideInput(v, ev)){
                KeyBoardUtil.closeKeybord(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}