package com.example.euniversity.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euniversity.R
import com.example.euniversity.entity.CommunityAnswerItem
import com.example.euniversity.ui.community.CommunityAnswerDetailsActivity

class CommunityAnswerAdapter(val communityAnswerList: List<CommunityAnswerItem>,val activity:Activity):RecyclerView.Adapter<CommunityAnswerAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val answerProblemPersonNickname=view.findViewById<TextView>(R.id.answerProblemPersonNickname)
        val answerProblemPersonIdentity=view.findViewById<TextView>(R.id.answerProblemPersonIdentity)
        val answerProblemTime=view.findViewById<TextView>(R.id.answerProblemTime)
        val answerContent=view.findViewById<TextView>(R.id.answerContent)
        val likeImage=view.findViewById<ImageView>(R.id.likeImage)
        val likeQuantity=view.findViewById<TextView>(R.id.likeQuantity)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityAnswerAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.community_problem_details_answer_item,parent,false)
        val viewHolder=ViewHolder(view)

        //设置recyclerView点击事件进入回答详情页面
        val onClick=View.OnClickListener {
            val position=viewHolder.adapterPosition
            //可以在本adapter传入的communityAnswerList的子项communityAnswerItem中
            // 使用从数据库中查询出来的回答的全部内容作为answerContent，但是本适配器只获取前一部分展示，详情在如下回答详情中展示
            val communityAnswerItem=communityAnswerList[position]
            val intent=Intent(parent.context,CommunityAnswerDetailsActivity::class.java)
            intent.putExtra("communityAnswerItem",communityAnswerItem)
            parent.context.startActivity(intent)
            //在进入回答详情时清除问题详情活动，这样在从回答详情重新进入问题详情活动（重新从数据库中获取数据进行加载更新点赞）时
            // 活动栈中只有一个问题详情活动
//            activity.finish()
        }
        //点击recycleView的每一个整体的选项都会触发响应事件进入回答详情页面
        //设置不看回答详情不能点赞，此处不能点赞只能看到点赞量，点击点赞图标也会跳转到回答详情页面
        val answerItemLayout=view.findViewById<ViewGroup>(R.id.answerItemLayout)
        answerItemLayout.setOnClickListener(onClick)

        return viewHolder
    }

    override fun getItemCount(): Int =communityAnswerList.size

    override fun onBindViewHolder(holder: CommunityAnswerAdapter.ViewHolder, position: Int) {
        val communityAnswerItem=communityAnswerList[position]
        holder.answerProblemPersonNickname.text=communityAnswerItem.answerProblemPersonNickname
        holder.answerProblemPersonIdentity.text=communityAnswerItem.answerProblemPersonIdentity
        holder.answerProblemTime.text=communityAnswerItem.answerProblemTime

        //可以在本adapter传入的communityAnswerList的子项communityAnswerItem中
        // 使用从数据库中查询出来的回答的全部内容作为answerContent，但是本适配器只获取前一部分展示，详情在回答详情中展示
        //可以避免进入回答详情页面时再次进行数据库查询操作
        //此处尚未实现，用全部内容适配到列表中
        holder.answerContent.text=
            if (communityAnswerItem.answerContent.length>35) communityAnswerItem.answerContent.substring(0,35)+"..."
            else communityAnswerItem.answerContent

        holder.likeImage.setImageResource(communityAnswerItem.likeImage)
        holder.likeQuantity.text=communityAnswerItem.likeQuantity.toString()
    }

}