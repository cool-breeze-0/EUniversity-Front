package com.example.euniversity.ui.predict

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.euniversity.MainActivity
import com.example.euniversity.R
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import kotlinx.android.synthetic.main.user_profile_information_activity.*

class PredictFragment : Fragment() {
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
        val predictAdmissionDivisionInput:TextView=view.findViewById(R.id.predictAdmissionDivisionInput)
        val predictBirthplaceInput:TextView=view.findViewById(R.id.predictBirthplaceInput)
        val predictButton:Button=view.findViewById(R.id.predictButton)
        val onClick=View.OnClickListener {
            when(it.id){
                R.id.predictAdmissionDivisionInput->{
                    val admissionDivision= arrayOf("文科","理科")
                    AlertDialog.Builder(mainActivity).apply {
                        setSingleChoiceItems(admissionDivision,0,{dialog, which ->
                            predictAdmissionDivisionInput.setText(admissionDivision.get(which))
                            dialog.dismiss()
                        })
                        create()
                        show()
                    }
                }
                R.id.predictBirthplaceInput->{
                    val birthplace= arrayOf("河北省","山西省","辽宁省","吉林省","黑龙江省","江苏省","浙江省","安徽省","福建省","江西省",
                        "山东省","河南省","湖北省","湖南省","广东省","海南省","四川省","贵州省","云南省","陕西省","甘肃省","青海省","台湾省"
                        ,"内蒙古","广西","西藏","宁夏","新疆","北京市","天津市","上海市","重庆市","香港","澳门特别行政区")
                    androidx.appcompat.app.AlertDialog.Builder(mainActivity).apply {
                        setSingleChoiceItems(birthplace,0,{dialog, which ->
                            predictBirthplaceInput.setText(birthplace.get(which))
                            dialog.dismiss()
                        })
                        create()
                        show()
                    }
                }
                R.id.predictButton->{
                    val intent= Intent(mainActivity,PredictResultActivity::class.java)
                    //根据用户输入发送请求给服务器获取预测的概率返回整数为所占百分比，此功能尚未实现，用98作为测试结果使用
                    intent.putExtra("result",98)
                    startActivity(intent)
                }
            }
        }
        predictAdmissionDivisionInput.setOnClickListener(onClick)
        predictBirthplaceInput.setOnClickListener(onClick)
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
}