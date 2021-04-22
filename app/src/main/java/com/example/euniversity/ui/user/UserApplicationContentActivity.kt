package com.example.euniversity.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.euniversity.R
import com.example.euniversity.utils.ActivityUtil

class UserApplicationContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_application_content_activity)
        //根据跳转过来的活动发送的数据确定加载哪个碎片
        val userApplicationContentFragment=intent.getStringExtra("userApplicationContentFragment")
        when(userApplicationContentFragment) {
            "aboutUs"-> ActivityUtil.replaceFragment(this, R.id.userApplicationContentFragment, UserAboutUsFragment(), false)
            "help"-> ActivityUtil.replaceFragment(this,R.id.userApplicationContentFragment,
                UserHelpFragment(),false)
        }
    }
    fun onClick(v: View){
        when(v.id){
            R.id.userApplicationContentBackButton->{
                finish()
            }
        }
    }
}