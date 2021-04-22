package com.example.euniversity.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.euniversity.R
import com.example.euniversity.adapter.UserCommonProblemItemAdapter
import com.example.euniversity.utils.ActivityUtil

class UserHelpFragment : Fragment() {
    private lateinit var userApplicationContentActivity: UserApplicationContentActivity
    private val commonProblem=ArrayList<String>()
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
        val view=inflater.inflate(R.layout.user_help_fragment, container, false)

        //设置toolbar的标题使之与此碎片展示内容对应
        val toolbarTitle: TextView =userApplicationContentActivity.findViewById(R.id.toolbarTitle)
        toolbarTitle.setText(R.string.user_help_toolbar_title)

        //配置具体的问题，从数据库中通过ID（即所处的第几个问题）查找问题配置到列表中
        //目前尚未完成功能，通过字符串列表存取问题进行测试
        initCommonProblem()
        val listView:ListView=view.findViewById(R.id.commonProblemListView)
        val adapter=UserCommonProblemItemAdapter(userApplicationContentActivity,R.layout.user_common_problem_item,commonProblem)
        listView.adapter=adapter
        //选中问题时跳转到问题详情的碎片中
        listView.setOnItemClickListener { parent, view, position, id ->
            //通过伴随对象传递选中的问题的position（第几个，从0开始）
            problemPosition=position
            ActivityUtil.replaceFragment(userApplicationContentActivity,R.id.userApplicationContentFragment,
                UserCommonProblemFragment(),false)
        }
        adapter.notifyDataSetChanged()

        return view
    }

    /**
     * 初始化数据：问题
     */
    private fun initCommonProblem(){
        commonProblem.add("用户协议")
        commonProblem.add("隐私政策")
    }

    /**
     * 通过伴随对象在此碎片被替换时将选中的问题的position传递到下一个碎片中
     */
    companion object{
        private var problemPosition=0
        fun getProblemPosition()= problemPosition
    }
}