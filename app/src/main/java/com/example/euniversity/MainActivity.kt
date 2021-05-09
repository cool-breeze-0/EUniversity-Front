package com.example.euniversity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.euniversity.ui.community.CommunityFragment
import com.example.euniversity.ui.home.HomeFragment
import com.example.euniversity.ui.predict.PredictFragment
import com.example.euniversity.ui.user.UserAccountActivity
import com.example.euniversity.ui.user.UserFragment
import com.example.euniversity.utils.ActivityUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configButtomNavigation()
        ActivityUtil.replaceFragment(this,R.id.mainFragment,HomeFragment(),false)
    }

    /**
     * 配置底部导航栏
     */
    private fun configButtomNavigation(){
        /**创建选择之后的监听器*/
        buttomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                    R.id.navigation_home->ActivityUtil.replaceFragment(this,R.id.mainFragment,HomeFragment(),false)
                    R.id.navigation_user->ActivityUtil.replaceFragment(this,R.id.mainFragment,UserFragment(),false)
                    R.id.navigation_community->ActivityUtil.replaceFragment(this,R.id.mainFragment,CommunityFragment(),false)
                    R.id.navigation_predict->ActivityUtil.replaceFragment(this,R.id.mainFragment,PredictFragment(),false)
            }
            true
        }
    }

}