package com.example.euniversity.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.euniversity.R
class UserCommonProblemFragment : Fragment() {
    private lateinit var userApplicationContentActivity: UserApplicationContentActivity
    private val TAG="UserCommonProblemFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(activity!=null){
            userApplicationContentActivity=activity as UserApplicationContentActivity
        }
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.user_common_problem_fragment, container, false)

        //设置toolbar的标题使之与此碎片展示内容对应
        val toolbarTitle: TextView =userApplicationContentActivity.findViewById(R.id.toolbarTitle)
        toolbarTitle.setText(R.string.user_common_problem_toolbar_title)

        //配置具体的问题以及解答，从数据库中通过ID（即所处的第几个问题）查找问题以及解答
        //目前尚未完成功能，通过字符串列表存取问题和解答进行测试
//        initProblemAnswer()
        val commonProblem=resources.getStringArray(R.array.userCommonProblem)
        val commonProblemList=ArrayList<String>()
        val commonAnswerList=ArrayList<String>()
        for(i in 0 until commonProblem.size){
            if(i%2==0){
                commonProblemList.add(commonProblem[i])
            }else{
                commonAnswerList.add(commonProblem[i])
            }
        }
        val commonProblemTextView:TextView=view.findViewById(R.id.commonProblemTextView)
        val commonAnswerTextView:TextView=view.findViewById(R.id.commonAnswerTextView)
        commonProblemTextView.setText(commonProblemList.get(UserHelpFragment.getProblemPosition()))
        commonAnswerTextView.setText(commonAnswerList.get(UserHelpFragment.getProblemPosition()))

        return view
    }

    /**
     * 初始化问题及解答

//    private fun initProblemAnswer(){
//        commonProblem.add("用户协议")
//        commonProblem.add("隐私政策")
//        commonAnswer.add("用户协议\n" +
//                "\n" +
//                "（以下简称“本公司”）按照下列条款与条件提供信息和产品，您在本协议中亦可被称为“用户”，以下所述条款和条" +
//                "件将构成您与本公司，就您使用提供的内容所达成的全部协议（以下称“本协议”）。")
//        commonAnswer.add("隐私政策的适用范围:\n" +
//                "\n" +
//                "除某些特定服务外，我们所有的服务均适用本《隐私政策》。这些特定服务将适用特定的隐私政策。针对某些特定服务" +
//                "的特定隐私政策，将更具体地说明我们在该等服务中如何使用您的信息。该特定服务的隐私政策构成本《隐私政策》的" +
//                "一部分。如相关特定服务的隐私政策与本《隐私政策》有不一致之处，适用该特定服务的隐私政策。"
//                )
//    }
     */
}