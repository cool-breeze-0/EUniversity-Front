package com.example.euniversity.ui.community

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.euniversity.R
import com.example.euniversity.entity.CommunityAnswerItem
import kotlinx.android.synthetic.main.community_answer_details_activity.*

class CommunityAnswerDetailsActivity : AppCompatActivity() {
    var likeImageId=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_answer_details_activity)

        //getParcelableExtra生成数据类型为Parcelable？类型，因为知道传过来的就是CommunityAnswerItem类型
        //因此转化为CommunityAnswerItem类型，这样才能调用里面的属性
        val communityAnswerItem=intent.getParcelableExtra<CommunityAnswerItem>("communityAnswerItem") as CommunityAnswerItem

        //将从问题详情页面的recycleView的Adapter中传来数据在活动中进行展示
        answerProblemPersonNickname.text=communityAnswerItem.answerProblemPersonNickname
        answerProblemPersonIdentity.text=communityAnswerItem.answerProblemPersonIdentity
        answerProblemTime.text=communityAnswerItem.answerProblemTime
        answerContent.text=communityAnswerItem.answerContent
        likeImage.setImageResource(communityAnswerItem.likeImage)
        likeQuantity.text=communityAnswerItem.likeQuantity.toString()

        likeImageId=communityAnswerItem.likeImage
    }
    fun onClick(v:View){
        when(v.id){
            //此时返回需要更新CommunityProblemDetails活动中的回答的点赞量和用户是否点赞图标，因此需要重新加载CommunityProblemDetails活动
            //
            //应设置在ProblemAnswerAdapter中通过点击事件方法中的intent传数据进入ProblemDetails活动中时将问题的ID一并传入，
            // 然后再将问题ID从ProblemDetails加载到AnswerAdapter中，最后通过AnswerAdapter的点击事件方法中的intent将ID传入到本活动中
            //即实现从ProblemAnswerAdapter点击事件将点击的问题的ID传入本活动以类变量存储(此部分未实现)
            //
            //在ProblemDetails（AnswerAdapter）跳转到本活动中时应该销毁ProblemDetails活动避免干扰（已经在AnswerAdapter中完成）
            //
            //本页面中点击toolbar的返回按钮或者底端返回键时，通过问题的ID再次从数据库中查询这个问题的“问题和所有回答”，
            // 并通过intent重新加载数据进入ProblemDetails活动页面更新点赞量和点赞图标，同时finsh本activity避免干扰（此部分尚未实现）
            R.id.communityAnswerDetailsBackButton->{
                finish()
            }
            //根据问题详情页面中的回答对用户是否是like
            // （通过传入回答详情页面的数据中的回答likeImage是否是R.drawable.community_problem_details_like_checked判断）
            // 执行操作，将likaImage设置的图片更改并重新设置likeImageId（这个变量实时反应回答对用户是否是like），点赞量重新设置
            //最后更新数据库中的数据，更改回答对用户的like属性（true改false，false改true）和点赞量，此部分数据库功能尚未实现，暂未进行
            R.id.likeImage->{
                if(likeImageId==R.drawable.community_problem_details_like_unchecked) {
                    likeImage.setImageResource(R.drawable.community_problem_details_like_checked)
                    likeImageId=R.drawable.community_problem_details_like_checked
                    likeQuantity.text=(likeQuantity.text.toString().toInt() + 1 ).toString()
                }else{
                    likeImage.setImageResource(R.drawable.community_problem_details_like_unchecked)
                    likeImageId=R.drawable.community_problem_details_like_unchecked
                    likeQuantity.text=(likeQuantity.text.toString().toInt() - 1 ).toString()
                }
            }
        }
    }
}