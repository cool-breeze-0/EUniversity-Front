package com.example.euniversity.ui.community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euniversity.R
import com.example.euniversity.adapter.CommunityAnswerAdapter
import com.example.euniversity.entity.CommunityAnswerItem
import kotlinx.android.synthetic.main.community_problem_details_activity.*

class CommunityProblemDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_problem_details_activity)

        //接受从CommunityProblemAnswerAdapter传来的数据并适配到activity布局中
        val askProblemPersonNicknameData=intent.getStringExtra("askProblemPersonNickname")
        val askProblemTimeData=intent.getStringExtra("askProblemTime")
        val problemContentData=intent.getStringExtra("problemContent")
        val answerListData=intent.getParcelableArrayListExtra<CommunityAnswerItem>("answerList")

        askProblemPersonNickname.text=askProblemPersonNicknameData
        askProblemTime.text=askProblemTimeData
        problemContent.text=problemContentData

        //answerListData不能为null，即通过getParcelableArrayListExtra获得的值不为null
        val adapter=CommunityAnswerAdapter(answerListData as ArrayList<CommunityAnswerItem>,this)
        answerList.adapter=adapter
        answerList.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()

    }
    fun onClick(view:View){
        when(view.id){
            R.id.communityProblemDetailsBackButton->{
                finish()
            }
            //点击我要回答按钮跳转到“提出或回答问题”页面，并设置好操作方式为answer
            R.id.answerButton->{
                val intent=Intent(this,CommunityAskOrAnswerActivity::class.java)
                intent.putExtra("opreateProblemType","answer")
                startActivity(intent)
            }
        }
    }
}