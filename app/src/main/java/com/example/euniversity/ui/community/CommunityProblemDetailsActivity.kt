package com.example.euniversity.ui.community

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euniversity.R
import com.example.euniversity.adapter.CommunityAnswerAdapter
import com.example.euniversity.entity.CommunityAnswerItem
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.utils.ResultEnum
import kotlinx.android.synthetic.main.community_problem_details_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CommunityProblemDetailsActivity : AppCompatActivity() {
    private val job= Job()
    private val scope= CoroutineScope(job)
    private var problemId=1
    private var answerId=1
    private var userAnswerContent=""
    private lateinit var context:CommunityProblemDetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_problem_details_activity)

        context=this
        problemId=intent.getIntExtra("problemId",1)

        initActivity()

        //接受从CommunityProblemAnswerAdapter传来的数据并适配到activity布局中
        /*val askProblemPersonNicknameData=intent.getStringExtra("askProblemPersonNickname")
        val askProblemTimeData=intent.getStringExtra("askProblemTime")
        val problemContentData=intent.getStringExtra("problemContent")
        answerListData=intent.getParcelableArrayListExtra<CommunityAnswerItem>("answerList")
                as ArrayList<CommunityAnswerItem>*/

       /* askProblemPersonNickname.text=askProblemPersonNicknameData
        askProblemTime.text=askProblemTimeData
        problemContent.text=problemContentData

        //answerListData不能为null，即通过getParcelableArrayListExtra获得的值不为null
        val adapter=CommunityAnswerAdapter(answerListData,this)
        answerList.adapter=adapter
        answerList.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()*/

    }

    fun initActivity(){
        scope.launch(Dispatchers.Main) {
            try {
                val result = EUniversityNetwork.findProblemById(problemId)
                when (result.code) {
                    ResultEnum.PROBLEM_NOT_EXIST.code -> {
                        Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                    }
                    ResultEnum.FIND_SUCCESS.code -> {
                        //查询问题的提出者昵称、提出时间、问题全部内容等
                        val askPersonResult =
                            EUniversityNetwork.findUserByPhone(result.data[0].problem.userPhone)
                        var askPersonNickName = ""
                        when (askPersonResult.code) {
                            ResultEnum.USER_NOT_EXIST.code -> {
                            }
                            ResultEnum.FIND_SUCCESS.code -> {
                                if (!askPersonResult.data[0].nikeName.equals("null"))
                                    askPersonNickName = askPersonResult.data[0].nikeName
                                else
                                    askPersonNickName =
                                        askPersonResult.data[0].phone.substring(0, 7) + "****"
                            }
                        }
                        //将问题信息填到activity中
                        askProblemPersonNickname.text = askPersonNickName
                        askProblemTime.text = "提问于" + result.data[0].problem.time.substring(0, 10)
                        problemContent.text = result.data[0].problem.content

                        //将回答列表的相关信息从数据库中查询填到activity中
                        val answers = result.data[0].answers
                        val answerListDetails = ArrayList<CommunityAnswerItem>()
                        for (answer in answers) {
                            //从数据库依次获得查询的各个回答的回答者昵称、回答者个人信息、所登录的用户是否对回答点赞等
                            var nickName = ""
                            var answerUserInformation = ""
                            var isliked = false;

                            val answerUserPhone = answer.userPhone
                            val answerPersonResult =
                                EUniversityNetwork.findUserByPhone(answerUserPhone)
                            when (answerPersonResult.code) {
                                ResultEnum.USER_NOT_EXIST.code -> {
                                }
                                ResultEnum.FIND_SUCCESS.code -> {
                                    if (!answerPersonResult.data[0].nikeName.equals("null")) {
                                        nickName = answerPersonResult.data[0].nikeName
                                        answerUserInformation =
                                            answerPersonResult.data[0].province + "|" + answerPersonResult.data[0].city + "|" +
                                                    if (answerPersonResult.data[0].gender == 0) "男" else "女"
                                        answerUserInformation += "|" + answerPersonResult.data[0].identity
                                    } else {
                                        nickName = answerPersonResult.data[0].phone.substring(
                                            0,
                                            7
                                        ) + "****"
                                    }
                                }
                            }
                            val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
                            val phone = prefs.getString("phone", "")
                            if (!phone.equals("")) {
                                val islikedResult = EUniversityNetwork.isliked(phone!!, answer.id)
                                when (islikedResult.code) {
                                    ResultEnum.INPUT_IS_NULL.code, ResultEnum.LIKE_NOT_EXIST.code -> {
                                    }
                                    ResultEnum.LIKE_EXIST.code -> {
                                        isliked = true
                                    }
                                }
                                //当用户回答过问题时，将按钮改为修改回答并将回答的内容和id存下来通过intent
                                //传到回答页面中
                                if(answerUserPhone.equals(phone)) {
                                    answerButton.text = "修改回答"
                                    userAnswerContent=answer.content
                                    answerId=answer.id
                                }
                            }
                            answerListDetails.add(
                                CommunityAnswerItem(
                                    nickName,
                                    answerUserInformation,
                                    "回答于" + answer.time.substring(0, 10),
                                    answer.content,
                                    if (isliked) R.drawable.community_problem_details_like_checked
                                    else R.drawable.community_problem_details_like_unchecked,
                                    answer.likes,
                                    answer.id
                                )
                            )
                        }
                        val adapter = CommunityAnswerAdapter(answerListDetails, context)
                        answerList.adapter = adapter
                        answerList.layoutManager = LinearLayoutManager(context)
                        adapter.notifyDataSetChanged()
//                    intent.putParcelableArrayListExtra("answerList",answerList)
                    }
                }

//                if(phone.equals("")){
//                    val result= EUniversityNetwork.findProblemById(problemId)
//                }
            }catch (e: SocketTimeoutException){
                Toast.makeText(context,"似乎没有网络，无法连接服务器！",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClick(view:View){
        when(view.id){
            R.id.communityProblemDetailsBackButton->{
                finish()
            }
            //点击我要回答按钮跳转到“提出或回答问题”页面，并设置好操作方式为answer
            R.id.answerButton->{
                val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
                val phone = prefs.getString("phone", "")
                if (!phone.equals("")) {
                    val intent = Intent(this, CommunityAskOrAnswerActivity::class.java)
                    intent.putExtra("opreateProblemType", "answer")
                    if(answerButton.text.equals("我来回答")) {
                        intent.putExtra("answerType", "answer")
                        intent.putExtra("problemId",problemId)
                    }else{
                        intent.putExtra("answerType","change")
                        intent.putExtra("answerContent",userAnswerContent)
                        intent.putExtra("answerId",answerId)
                    }
                    startActivity(intent)
                }else{
                    Toast.makeText(context,"请登录后再进行操作！",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        initActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}