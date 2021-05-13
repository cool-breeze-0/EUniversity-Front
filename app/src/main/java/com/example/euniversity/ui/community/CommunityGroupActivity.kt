package com.example.euniversity.ui.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euniversity.R
import com.example.euniversity.adapter.CommunityProblemAnserAdapter
import com.example.euniversity.entity.CommunityProblemAnswerItem
import com.example.euniversity.network.response.ProblemAnswer
import kotlinx.android.synthetic.main.community_group_activity.*

class CommunityGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_group_activity)
        //获取从groupItem传来的标题和问题项
        val groupItem=intent.getStringExtra("groupItem")
        val communityProblemAnswer=intent.getParcelableArrayListExtra<CommunityProblemAnswerItem>("communityProblemAnswer")
        val problemIdList=intent.getIntegerArrayListExtra("problemIdList")
        //将传来的标题和问题项在活动中显示
        toolbarTitle.text=groupItem
        val adapter=CommunityProblemAnserAdapter(communityProblemAnswer as List<CommunityProblemAnswerItem>,
            problemIdList!!)
        problemAnswerList.adapter=adapter
        problemAnswerList.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }
    fun onClick(v:View){
        when(v.id){
            R.id.communityGroupBackButton->{
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}