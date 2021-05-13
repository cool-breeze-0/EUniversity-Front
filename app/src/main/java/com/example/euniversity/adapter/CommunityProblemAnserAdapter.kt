package com.example.euniversity.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euniversity.R
import com.example.euniversity.entity.CommunityAnswerItem
import com.example.euniversity.entity.CommunityProblemAnswerItem
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.network.response.ProblemAnswer
import com.example.euniversity.ui.community.CommunityProblemDetailsActivity
import com.example.euniversity.utils.ResultEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CommunityProblemAnserAdapter(val communityProblemAnserList:List<CommunityProblemAnswerItem>,
                                   val problemIdList:ArrayList<Int>) :RecyclerView.Adapter<CommunityProblemAnserAdapter.ViewHolder>(){
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val problem:TextView=view.findViewById(R.id.problem)
        val askProblemTime:TextView=view.findViewById(R.id.askProblemTime)
        val answer:TextView=view.findViewById(R.id.answer)
        val answerProblemTime:TextView=view.findViewById(R.id.answerProblemTime)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityProblemAnserAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.community_problem_answer_item,parent,false)
        val viewHolder=ViewHolder(view)

        val onClick=View.OnClickListener {
            val intent=Intent(parent.context,CommunityProblemDetailsActivity::class.java)
            val problemId=problemIdList[viewHolder.adapterPosition]
            //将问题id传到CommunityProblemDetailsActivity中
                /*val answers=problemAnswerListDB[position].answers
                val answerList=ArrayList<CommunityAnswerItem>()
                for(answer in answers){
                    var nickName=""
                    var answerUserInformation=""
                    var isliked=false;

                    val answerUserPhone=answer.userPhone
                    val result=EUniversityNetwork.findUserByPhone(answerUserPhone)
                    when(result.code){
                        ResultEnum.USER_NOT_EXIST.code->{}
                        ResultEnum.FIND_SUCCESS.code->{
                            if (!result.data[0].nikeName.equals("null")) {
                                nickName = result.data[0].nikeName
                                answerUserInformation=result.data[0].province+"|"+result.data[0].city+"|"+
                                        if(result.data[0].gender==0) "男" else "女"
                                answerUserInformation+="|"+result.data[0].identity
                            }else {
                                nickName = result.data[0].phone.substring(0, 7) + "****"
                            }
                        }
                    }
                    val prefs=activity.getSharedPreferences("user", Context.MODE_PRIVATE)
                    val phone=prefs.getString("phone","")
                    if(!phone.equals("")) {
                        val islikedResult = EUniversityNetwork.isliked(phone!!,answer.id)
                        when(islikedResult.code){
                            ResultEnum.INPUT_IS_NULL.code,ResultEnum.LIKE_NOT_EXIST.code->{}
                            ResultEnum.LIKE_EXIST.code->{
                                isliked=true
                            }
                        }
                    }
                    answerList.add(
                        CommunityAnswerItem(
                        nickName,answerUserInformation,"回答于"+answer.time.substring(0,10),
                            answer.content,
                            if(isliked) R.drawable.community_problem_details_like_checked
                            else R.drawable.community_problem_details_like_unchecked,
                            answer.likes,answer.id
                    )
                    )
                }
                intent.putParcelableArrayListExtra("answerList",answerList)
                //根据从communityfragment中获得问题的提问者phone从数据库中查询提问者昵称
                val result=EUniversityNetwork.findUserByPhone(problemAnswerListDB[position].problem.userPhone)
                var nickName=""
                when(result.code){
                    ResultEnum.USER_NOT_EXIST.code->{}
                    ResultEnum.FIND_SUCCESS.code->{
                        if (!result.data[0].nikeName.equals("null"))
                            nickName=result.data[0].nikeName
                        else
                            nickName=result.data[0].phone.substring(0,7)+"****"
                    }
                }
                //从communityfragment中获得问题的详细信息传到CommunityProblemDetailsActivity页面
                intent.putExtra("askProblemPersonNickname",nickName)
                intent.putExtra("askProblemTime","提问于"+problemAnswerListDB[position].problem.time.substring(0,10))
                intent.putExtra("problemContent",problemAnswerListDB[position].problem.content)*/
            intent.putExtra("problemId",problemId)
            parent.context.startActivity(intent)
        }
        //点击recycleView的每一个整体的选项都会触发响应事件进入问题详情页面
        val communityProblemAnswerLayout:ViewGroup=view.findViewById(R.id.communityProblemAnswerLayout)
        communityProblemAnswerLayout.setOnClickListener(onClick)

        return viewHolder
    }

    override fun getItemCount(): Int =communityProblemAnserList.size

    override fun onBindViewHolder(holder: CommunityProblemAnserAdapter.ViewHolder, position: Int) {
        val communityProblemAnswerItem=communityProblemAnserList[position]
        holder.problem.text=communityProblemAnswerItem.problem
        holder.askProblemTime.text=communityProblemAnswerItem.askProblemTime
        holder.answer.text=communityProblemAnswerItem.answer
        holder.answerProblemTime.text=communityProblemAnswerItem.answerProblemTime
    }
    /**
     * 根据选中的的位置的问题的ID从数据库中查询出Answer列表，
     * 注意likeImage配置时需要从数据库中得到回答对用户是否是like来判断使用那个图标
     * 功能尚未实现，直接自定义数据测试
     */
    /*private fun findAnswerList(position:Int):ArrayList<CommunityAnswerItem>{
        val communityAnswerList=ArrayList<CommunityAnswerItem>()
//        communityAnswerList.add(
//            CommunityAnswerItem("1585465****",
//                "南昌大学|大三|已认证","回答于2021.1.5","同学你好！\n" +
//                        "高考是人生的一件大事，这是毫无疑...",R.drawable.community_problem_details_like_checked,56))

        return communityAnswerList
    }*/
}