package com.example.euniversity.ui.community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.euniversity.MainActivity
import com.example.euniversity.R
import com.example.euniversity.adapter.CommunityProblemAnserAdapter
import com.example.euniversity.adapter.ImageAdapter
import com.example.euniversity.entity.CommunityProblemAnswerItem
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.network.response.ProblemAnswer
import com.example.euniversity.network.response.Response
import com.example.euniversity.utils.KeyBoardUtil
import com.example.euniversity.utils.ResultEnum
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.community_problem_answer_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class CommunityFragment : Fragment() {
    private val TAG="CommunityFragment"
    private lateinit var communityViewModel: CommunityViewModel
    private lateinit var mainActivity:MainActivity
    private lateinit var popupMenu:PopupMenu
    private val job= Job()
    private val scope= CoroutineScope(job)

    private var time=1
    private var operation="init"

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if(activity!=null){
            mainActivity=activity as MainActivity
        }
        communityViewModel =
                ViewModelProviders.of(this).get(CommunityViewModel::class.java)
        val view = inflater.inflate(R.layout.community_fragment, container, false)
        communityViewModel.problemIdList.postValue(ArrayList())
        communityViewModel.problemAnswerList.postValue(ArrayList())

        //将图片配置到banner中实现图片轮播
        val banner=view.findViewById<Banner<Int,ImageAdapter>>(R.id.communityBanner)
        val adapter=ImageAdapter(initImages())
        banner.setAdapter(adapter)
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(context))

        //初始化问题项列表
        time=1
        operation="init"
        initProblemAnswerList()

        //初始化排序菜单
        val sortImage=view.findViewById<ImageView>(R.id.sortImage)
        initPopupMenu(sortImage)

        //当viewModel的变量内容变化时触发观察者事件，将问题项实例CommunityProblemAnswerItem适配到RecycleView中
        communityViewModel.problemAnswerList.observe(viewLifecycleOwner, Observer {
            val communityProblemAnwerList=view.findViewById<RecyclerView>(R.id.communityProblemAnswerList)
            Log.e(TAG,"observe event")
            val recyclerAdapter=CommunityProblemAnserAdapter(it,communityViewModel.problemIdList.value!!)
            communityProblemAnwerList.layoutManager = LinearLayoutManager(mainActivity)
            communityProblemAnwerList.adapter=recyclerAdapter
            recyclerAdapter.notifyDataSetChanged()
        })

        val refreshLayout=view.findViewById<TwinklingRefreshLayout>(R.id.refreshLayout)
        refreshLayout.setAutoLoadMore(false)
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter(){
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                Handler().postDelayed(Runnable {
                    refreshLayout?.finishRefreshing()
                },1000)
            }

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                Handler().postDelayed(Runnable {
//                    time+=1
//                    operateUniversity(view.findViewById<EditText>(R.id.searchEditView).text.toString())
                    time+=1
                    when(operation){
                        "init"->initProblemAnswerList()
                        "search"->findSearchResult(view.findViewById<EditText>(R.id.searchEditView).text.toString())
                        "timeSort"->findSortResult("timeSort")
                        "qualitySort"->findSortResult("qualitySort")
                        "comprehensiveSort"->findSortResult("comprehensiveSort")
                    }
                    refreshLayout?.finishLoadmore()
                },1000)
            }
        } )

        //为各个组件添加点击事件
        val myProblemImage=view.findViewById<ImageView>(R.id.myProblemImage)
        val myAnswerImage=view.findViewById<ImageView>(R.id.myAnswerImage)
        val commonProblemImage=view.findViewById<ImageView>(R.id.commonProblemImage)

        val searchImage=view.findViewById<ImageView>(R.id.searchImage)
        val searchEditView=view.findViewById<EditText>(R.id.searchEditView)

        val askButton=view.findViewById<TextView>(R.id.askButton)
        val backToTopImage=view.findViewById<ImageView>(R.id.backToTopImage)

        val onClick=View.OnClickListener {
            when(it.id){
                //根据所选的groupItem将不同的groupItem名称和数据库中查找的问题项传输到CommunityGroupActivity中展示不同的结果
                //此处为了将问题项传输，将CommunityProblemAnswerItem继承Parcelable接口，并且强制类型转化
                //List<CommunityProblemAnswerItem>为ArrayList<CommunityProblemAnswerItem>
                //必须注意从findGroupResult返回的数据类型为ArrayList<CommunityProblemAnswerItem>？（是List<CommunityProblemAnswerItem>？的子类）
                R.id.myProblemImage-> {
                    val prefs=mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
                    val phone=prefs.getString("phone","")
                    if(!phone.equals("")) {
                        scope.launch(Dispatchers.Main) {
                            try {
                                var problemAnswerList=ArrayList<CommunityProblemAnswerItem>()
                                var problemIdList=ArrayList<Int>();
                                val result = EUniversityNetwork.myProblem(phone!!)
                                when (result.code) {
                                    ResultEnum.INPUT_IS_NULL.code -> {
                                        Toast.makeText(mainActivity, result.msg, Toast.LENGTH_SHORT).show()
                                    }
                                    ResultEnum.USER_PROBLEM_NOT_EXIST.code->{
                                        Toast.makeText(mainActivity, "您还没有提过问题", Toast.LENGTH_SHORT).show()
                                    }
                                    ResultEnum.FIND_SUCCESS.code -> {
                                        for(i in 0 until result.data.size){
                                            problemIdList.add(result.data[i].problem.id)
                                        }
                                        problemAnswerList=resultToProblemAnser(result)
                                    }
                                }
                                val intent=Intent(mainActivity,CommunityGroupActivity::class.java)
                                intent.putExtra("groupItem","我的提问")
                                intent.putParcelableArrayListExtra("communityProblemAnswer",
                                    problemAnswerList)
                                intent.putIntegerArrayListExtra("problemIdList",problemIdList)
                                startActivity(intent)
                            } catch (e: SocketTimeoutException) {
                                Toast.makeText(mainActivity, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }else{
                        Toast.makeText(context,"请登录后再进行操作！",Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.myAnswerImage->{
                    val prefs=mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
                    val phone=prefs.getString("phone","")
                    if(!phone.equals("")) {
                        scope.launch(Dispatchers.Main) {
                            try {
                                var problemAnswerList=ArrayList<CommunityProblemAnswerItem>()
                                var problemIdList=ArrayList<Int>();
                                val result = EUniversityNetwork.myAnswer(phone!!)
                                when (result.code) {
                                    ResultEnum.INPUT_IS_NULL.code -> {
                                        Toast.makeText(mainActivity, result.msg, Toast.LENGTH_SHORT).show()
                                    }
                                    ResultEnum.USER_ANSWER_NOT_EXIST.code->{
                                        Toast.makeText(mainActivity, "您还没有回答问题", Toast.LENGTH_SHORT).show()
                                    }
                                    ResultEnum.FIND_SUCCESS.code -> {
                                        for(i in 0 until result.data.size){
                                            problemIdList.add(result.data[i].problem.id)
                                        }
                                        problemAnswerList=resultToProblemAnser(result)
                                    }
                                }
                                val intent=Intent(mainActivity,CommunityGroupActivity::class.java)
                                intent.putExtra("groupItem","我的回答")
                                intent.putParcelableArrayListExtra("communityProblemAnswer",
                                    problemAnswerList)
                                intent.putIntegerArrayListExtra("problemIdList",problemIdList)
                                startActivity(intent)
                            } catch (e: SocketTimeoutException) {
                                Toast.makeText(mainActivity, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }else{
                        Toast.makeText(context,"请登录后再进行操作！",Toast.LENGTH_SHORT).show()
                    }

                }
                R.id.commonProblemImage->{
                    scope.launch(Dispatchers.Main) {
                        try {
                            var problemAnswerList = ArrayList<CommunityProblemAnswerItem>()
                            var problemIdList=ArrayList<Int>();
                            val result = EUniversityNetwork.commonProblem()
                            when (result.code) {
                                ResultEnum.NO_PROBLEMS_IN_DATABASE.code -> {
                                    Toast.makeText(mainActivity, result.msg, Toast.LENGTH_SHORT).show()
                                }
                                ResultEnum.FIND_SUCCESS.code -> {
                                    for(i in 0 until result.data.size){
                                        problemIdList.add(result.data[i].problem.id)
                                    }
                                    problemAnswerList = resultToProblemAnser(result)
                                }
                            }
                            val intent=Intent(mainActivity,CommunityGroupActivity::class.java)
                            intent.putExtra("groupItem","常见问题")
                            intent.putParcelableArrayListExtra("communityProblemAnswer",
                                problemAnswerList)
                            intent.putIntegerArrayListExtra("problemIdList",problemIdList)
                            startActivity(intent)
                        } catch (e: SocketTimeoutException) {
                            Toast.makeText(mainActivity, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                }
                //根据搜索文本框中用户输入的文本在数据库中查询相应的问题项
                R.id.searchImage->{
                    time=1
                    operation="search"
                    findSearchResult(searchEditView.text.toString())
                }
                //点击排序图标时弹出菜单供用户选择排序方式
                R.id.sortImage->{
                    popupMenu.show()
                }
                //点击我要提问按钮进入提问活动页面，并将处理问题类型发送给activity
                R.id.askButton->{
                    val prefs=mainActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
                    val phone=prefs.getString("phone","")
                    if(!phone.equals("")) {
                        val intent=Intent(mainActivity,CommunityAskOrAnswerActivity::class.java)
                        intent.putExtra("opreateProblemType","ask")
                        mainActivity.startActivity(intent)
                    }else{
                        Toast.makeText(context,"请登录后再进行操作！",Toast.LENGTH_SHORT).show()
                    }
                }
                //点击回到顶部图标时,回到整个碎片顶部并设置recyclerView列表回到第0个的位置
                R.id.backToTopImage->{
                    val recyclerView=view.findViewById<RecyclerView>(R.id.communityProblemAnswerList)
                    val scrollView=view.findViewById<ScrollView>(R.id.scrollView)
                    recyclerView.smoothScrollToPosition(0)
                    scrollView.fullScroll(ScrollView.FOCUS_UP)
                }
            }
        }
        searchImage.setOnClickListener(onClick)
        myProblemImage.setOnClickListener(onClick)
        myAnswerImage.setOnClickListener(onClick)

        commonProblemImage.setOnClickListener(onClick)
        sortImage.setOnClickListener(onClick)

        askButton.setOnClickListener(onClick)
        backToTopImage.setOnClickListener(onClick)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    /**
     * 初始化banner需要的图片
     */
    private fun initImages():List<Int>{
        val imageList=ArrayList<Int>()
        val imageResource=resources.obtainTypedArray(R.array.communityBannerImage)
        for (i in 0 until imageResource.length()){
            imageList.add(imageResource.getResourceId(i,0))
        }
        return imageList
    }

    /**
     * 初始化排序菜单，点击排序图标时弹出菜单项供用户选择排序方式
     */
    private fun initPopupMenu(view:View){
        popupMenu=PopupMenu(mainActivity,view)
        popupMenu.menuInflater.inflate(R.menu.sort_problem_answer_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                //根据用户选择的排序方法菜单项将查询的问题项进行排序并在recycleView中更新
                R.id.comprehensiveSortItem-> {
                    time=1
                    operation="comprehensiveSort"
                    findSortResult("comprehensiveSort")
                }
                R.id.qualitySortItem->{
                    time=1
                    operation="qualitySort"
                    findSortResult("qualitySort")
                }
                R.id.timeSortItem->{
                    time=1
                    operation="timeSort"
                    findSortResult("timeSort")
                }
            }
            true
        }
    }

    /**
     * 倒置数据库中传来的问题列表数据，与recyclerView同步，便于数据操作
     */
    /*fun upsideDownData(data:List<ProblemAnswer>):ArrayList<Int> {
        val newData = ArrayList<Int>();
        for(i in 1..data.size){
            newData.add(data[data.size-i].problem.id)
        }
        return newData
    }*/

    /**
     * 初始化问题项列表，从数据库中获取问题及回答
     */
    private fun initProblemAnswerList(){
//        problemAnswerList.add(CommunityProblemAnswerItem("问：  分数估了430，没发挥好，最擅长英语，但考试也失利了，想学英语专业...",
//        "2020.12.5提问","答：  同学你好！\n" + "高考是人生的一件大事，这是毫无疑...","2021.1.15回答"))
        scope.launch(Dispatchers.Main){
            try {
                val result = EUniversityNetwork.findAllProblemAnswer(time)
                val problemAnswerList=if(time==1) ArrayList<CommunityProblemAnswerItem>() else communityViewModel.problemAnswerList.value!!
                val problemIdList=if(time==1) ArrayList<Int>() else communityViewModel.problemIdList.value!!
                when(result.code){
                    ResultEnum.NO_PROBLEMS_IN_DATABASE.code->{
                        Toast.makeText(mainActivity,result.msg,Toast.LENGTH_SHORT).show()
                    }
                    ResultEnum.FIND_SUCCESS.code->{
                        problemAnswerList.addAll(resultToProblemAnser(result))
                        for(i in 0 until result.data.size){
                            problemIdList.add(result.data[i].problem.id)
                        }
                        communityViewModel.problemIdList.postValue(problemIdList)
                        communityViewModel.problemAnswerList.postValue(problemAnswerList)
                    }
                }

            }catch (e: SocketTimeoutException){
                Toast.makeText(mainActivity,"似乎没有网络，无法连接服务器！",Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 根据接收到的问题及回答result，将其中从数据库中查询出的数据转化成ProblemAnswer适配器所需要的内容
     * 将问题逆序显示，这样就可实现默认时间排序，最后提问的显示在最前面（数据库提问时间越早越在前面）
     */
    fun resultToProblemAnser(result:Response<ProblemAnswer>):ArrayList<CommunityProblemAnswerItem>{
        val problemAnswerList=ArrayList<CommunityProblemAnswerItem>()
        for(i in 0 until result.data.size ){
            val problem=result.data[i].problem
            val answers=result.data[i].answers
            val problemTime=problem.time.substring(0,10)
            val problemContent=
                if(problem.content.length>35) problem.content.substring(0,35)+"..." else problem.content
            if(answers.size>0) {
                val new=answers.size-1
                val answerTime=answers[new].time.substring(0,10)
                val answerContent=
                    if (answers[new].content.length>35) answers[new].content.substring(0,35)+"..." else answers[new].content
                problemAnswerList.add(CommunityProblemAnswerItem(
                    problemContent,"提问于"+problemTime,answerContent,"回答于"+answerTime))
            }else{
                problemAnswerList.add(CommunityProblemAnswerItem(
                    problemContent,"提问于"+problemTime,"暂无回答",""))
            }
        }
        return problemAnswerList;
    }

    /**
     * 根据所选的groupItem在数据库中查询相应的问题项返回，此功能尚未实现，直接返回所有的问题项
     */
    /*
    private fun findGroupResult(groupItem:String):List<CommunityProblemAnswerItem>?{
        return communityViewModel.problemAnswerList.value
    }*/

    /**
     * 根据搜索文本框中用户输入的文本在数据库中查询相应的问题项，并修改viewModel中的problemAnswerList
     * 观察者观察到viewModel中的problemAnswerList内容变化后会自动在活动中将新的problemAnswerList内容加载到recycleView中
     * 此部分数据库查询功能尚未实现，直接添加一个问题项作为结果（事实上查询结果只会更少，此处仅供测试使用）
     */
    private fun findSearchResult(text:String){
        /*val problemAnswerList=communityViewModel.problemAnswerList.value as ArrayList<CommunityProblemAnswerItem>
        problemAnswerList.add(CommunityProblemAnswerItem("问：  分数估了430，没发挥好，最擅长英语，但考试也失利了，想学英语专业...",
            "2020.12.5提问","答：  同学你好！\n" + "高考是人生的一件大事，这是毫无疑...","2021.1.15回答"))
        thread{
            communityViewModel.problemAnswerList.postValue(problemAnswerList)
        }*/
        if(text.equals("")){
            Toast.makeText(mainActivity,"请在搜索框输入内容！",Toast.LENGTH_SHORT).show()
        }else {
            scope.launch(Dispatchers.Main) {
                try {
                    val problemAnswerList=if(time==1) ArrayList<CommunityProblemAnswerItem>() else communityViewModel.problemAnswerList.value!!
                    val problemIdList=if(time==1) ArrayList<Int>() else communityViewModel.problemIdList.value!!
                    val result = EUniversityNetwork.searchProblem(text,time);
                    when (result.code) {
                        ResultEnum.INPUT_IS_NULL.code -> {
                            Toast.makeText(mainActivity, result.msg, Toast.LENGTH_SHORT).show()
                        }
                        ResultEnum.FIND_PROBLEM_FAILE.code -> {
                            Toast.makeText(mainActivity, "抱歉，没有匹配到更多问题", Toast.LENGTH_SHORT).show()
                            if(time==1) {
                                communityViewModel.problemIdList.postValue(problemIdList)
                                communityViewModel.problemAnswerList.postValue(problemAnswerList)
                            }
                        }
                        ResultEnum.FIND_SUCCESS.code -> {
                            problemAnswerList.addAll(resultToProblemAnser(result))
                            for(i in 0 until result.data.size){
                                problemIdList.add(result.data[i].problem.id)
                            }
                            communityViewModel.problemIdList.postValue(problemIdList)
                            communityViewModel.problemAnswerList.postValue(problemAnswerList)
                        }
                    }

                } catch (e: SocketTimeoutException) {
                    Toast.makeText(mainActivity, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 根据用户选择的排序方法菜单项将查询的问题项进行排序，并修改viewModel中的problemAnswerList
     * 观察者观察到viewModel中的problemAnswerList内容变化后会自动在活动中将新的problemAnswerList内容加载到recycleView中
     */
    private fun findSortResult(text: String){
        /*val problemAnswerList=communityViewModel.problemAnswerList.value as ArrayList<CommunityProblemAnswerItem>
        problemAnswerList.add(CommunityProblemAnswerItem("问：  分数估了430，没发挥好，最擅长英语，但考试也失利了，想学英语专业...",
            "2020.12.5提问","答：  同学你好！\n" + "高考是人生的一件大事，这是毫无疑...","2021.1.15回答"))
        thread{
            communityViewModel.problemAnswerList.postValue(problemAnswerList)
        }*/
        if(text.equals("timeSort")){
            initProblemAnswerList()
        }else{
            scope.launch(Dispatchers.Main){
                try {
                    val problemAnswerList=if(time==1) ArrayList<CommunityProblemAnswerItem>() else communityViewModel.problemAnswerList.value!!
                    val problemIdList=if(time==1) ArrayList<Int>() else communityViewModel.problemIdList.value!!
                    val result:Response<ProblemAnswer>;
                    if(text.equals("qualitySort"))
                        result= EUniversityNetwork.qualitySortProblem(time)
                    else
                        result = EUniversityNetwork.comprehensiveSortProblem(time)
                    when(result.code){
                        ResultEnum.NO_PROBLEMS_IN_DATABASE.code->{
                            Toast.makeText(mainActivity,result.msg,Toast.LENGTH_SHORT).show()
                        }
                        ResultEnum.FIND_SUCCESS.code->{
                            problemAnswerList.addAll(resultToProblemAnser(result))
                            for(i in 0 until result.data.size){
                                problemIdList.add(result.data[i].problem.id)
                            }
                            communityViewModel.problemIdList.postValue(problemIdList)
                            communityViewModel.problemAnswerList.postValue(problemAnswerList)
                        }
                    }

                }catch (e: SocketTimeoutException){
                    Toast.makeText(mainActivity,"似乎没有网络，无法连接服务器！",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}