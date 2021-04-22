package com.example.euniversity.ui.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.euniversity.R
import kotlinx.android.synthetic.main.community_ask_or_answer_activity.*

class CommunityAskOrAnswerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_ask_or_answer_activity)

        //根据启动活动时传来的操作类型加载不同页面实现不同功能
        val operateProblemType=intent.getStringExtra("opreateProblemType")
        when(operateProblemType){
            "ask"->{
                toolbarTitle.text="提出问题"
                operateDescription.text="我的问题"
                textCapacity.text="200字以内"
                operateButton.text="提交问题"
                //提问和回答问题的提交后需要执行的操作不同，需要分别设置不同的响应事件方法
                //目前尚未实现此功能，直接关闭活动进行测试使用
                operateButton.setOnClickListener {
                    finish()
                }
            }
            "answer"->{
                toolbarTitle.text="回答问题"
                operateDescription.text="我的回答"
                textCapacity.text="500字以内"
                operateButton.text="提交回答"
                //提问和回答问题的提交后需要执行的操作不同，需要分别设置不同的响应事件方法
                //目前尚未实现此功能，直接关闭活动进行测试使用
                operateButton.setOnClickListener {
                    finish()
                }
            }
        }
    }
    fun onClick(v:View){
        when(v.id){
            R.id.communityAskOrAnswerBackButton->{
                finish()
            }
        }
    }
}