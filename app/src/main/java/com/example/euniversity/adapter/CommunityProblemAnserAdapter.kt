package com.example.euniversity.adapter

import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euniversity.R
import com.example.euniversity.entity.CommunityAnswerItem
import com.example.euniversity.entity.CommunityProblemAnswerItem
import com.example.euniversity.ui.community.CommunityProblemDetailsActivity

class CommunityProblemAnserAdapter(val communityProblemAnserList:List<CommunityProblemAnswerItem>) :RecyclerView.Adapter<CommunityProblemAnserAdapter.ViewHolder>(){
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
            val position=viewHolder.adapterPosition
            //数据，根据选中的的位置的问题的ID从数据库中查询出（问题和回答可能需要分别用一张数据表），问题的ID可以在CommunityFragment
            //用伴随对象以列表存储或者通过本Adapter中增加参数传进来，这里通过选择的position访问得到ID。
            //由于获取ID方法和数据库查询方法未实现，此处用position暂时代替，仅供测试
            intent.putParcelableArrayListExtra("answerList",findAnswerList(position))
            //根据问题的ID从数据库中查询提问者昵称、提问时间等，功能尚未实现，自定义数据测试
            intent.putExtra("askProblemPersonNickname","Helloworld")
            intent.putExtra("askProblemTime","提问于2020.12.5")
            intent.putExtra("problemContent","分数估了430，没发挥好，最擅长英语，但考试也失利了，想学英语专业但不知道要不要复读。")

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
    private fun findAnswerList(id:Int):ArrayList<CommunityAnswerItem>{
        val communityAnswerList=ArrayList<CommunityAnswerItem>()
        communityAnswerList.add(
            CommunityAnswerItem("1585465****",
            "南昌大学|大三|已认证","回答于2021.1.5","同学你好！\n" +
                        "高考是人生的一件大事，这是毫无疑...",R.drawable.community_problem_details_like_checked,56))
        communityAnswerList.add(
            CommunityAnswerItem("清风",
                "浙江大学|大四|已认证","回答于2021.1.15","同学你好！\n" +
                        "高考是人生的一件大事，这是毫无疑...",R.drawable.community_problem_details_like_unchecked,5))
        communityAnswerList.add(
            CommunityAnswerItem("1585465****",
                "南昌大学|大三|已认证","回答于2021.1.5","同学你好！\n" +
                        "高考是人生的一件大事，这是毫无疑...",R.drawable.community_problem_details_like_checked,56))
        return communityAnswerList
    }
}