package com.example.euniversity.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.euniversity.R
import com.example.euniversity.entity.HomeUniversityItem
import kotlinx.android.synthetic.main.home_university_details_activity.*

class HomeUniversityDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_university_details_activity)

        //getParcelableExtra生成数据类型为Parcelable？类型，因为知道传过来的就是HomeUniversityItem类型
        //因此转化为HomeUniversityItem类型，这样才能调用里面的属性
        val homeUniversityItem=intent.getParcelableExtra<HomeUniversityItem>("homeUniversityItem") as HomeUniversityItem
        val universityIntroductionData=intent.getStringExtra("universityIntroduction")

        //将从主页的recycleView的Adapter中传来数据在活动中进行展示
        universityImage.setImageResource(homeUniversityItem.universityImage)
        universityName.text=homeUniversityItem.universityName
        universityAttribute.text=homeUniversityItem.universityAttribute
        universityIntroduction.text=universityIntroductionData

    }
    fun onClick(v: View){
        when(v.id){
            R.id.homeUniversityDetailsBackButton->{
                finish()
            }
        }
    }
}