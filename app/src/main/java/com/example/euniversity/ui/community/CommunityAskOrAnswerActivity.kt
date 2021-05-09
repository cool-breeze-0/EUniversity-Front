package com.example.euniversity.ui.community

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.euniversity.R
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.network.response.Problem
import com.example.euniversity.utils.ResultEnum
import kotlinx.android.synthetic.main.community_ask_or_answer_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommunityAskOrAnswerActivity : AppCompatActivity() {
    private val job= Job()
    private val scope= CoroutineScope(job)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_ask_or_answer_activity)

        val context=this
        //根据启动活动时传来的操作类型加载不同页面实现不同功能
        val operateProblemType=intent.getStringExtra("opreateProblemType")
        when(operateProblemType){
            "ask"->{
                toolbarTitle.text="提出问题"
                operateDescription.text="我的问题"
                textCapacity.text="200字以内"
                operateButton.text="提交问题"
                //提问和回答问题的提交后需要执行的操作不同，需要分别设置不同的响应事件方法
                operateButton.setOnClickListener {
                    val prefs=getSharedPreferences("user", Context.MODE_PRIVATE)
                    val phone=prefs.getString("phone","")
                    if(!phone.equals("")) {
                        val problem = problemContent.text.toString()
                        if (problem.equals("")) {
                            Toast.makeText(context, "请输入提问内容！", Toast.LENGTH_SHORT).show()
                        } else if (problem.length > 200) {
                            Toast.makeText(context, "问题内容必须在200字符以内！", Toast.LENGTH_SHORT).show()
                        } else {
                            scope.launch(Dispatchers.Main) {
                                try {
                                    val result=EUniversityNetwork.askProblem(
                                        problem,LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),phone!!)
                                    when(result.code){
                                        ResultEnum.INPUT_IS_NULL.code,ResultEnum.ASK_PROBLEM_FAILD.code->{
                                            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                                        }
                                        ResultEnum.ASK_PROBLEM_SUCCESS.code->{
                                            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                                            context.finish()
                                        }
                                    }
                                } catch (e: SocketTimeoutException) {
                                    Toast.makeText(context, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }else{
                        Toast.makeText(context,"请登录后再进行操作！",Toast.LENGTH_SHORT).show()
                    }
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}