package com.example.euniversity.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.euniversity.R
import kotlinx.android.synthetic.main.user_profile_information_activity.*

class UserProfileInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_information_activity)

    }

    fun onClick(v:View){
        when(v.id){
            //返回按钮功能与返回键一致
            R.id.userProfileInformationBackButton->{
                finish()
            }
            //点击性别文本框弹出对话框，给出选项供用户选择
            R.id.textViewUserProfileInformationGender->{
                val gender= arrayOf("男","女")
                AlertDialog.Builder(this).apply {
                    setSingleChoiceItems(gender,0,{dialog, which ->
                        textViewUserProfileInformationGender.setText(gender.get(which))
                        dialog.dismiss()
                    })
                    create()
                    show()
                }
            }
            //点击省份文本框弹出对话框，给出选项供用户选择
            R.id.textViewUserProfileInformationProvince->{
                val province= arrayOf("河北省","山西省","辽宁省","吉林省","黑龙江省","江苏省","浙江省","安徽省","福建省","江西省",
                "山东省","河南省","湖北省","湖南省","广东省","海南省","四川省","贵州省","云南省","陕西省","甘肃省","青海省","台湾省"
                ,"内蒙古","广西","西藏","宁夏","新疆","北京市","天津市","上海市","重庆市","香港","澳门特别行政区")
                AlertDialog.Builder(this).apply {
                    setSingleChoiceItems(province,0,{dialog, which ->
                        textViewUserProfileInformationProvince.setText(province.get(which))
                        dialog.dismiss()
                    })
                    create()
                    show()
                }
            }
            //点击用户身份文本框弹出对话框，给出选项供用户选择
            R.id.textViewUserProfileInformationIdentity->{
                val identity= arrayOf("高中生","家长","在校大学生","老师")
                AlertDialog.Builder(this).apply {
                    setSingleChoiceItems(identity,0,{dialog, which ->
                        textViewUserProfileInformationIdentity.setText(identity.get(which))
                        dialog.dismiss()
                    })
                    create()
                    show()
                }
            }
            //先判断用户数据有效性，将用户信息提交到数据库中，目前未完成这部分工作直接返回
            R.id.finishButton->{
                finish()
            }
        }
    }
}