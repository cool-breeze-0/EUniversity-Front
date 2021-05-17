package com.example.euniversity.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.euniversity.R
import com.example.euniversity.entity.HomeUniversityItem
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.network.ServiceCreator
import com.example.euniversity.utils.ActivityUtil
import com.example.euniversity.utils.ResultEnum
import kotlinx.android.synthetic.main.home_university_details_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomeUniversityDetailsActivity : AppCompatActivity() {
    private val job= Job()
    private val scope= CoroutineScope(job)
    private lateinit var context:HomeUniversityDetailsActivity
    private var universityId=30
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_university_details_activity)

        context=this

        universityId=intent.getIntExtra("universityId",30)
        //将从主页的recycleView的Adapter中传来数据在活动中进行展示
//        Glide.with(this)
//            .load(ServiceCreator.getBASE_URL()+homeUniversityItem.universityLogo)
//            .into(universityImage)
        scope.launch(Dispatchers.Main){
            try {
                val result= EUniversityNetwork.findUniversityById(universityId);
                when(result.code){
                    ResultEnum.UNIVERSITY_NOT_EXIST.code->{
                        Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                    }
                    ResultEnum.FIND_SUCCESS.code->{
                        val university=result.data[0]
                        Glide.with(context)
                            .load(ServiceCreator.getBASE_URL()+university.logo)
                            .into(universityLogo)
                        universityName.text=university.name
                        level.text="院校层次："+university.level
                        type.text="院校类型："+university.type
                        nature.text="办学性质："+university.nature
                        educationLevel.setText("学历层次："+(if(university.f985.equals(1)) "985 " else "")+
                                (if(university.f211.equals(1)) "211 " else "")+(if(university.dualClass.equals("双一流")) "双一流" else ""))
                        provinceCity.text="院校地址："+university.province+" "+university.city
                        url.text="院校官网："+university.url
                        if(!university.image.equals("")) {
                            universityImage.layoutParams.height=(context.windowManager.defaultDisplay.width*0.625).toInt()
                            Glide.with(context)
                                .load(ServiceCreator.getBASE_URL()+university.image)
                                .into(universityImage)
                        }
                        changeFragment("introductionFragment")
                    }
                }
            } catch (e: SocketTimeoutException) {
                Toast.makeText(context, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    fun onClick(v: View){
        when(v.id){
            R.id.homeUniversityDetailsBackButton->{
                finish()
            }
            R.id.universityIntroduction->{
                changeFragment("introductionFragment")
            }
            R.id.universityScore->{
                changeFragment("scoreFragment")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun getUniversityId():Int{
        return this.universityId
    }

    fun changeFragment(fragment:String){
        when(fragment){
            "introductionFragment"->{
                universityScore.setTextColor(android.graphics.Color.BLACK)
                universityScore.setBackgroundColor(resources.getColor(R.color.white))

                universityIntroduction.setTextColor(resources.getColor(R.color.white))
                universityIntroduction.setBackgroundColor(resources.getColor(R.color.home_university_details_fragment_choice))
                ActivityUtil.replaceFragment(context,R.id.universityDetailsFragment,HomeUniversityIntroductionFragment(),false)
            }
            "scoreFragment"->{
                universityIntroduction.setTextColor(android.graphics.Color.BLACK)
                universityIntroduction.setBackgroundColor(resources.getColor(R.color.white))

                universityScore.setTextColor(resources.getColor(R.color.white))
                universityScore.setBackgroundColor(resources.getColor(R.color.home_university_details_fragment_choice))
                ActivityUtil.replaceFragment(context,R.id.universityDetailsFragment,HomeUniversityScoreFragment(),false)
            }
        }

    }
}