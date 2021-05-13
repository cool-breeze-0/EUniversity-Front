package com.example.euniversity.ui.community

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.euniversity.MainActivity
import com.example.euniversity.R
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.network.response.Answer
import com.example.euniversity.network.response.Problem
import com.example.euniversity.network.response.Response
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
                                            val intent =
                                                Intent(context, MainActivity::class.java)
                                            //提问后回到communityFragment会刷新communityFragment页面从而刷新问题列表
                                            //默认的按时间排序可以很容易看到自己的提问
                                            intent.putExtra("fragment","communityFragment")
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
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
                if(intent.getStringExtra("answerType").equals("change")){
                    problemContent.setText(intent.getStringExtra("answerContent"))
                }
                //提问和回答问题的提交后需要执行的操作不同，需要分别设置不同的响应事件方法
                //目前尚未实现此功能，直接关闭活动进行测试使用
                operateButton.setOnClickListener {
                    val prefs=getSharedPreferences("user", Context.MODE_PRIVATE)
                    val phone=prefs.getString("phone","")
                    if(!phone.equals("")) {
                        val answer = problemContent.text.toString()
                        if (answer.equals("")) {
                            Toast.makeText(context, "请输入回答内容！", Toast.LENGTH_SHORT).show()
                        } else if (answer.length > 500) {
                            Toast.makeText(context, "回答内容必须在500字符以内！", Toast.LENGTH_SHORT).show()
                        } else {
                            scope.launch(Dispatchers.Main) {
                                try {
                                    val result:Response<Answer>;
                                    if(intent.getStringExtra("answerType").equals("change")) {
                                        result=EUniversityNetwork.updateAnswer(
                                            intent.getIntExtra("answerId",1),
                                            answer,
                                            LocalDateTime.now()
                                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                        )
                                    }else{
                                        result = EUniversityNetwork.answerProblem(
                                            answer,
                                            LocalDateTime.now()
                                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                            phone!!,
                                            intent.getIntExtra("problemId", 1)
                                        )
                                    }
                                    when(result.code){
                                        ResultEnum.INPUT_IS_NULL.code,ResultEnum.ADD_ANSWER_FAILD.code,
                                        ResultEnum.UPDATE_ANSWER_FAILD.code->{
                                            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                                        }
                                        ResultEnum.ADD_ANSWER_SUCCESS.code,ResultEnum.UPDATE_ANSWER_SUCCESS.code->{
                                            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                                            context.finish()
                                            /*val intent =
                                                Intent(context, MainActivity::class.java)
                                            //提问后回到communityFragment会刷新communityFragment页面从而刷新问题列表
                                            //默认的按时间排序可以很容易看到自己的提问
                                            intent.putExtra("fragment","communityFragment")
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)*/
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