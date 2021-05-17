package com.example.euniversity.ui.predict

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.euniversity.MainActivity
import com.example.euniversity.R
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.utils.PredictUtil
import com.example.euniversity.utils.ResultEnum
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import kotlinx.android.synthetic.main.user_profile_information_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class PredictFragment : Fragment() {
    private val job= Job()
    private val scope= CoroutineScope(job)
    private lateinit var mainActivity: MainActivity
    private lateinit var predictViewModel: PredictViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if(activity!=null){
            mainActivity=activity as MainActivity
        }
        predictViewModel =
                ViewModelProviders.of(this).get(PredictViewModel::class.java)
        val view = inflater.inflate(R.layout.predict_fragment, container, false)

        //为各个组件添加点击事件
        val admissionDivisionInput:TextView=view.findViewById(R.id.admissionDivisionInput)
        val provinceInput:TextView=view.findViewById(R.id.provinceInput)
        val predictButton:Button=view.findViewById(R.id.predictButton)
        val onClick=View.OnClickListener {
            when(it.id){
                R.id.admissionDivisionInput->{
                    val admissionDivision= arrayOf("综合","文科","理科")
                    AlertDialog.Builder(mainActivity).apply {
                        setSingleChoiceItems(admissionDivision,0,{dialog, which ->
                            admissionDivisionInput.setText(admissionDivision.get(which))
                            dialog.dismiss()
                        })
                        create()
                        show()
                    }
                }
                R.id.provinceInput->{
                    val birthplace= arrayOf("北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江",
                        "上海","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南","重庆",
                        "四川","贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门")
                    androidx.appcompat.app.AlertDialog.Builder(mainActivity).apply {
                        setSingleChoiceItems(birthplace,0,{dialog, which ->
                            provinceInput.setText(birthplace.get(which))
                            dialog.dismiss()
                        })
                        create()
                        show()
                    }
                }
                R.id.predictButton-> {
                    val universityInput =
                        view.findViewById<EditText>(R.id.universityInput).text.toString()
                    val scoreInput = view.findViewById<EditText>(R.id.scoreInput).text.toString()
                    if (universityInput.equals("")) {
                        Toast.makeText(context, "请输入院校全称！", Toast.LENGTH_SHORT).show()
                    } else if (scoreInput.equals("")) {
                        Toast.makeText(context, "请输入预估分数！", Toast.LENGTH_SHORT).show()
                    } else if (admissionDivisionInput.equals("")) {
                        Toast.makeText(context, "请选择录取科类！", Toast.LENGTH_SHORT).show()
                    }else if (provinceInput.equals("")) {
                        Toast.makeText(context, "请选择生源地！", Toast.LENGTH_SHORT).show()
                    }else {
                        scope.launch(Dispatchers.Main) {
                            try {
                                val universityIdResult =
                                    EUniversityNetwork.findUniversityIdByName(universityInput)
                                when(universityIdResult.code){
                                    ResultEnum.INPUT_IS_NULL.code,ResultEnum.UNIVERSITY_NOT_EXIST.code->{
                                        Toast.makeText(context, universityIdResult.msg, Toast.LENGTH_SHORT).show()
                                    }
                                    ResultEnum.FIND_SUCCESS.code->{
                                        val universityId=universityIdResult.data[0]
                                        val result = EUniversityNetwork.findUniversityScore(
                                            universityId,provinceInput.text.toString(), admissionDivisionInput.text.toString())
                                        when (result.code) {
                                            ResultEnum.INPUT_IS_NULL.code -> {
                                                Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                                            }
                                            ResultEnum.FIND_SUCCESS.code -> {
                                                val grades=result.data
                                                if(grades[0].equals("—")||grades[1].equals("—")||grades[2].equals("—")){
                                                    Toast.makeText(context,
                                                        "该校近几年在此省份没有完整的此科类的招录数据,无法进行预测，" +
                                                                "可能由于高考改革所致",
                                                    Toast.LENGTH_SHORT).show()
                                                }else{
                                                    val p=PredictUtil.getPredictResult(grades,scoreInput.toInt())
                                                    val intent = Intent(mainActivity, PredictResultActivity::class.java)
                                                    //根据用户输入发送请求给服务器获取预测的概率返回整数为所占百分比，此功能尚未实现，用98作为测试结果使用
                                                    intent.putExtra("result", String.format("%2f",p*100))
                                                    startActivity(intent)
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (e: SocketTimeoutException) {
                                Toast.makeText(context, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
        admissionDivisionInput.setOnClickListener(onClick)
        provinceInput.setOnClickListener(onClick)
        predictButton.setOnClickListener(onClick)
/*
//        val refreshLayout: TwinklingRefreshLayout =view.findViewById(R.id.refreshLayout)
//        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter(){
//            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
//                Handler().postDelayed({
//                    refreshLayout?.finishRefreshing()
//                },2000)
//            }
//
//            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
//                Handler().postDelayed({
//                    refreshLayout?.finishLoadmore()
//                },2000)
//            }
//        })
*/
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}