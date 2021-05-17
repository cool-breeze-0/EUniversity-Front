package com.example.euniversity.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.euniversity.R
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.network.ServiceCreator
import com.example.euniversity.utils.ResultEnum
import kotlinx.android.synthetic.main.home_university_details_activity.*
import kotlinx.android.synthetic.main.user_profile_information_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomeUniversityScoreFragment : Fragment() {
    private val job= Job()
    private val scope= CoroutineScope(job)
    private lateinit var homeUniversityDetailsActivity: HomeUniversityDetailsActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(activity!=null){
            homeUniversityDetailsActivity=activity as HomeUniversityDetailsActivity
        }
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.home_university_score_fragment, container, false)

        val province=view.findViewById<TextView>(R.id.province)
        province.text="北京"
        val division=view.findViewById<TextView>(R.id.division)
        division.text="综合"

        //初始化默认获取北京综合的分数展示
        updateScore("北京","综合",view)

        val provinceIcon=view.findViewById<ImageView>(R.id.provinceIcon)
        val divisionIcon=view.findViewById<ImageView>(R.id.divisionIcon)
        val onClick=View.OnClickListener {
            when(it.id){
                R.id.province,R.id.provinceIcon->{
                    val provinceList= arrayOf("北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江",
                        "上海","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南","重庆",
                        "四川","贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门")
                    AlertDialog.Builder(homeUniversityDetailsActivity).apply {
                        setSingleChoiceItems(provinceList,0,{dialog, which ->
                            province.setText(provinceList.get(which))
                            updateScore(province.text.toString(),division.text.toString(),view)
                            dialog.dismiss()
                        })
                        create()
                        show()
                    }
                }
                R.id.division,R.id.divisionIcon->{
                    val divisionList= arrayOf("综合","理科","文科")
                    AlertDialog.Builder(homeUniversityDetailsActivity).apply {
                        setSingleChoiceItems(divisionList,0,{dialog, which ->
                            division.setText(divisionList.get(which))
                            updateScore(province.text.toString(),division.text.toString(),view)
                            dialog.dismiss()
                        })
                        create()
                        show()
                    }
                }
            }
        }
        province.setOnClickListener(onClick)
        provinceIcon.setOnClickListener(onClick)
        division.setOnClickListener(onClick)
        divisionIcon.setOnClickListener(onClick)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    /**
     * 根据省份和录取科类以及所在的学校ID获得近三年的最低录取分数的数据显示在列表中
     */
    fun updateScore(province:String,division:String,view:View){
        scope.launch(Dispatchers.Main){
            try {
                val result= EUniversityNetwork.findUniversityScore(homeUniversityDetailsActivity.getUniversityId(),
                province, division);
                when(result.code){
                    ResultEnum.INPUT_IS_NULL.code->{
                        Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                    }
                    ResultEnum.FIND_SUCCESS.code->{
                        if(result.data[0].equals("—")&&result.data[1].equals("—")&&result.data[1].equals("—")){
                            Toast.makeText(homeUniversityDetailsActivity,"该校在此省份似乎没有招录此科类的考生",Toast.LENGTH_SHORT).show()
                        }
                        val grade1=view.findViewById<TextView>(R.id.grade1)
                        val grade2=view.findViewById<TextView>(R.id.grade2)
                        val grade3=view.findViewById<TextView>(R.id.grade3)
                        grade1.text=result.data[0]
                        grade2.text=result.data[1]
                        grade3.text=result.data[2]
                    }
                }
            } catch (e: SocketTimeoutException) {
                Toast.makeText(context, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}