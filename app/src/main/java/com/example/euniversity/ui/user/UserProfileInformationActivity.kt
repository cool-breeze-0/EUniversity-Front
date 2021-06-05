package com.example.euniversity.ui.user

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.euniversity.R
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.utils.KeyBoardUtil
import com.example.euniversity.utils.ResultEnum
import kotlinx.android.synthetic.main.user_profile_information_activity.*
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class UserProfileInformationActivity : AppCompatActivity() {
    private val TAG="UserProfileInformationActivity"
    private val job= Job()
    private val scope= CoroutineScope(job)
    private lateinit var context:UserProfileInformationActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_information_activity)
        context=this
        val prefs=getSharedPreferences("user", Context.MODE_PRIVATE)
        val phone=prefs.getString("phone","")
        if(!phone.equals("")) {
            scope.launch(Dispatchers.Main) {
                try {
                    val result = EUniversityNetwork.findUserByPhone(phone!!)
                    when (result.code) {
                        ResultEnum.USER_NOT_EXIST.code -> {
                            Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                        }
                        ResultEnum.FIND_SUCCESS.code -> {
                            val userInformation = result.data[0]
                            if (!userInformation.province.equals("null")) {
                                editTextUserProfileInformationName.setText(userInformation.nikeName)
                                textViewUserProfileInformationGender.setText(if (userInformation.gender == 0) "男" else "女")
                                textViewUserProfileInformationProvince.setText(userInformation.province)
                                textViewUserProfileInformationCity.setText(userInformation.city)
                                textViewUserProfileInformationIdentity.setText(userInformation.identity)
                            }
                        }
                    }
                }catch (e: SocketTimeoutException){
                    Toast.makeText(context,"似乎没有网络，无法连接服务器！",Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this,"请登录后再进行操作！",Toast.LENGTH_SHORT).show()
        }

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
                val prefs=getSharedPreferences("user", Context.MODE_PRIVATE)
                val phone=prefs.getString("phone","")
                if(!phone.equals("")) {
                    val nickName = editTextUserProfileInformationName.text.toString()
                    val gender = textViewUserProfileInformationGender.text.toString()
                    val province = textViewUserProfileInformationProvince.text.toString()
                    val city = textViewUserProfileInformationCity.text.toString()
                    val identity = textViewUserProfileInformationIdentity.text.toString()
                    if (nickName.equals("") || gender.equals("") || province.equals("") || city.equals("") || identity.equals("")) {
                        Toast.makeText(this, "请完善个人信息！", Toast.LENGTH_SHORT).show()
                    } else {
                        val genderToInt=if(gender.equals("男"))0 else 1
                        scope.launch(Dispatchers.Main){
                            try {
                                val result = EUniversityNetwork.updateProfileInformation(
                                    phone!!, nickName, genderToInt, province, city, identity
                                )
                                when (result.code) {
                                    ResultEnum.INPUT_IS_NULL.code, ResultEnum.USER_NOT_EXIST.code,
                                    ResultEnum.UPDATE_PROFILE_INFORMATION_FAILD.code -> {
                                        Toast.makeText(context, result.msg, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    ResultEnum.UPDATE_PROFILE_INFORMATION_SUCCESS.code -> {
                                        Toast.makeText(context, result.msg, Toast.LENGTH_SHORT)
                                            .show()
                                        context.finish()
                                    }
                                }
                            } catch (e: SocketTimeoutException) {
                                Toast.makeText(
                                    context,
                                    "似乎没有网络，无法连接服务器！",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }else{
                    Toast.makeText(this,"请登录后再进行操作！",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev?.action== MotionEvent.ACTION_DOWN){
            val v=currentFocus
            if(KeyBoardUtil.isShouldHideInput(v, ev)){
                KeyBoardUtil.closeKeybord(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}