package com.example.euniversity.ui.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.euniversity.MainActivity
import com.example.euniversity.R
import com.example.euniversity.adapter.HomeUniversityAdapter
import com.example.euniversity.adapter.ImageAdapter
import com.example.euniversity.entity.CommunityProblemAnswerItem
import com.example.euniversity.entity.HomeUniversityItem
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.network.response.Response
import com.example.euniversity.network.response.University
import com.example.euniversity.utils.KeyBoardUtil
import com.example.euniversity.utils.ResultEnum
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.processor.RefreshProcessor
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class HomeFragment : Fragment() {
    private val TAG="HomeFragment"
    private lateinit var mainActivity: MainActivity
    private lateinit var homeViewModel: HomeViewModel

    private var time=1
    private var operation="init"

    private val job= Job()
    private val scope= CoroutineScope(job)

    private lateinit var allFilterList:ArrayList<ArrayList<CheckBox>>
    private lateinit var allFilterStringList:ArrayList<String>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if(activity!=null){
            mainActivity=activity as MainActivity
        }
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.universityIdList.postValue(ArrayList<Int>())
        homeViewModel.universityList.postValue(ArrayList())
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        //将图片配置到banner中实现图片轮播
        val banner=view.findViewById<Banner<Int,ImageAdapter>>(R.id.homeBanner)
        val adapter=ImageAdapter(initImages())
        banner.setAdapter(adapter)
            .addBannerLifecycleObserver(this)
            .setIndicator(CircleIndicator(context))
        //初始化学校列表
        time=1
        operation="init"
        operateUniversity("")

        //初始化筛选弹出窗口
        val popupWindow=initPopupWindow()

        //当viewModel的变量内容变化时触发观察者事件，将学校实例HomeUniversityItem适配到RecycleView中
        homeViewModel.universityList.observe(viewLifecycleOwner, Observer {
            val recyclerAdapter=HomeUniversityAdapter(it,mainActivity,homeViewModel.universityIdList.value!!)
            val universityList=view.findViewById<RecyclerView>(R.id.universityList)
            universityList.layoutManager=LinearLayoutManager(mainActivity)
            universityList.adapter=recyclerAdapter
            recyclerAdapter.notifyDataSetChanged()
        })

        val refreshLayout=view.findViewById<TwinklingRefreshLayout>(R.id.refreshLayout)
        refreshLayout.setAutoLoadMore(false)
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setOnRefreshListener(object :RefreshListenerAdapter(){
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                Handler().postDelayed(Runnable {
                    refreshLayout?.finishRefreshing()
                },1000)
            }

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                Handler().postDelayed(Runnable {
                    time+=1
                    operateUniversity(view.findViewById<EditText>(R.id.searchEditView).text.toString())
                    refreshLayout?.finishLoadmore()
                },1000)
            }
        } )

        val floatingActionButton=view.findViewById<FloatingActionButton>(R.id.floatingActionButton)

        val searchImage=view.findViewById<ImageView>(R.id.searchImage)
        val searchEditView=view.findViewById<EditText>(R.id.searchEditView)
        val filterImage=view.findViewById<ImageView>(R.id.filterImage)

        val onClick=View.OnClickListener {
            when(it.id){
                //点击浮动按钮时，recyclerView跳转到第0个项
                R.id.floatingActionButton->{
                    val recyclerView=view.findViewById<RecyclerView>(R.id.universityList)
                    val scrollView=view.findViewById<ScrollView>(R.id.scrollView)
                    recyclerView.smoothScrollToPosition(0)
                    scrollView.fullScroll(ScrollView.FOCUS_UP)
                }
                //根据搜索文本框中用户输入的文本在数据库中查询相应的大学
                R.id.searchImage->{
                    time=1
                    operation="search"
                    operateUniversity(searchEditView.text.toString())
                }
                //用户点击筛选图标时弹出窗口供用户选择筛选方式
                R.id.filterImage->{
                    val rootView=LayoutInflater.from(mainActivity).inflate(R.layout.home_fragment,null)
                    popupWindow.showAtLocation(rootView,Gravity.BOTTOM,0,0)
                }
            }
        }

        floatingActionButton.setOnClickListener(onClick)
        searchImage.setOnClickListener(onClick)
        filterImage.setOnClickListener(onClick)

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
        val imageResource=resources.obtainTypedArray(R.array.homeBannerImage)
        for (i in 0 until imageResource.length()){
            imageList.add(imageResource.getResourceId(i,0))
        }
        return imageList
    }

    /**
     * 初始化学校列表，从数据库中获取信息生成列表赋值给viewModel的universityList
     */
    /*private fun initUniversity(){
//        universityList.add(HomeUniversityItem(R.drawable.university2,"哈尔滨工业大学",
//            "985 211 工科 黑龙江 哈尔滨市 985 211 工科 黑龙江 哈尔滨市"))
        scope.launch(Dispatchers.Main){
            try {
                val result=EUniversityNetwork.findUniversity();
                displayUniversity(result)
            } catch (e: SocketTimeoutException) {
                Toast.makeText(mainActivity, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }*/

    /**
     * 根据搜索文本框中用户输入的文本在数据库中查询相应的大学，并修改viewModel中的universityList
     * 观察者观察到viewModel中的universityList内容变化后会自动在活动中将新的universityList内容加载到recycleView中
     */
    /*private fun findSearchResult(text:String){
        scope.launch(Dispatchers.Main){
            try {
                val result=EUniversityNetwork.searchUniversity(text);
                displayUniversity(result)
            } catch (e: SocketTimeoutException) {
                Toast.makeText(mainActivity, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }*/

    /**
     * 初始化弹出窗口，在用户点击筛选按钮时弹出筛选菜单窗口供用户进行筛选操作
     */
    private fun initPopupWindow():PopupWindow{
        val view=LayoutInflater.from(context).inflate(R.layout.home_filter_popupwindow,null,false)
        val popupWindow=PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true)

        //初始化筛选列表，获取筛选列表的所有checkbox的View即CheckBox类，配置到对应类变量中
        //将所有checkbox的text配置好相应文本
        initFilterArrayList(view)

        //所有checkbox的OnCheckedChangeListener
        val checkOnCheckedChanged=CompoundButton.OnCheckedChangeListener{buttonView, isChecked ->
            when(buttonView.id){
                R.id.universityType1,R.id.universityLevel1,R.id.educationLevel1,R.id.universityNature1,R.id.area1->{
                    unlimitedCheckboxChecked(isChecked,buttonView as CheckBox)
                }
                else->{
                    normalCheckboxChecked(isChecked,buttonView as CheckBox )
                }
            }
        }
        setCheckbox(checkOnCheckedChanged)

        val cancel=view.findViewById<TextView>(R.id.cancel)
        val reset=view.findViewById<Button>(R.id.reset)
        val determine=view.findViewById<Button>(R.id.determine)

        val onClick=View.OnClickListener {
            when(it.id){
                //点击取消时关闭popupWindow
                R.id.cancel->{
                    popupWindow.dismiss()
                }
                //点击重置按钮时将所有筛选列表的“不限”checkbox选中，触发OnCheckedChangeListener后会自动将其他checkbox的
                //ischecked属性设置为false
                R.id.reset->{
                    for(i in 0 until allFilterList.size){
                        allFilterList[i][0].isChecked=true
                    }
                }
                //点击筛选页面的确定选项时，根据所有checkbox的ischecked状态（分unlimitedCheckbox和normalCheckbox）
                //确定数据库查询的语句，获得院校列表postvalue到viewModel的universityList变量中，观察者观察到变量变化后会自动修改
                //recyclerView中的院校，此部分功能尚未实现，关闭popupWindow做测试使用
                R.id.determine->{
                    time=1
                    operation="filter"
                    operateUniversity("")
                    popupWindow.dismiss()
                }
            }
        }

        cancel.setOnClickListener(onClick)
        reset.setOnClickListener(onClick)
        determine.setOnClickListener(onClick)

        return popupWindow
    }

    /**
     *初始化筛选列表，获取筛选列表的所有checkbox的View即CheckBox类，配置到对应类变量中
     *将所有checkbox的text配置好相应文本
     * @param v 筛选页面形成的View
     */
    fun initFilterArrayList(v:View){
        val universityTypeList:ArrayList<CheckBox> = arrayListOf(v.findViewById(R.id.universityType1),
            v.findViewById(R.id.universityType2),
            v.findViewById(R.id.universityType3),
            v.findViewById(R.id.universityType4),
            v.findViewById(R.id.universityType5),
            v.findViewById(R.id.universityType6),
            v.findViewById(R.id.universityType7),
            v.findViewById(R.id.universityType8),
            v.findViewById(R.id.universityType9),
            v.findViewById(R.id.universityType10),
            v.findViewById(R.id.universityType11),
            v.findViewById(R.id.universityType12),
            v.findViewById(R.id.universityType13),
            v.findViewById(R.id.universityType14)
        )
        val universityLevelList:ArrayList<CheckBox> = arrayListOf(v.findViewById(R.id.universityLevel1),
            v.findViewById(R.id.universityLevel2),
            v.findViewById(R.id.universityLevel3)
        )
        val educationLevelList:ArrayList<CheckBox> = arrayListOf(v.findViewById(R.id.educationLevel1),
            v.findViewById(R.id.educationLevel2),
            v.findViewById(R.id.educationLevel3),
            v.findViewById(R.id.educationLevel4)
        )
        val universityNatureList:ArrayList<CheckBox> = arrayListOf(v.findViewById(R.id.universityNature1),
            v.findViewById(R.id.universityNature2),
            v.findViewById(R.id.universityNature3),
            v.findViewById(R.id.universityNature4)
        )
        val provinceList:ArrayList<CheckBox> = arrayListOf(v.findViewById(R.id.area1),
            v.findViewById(R.id.area2),
            v.findViewById(R.id.area3),
            v.findViewById(R.id.area4),
            v.findViewById(R.id.area5),
            v.findViewById(R.id.area6),
            v.findViewById(R.id.area7),
            v.findViewById(R.id.area8),
            v.findViewById(R.id.area9),
            v.findViewById(R.id.area10),
            v.findViewById(R.id.area11),
            v.findViewById(R.id.area12),
            v.findViewById(R.id.area13),
            v.findViewById(R.id.area14),
            v.findViewById(R.id.area15),
            v.findViewById(R.id.area16),
            v.findViewById(R.id.area17),
            v.findViewById(R.id.area18),
            v.findViewById(R.id.area19),
            v.findViewById(R.id.area20),
            v.findViewById(R.id.area21),
            v.findViewById(R.id.area22),
            v.findViewById(R.id.area23),
            v.findViewById(R.id.area24),
            v.findViewById(R.id.area25),
            v.findViewById(R.id.area26),
            v.findViewById(R.id.area27),
            v.findViewById(R.id.area28),
            v.findViewById(R.id.area29),
            v.findViewById(R.id.area30),
            v.findViewById(R.id.area31),
            v.findViewById(R.id.area32),
            v.findViewById(R.id.area33),
            v.findViewById(R.id.area34),
            v.findViewById(R.id.area35)
        )
        allFilterList= arrayListOf(universityTypeList,universityLevelList,educationLevelList,universityNatureList,provinceList)

        val universityTypeText= arrayListOf<String>("不限","理工类","综合类","语言类","艺术类","农林类","民族类",
                "医药类","师范类","财经类","体育类","政法类","军事类","其他")
        val universityLevelText= arrayListOf<String>("不限","普通本科","专科（高职）")
        val educationLevelText= arrayListOf<String>("不限","985","211","双一流")
        val universityNatureText= arrayListOf<String>("不限","公办","民办","中外合作办学")
        val provinceText= arrayListOf<String>("不限","北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江",
            "上海","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南","重庆",
            "四川","贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门")
        allFilterStringList= arrayListOf(getAllFilterString(universityTypeText),getAllFilterString(universityLevelText),
        getAllFilterString(educationLevelText),getAllFilterString(universityNatureText),getAllFilterString(provinceText))


        for (i in 0 until universityTypeList.size)  universityTypeList[i].text=universityTypeText[i]
        for (i in 0 until universityLevelList.size)  universityLevelList[i].text=universityLevelText[i]
        for (i in 0 until educationLevelList.size)  educationLevelList[i].text=educationLevelText[i]
        for (i in 0 until universityNatureList.size)  universityNatureList[i].text=universityNatureText[i]
        for (i in 0 until provinceList.size)  provinceList[i].text=provinceText[i]
    }

    /**
     * 点“不限”checkbox时进行相应操作，点完后“不限”checkbox的ischecked属性为true时
     * 将对应的筛选列表的其他checkbox的ischecked属性设置为false
     * 点完后“不限”checkbox的ischecked属性为false时,判断对应筛选列表的其他checkbox的ischecked属性是否都为false
     * 如果都为false则重新将“不限”checkbox的ischecked属性设置为true，不能让用户在该筛选列表下一个checkbox选项都不选
     * @param isChecked “不限”checkbox的ischecked属性
     * @param checkbox “不限”checkbox的View即CheckBox类
     */
    fun unlimitedCheckboxChecked(isChecked:Boolean,checkbox: CheckBox){
        val containedFilterList=judgeContainedFilterList(checkbox)
        if(isChecked){
            for (i in 1 until containedFilterList.size){
                containedFilterList[i].isChecked=false
            }
        }else{
            for (i in 1 until containedFilterList.size){
                if(containedFilterList[i].isChecked)
                    return
            }
            containedFilterList[0].isChecked=true
        }
    }

    /**
     * 点普通的checkbox时进行相应操作
     * 点完后，如果checkbox的ischecked属性为true，则将筛选列表下的“不限”checkbox的ischecked属性设置为false
     * 如果checkbox的ischecked属性为false，则判断该筛选列表的所有普通checkbox的ischecked属性是否都为false
     * 如果都为false则将该筛选列表的“不限”checkbox的ischecked属性设置为true
     * @param isChecked 普通checkbox的ischecked属性
     * @param checkbox 普通checkbox的View即CheckBox类
     */
    fun normalCheckboxChecked(isChecked: Boolean,checkbox: CheckBox){
        val containedFilterList=judgeContainedFilterList(checkbox)
        if (isChecked){
            containedFilterList[0].isChecked=false
        }else{
            for (i in 1 until containedFilterList.size){
                if(containedFilterList[i].isChecked)
                    return
            }
            containedFilterList[0].isChecked=true
        }
    }

    /**
     * 根据checkbox的View即CheckBox类返回其所在的筛选列表
     */
    fun judgeContainedFilterList(checkbox: CheckBox):ArrayList<CheckBox>{
        for (i in 0 until allFilterList.size){
            if (allFilterList[i].contains(checkbox))
                return allFilterList[i]
        }
        return ArrayList<CheckBox>()
    }

    /**
     * 设置所有checkbox的OnCheckedChangeListener
     */
    fun setCheckbox(checkOnCheckedChanged:CompoundButton.OnCheckedChangeListener){
        for(i in 0 until allFilterList.size){
            for(j in 0 until allFilterList[i].size) {
                allFilterList[i][j].setOnCheckedChangeListener(checkOnCheckedChanged)
            }
        }
    }

    /**
     *筛选菜单点击确定时进行筛选并将结果返回
     */
    /*fun filterUniversity(){
        val filterStringList=getFilterStringList()
        scope.launch(Dispatchers.Main){
            try {
                //由于filterStringList生成时顺序与filterUniversity函数对应参数不匹配，需要调换顺序将合适的参数传入函数
                val result=EUniversityNetwork.filterUniversity(filterStringList[0],
                    filterStringList[1],filterStringList[4],filterStringList[2],filterStringList[3]);
                displayUniversity(result)
            } catch (e: SocketTimeoutException) {
                Toast.makeText(mainActivity, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }*/

    /**
     * 除学历层次外，其他筛选类型选择不限时，各个类型的向服务器发送的筛选列表字符串即数据库查询时使用的筛选列表字符串
     */
    fun getAllFilterString(filterText:ArrayList<String>):String{
        var filterString=""
        for (i in 1 until filterText.size){
            filterString+="\""+filterText[i]+"\""+","
        }
        return filterString.substring(0,filterString.length-1)
    }

    /**
     * 根据弹出的筛选菜单中用户选择的checkbox，返回各个类型的向服务器发送的筛选列表字符串形成的
     * 字符串列表
     */
    fun getFilterStringList():ArrayList<String>{
        val filterStringList=ArrayList<String>()
        val list= arrayListOf<Int>(0,1,3,4)
        //除学历层次外的其他筛选类型直接返回数据库查询时需要使用的筛选列表字符串，eg"\"综合类\""
        for(i in list){
            var filterString=""
            for(checkbox in allFilterList[i]){
                if(checkbox.isChecked)
                    filterString+="\""+(checkbox.text.toString())+"\""+","
            }
            filterString=filterString.substring(0,filterString.length-1)
            if (filterString.equals("\"不限\"")){
                filterString=allFilterStringList[i]
            }
            filterStringList.add(filterString)
        }
        //学历层次数据库中有三个属性并且查询更复杂，直接返回用户选择的checkbox的text，中间用逗号隔开，
        var filterString=""
        for(checkbox in allFilterList[2]){
            if(checkbox.isChecked)
                filterString+=(checkbox.text.toString())+","
        }
        filterStringList.add(filterString.substring(0,filterString.length-1))
        return filterStringList
    }

    /**
     * 对院校进行的具体操作，初始化、筛选、查找等
     */
    fun operateUniversity(text: String){
        scope.launch(Dispatchers.Main){
            try {
                val result:Response<University>
                if(operation.equals("init")){
                    result=EUniversityNetwork.findUniversity(time);
                }else if(operation.equals("search")){
                    result=EUniversityNetwork.searchUniversity(text,time);
                }else {
                    val filterStringList=getFilterStringList()
                    //由于filterStringList生成时顺序与filterUniversity函数对应参数不匹配，需要调换顺序将合适的参数传入函数
                    result = EUniversityNetwork.filterUniversity(
                        filterStringList[0],
                        filterStringList[1],
                        filterStringList[4],
                        filterStringList[2],
                        filterStringList[3],
                        time
                    );
                }
                displayUniversityList(result)
            } catch (e: SocketTimeoutException) {
                Toast.makeText(mainActivity, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    /**
     * 通过LiveData的postValue显示初始化学校列表、查找学校、筛选学校后的RecyclerView
     */
    fun displayUniversityList(result:Response<University>){
        val universityIdList=if(time==1) ArrayList<Int>() else homeViewModel.universityIdList.value!!
        val universityList=if(time==1) ArrayList<HomeUniversityItem>() else homeViewModel.universityList.value!!
        when(result.code){
            ResultEnum.INPUT_IS_NULL.code,ResultEnum.FILTER_UNIVERSITY_FAILE.code,ResultEnum.NO_UNIVERSITYS_IN_DATABASE.code,
            ResultEnum.FIND_UNIVERSITY_FAILE.code->{
                Toast.makeText(mainActivity, result.msg, Toast.LENGTH_SHORT).show()
                if(time==1) {
                    homeViewModel.universityIdList.postValue(ArrayList<Int>())
                    homeViewModel.universityList.postValue(ArrayList<HomeUniversityItem>())
                }
            }
            ResultEnum.FIND_SUCCESS.code->{
                val universityListDB=result.data
                for (university in universityListDB){
                    universityIdList.add(university.id)
                    universityList.add(
                        HomeUniversityItem(university.logo,university.name,
                            university.type+" "+university.province+" "+university.city+" "
                                    +(if(university.f985.equals(1)) "985 " else "")+(if(university.f211.equals(1)) "211 " else "")
                                    +(if(university.dualClass.equals("双一流")) "双一流" else "")))
                }
                homeViewModel.universityIdList.postValue(universityIdList)
                homeViewModel.universityList.postValue(universityList)
            }
        }
    }
}