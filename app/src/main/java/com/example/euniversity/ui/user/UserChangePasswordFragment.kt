package com.example.euniversity.ui.user

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.euniversity.R
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.utils.ActivityUtil
import com.example.euniversity.utils.LimitInputTextWeather
import com.example.euniversity.utils.PasswordUtil
import com.example.euniversity.utils.ResultEnum
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class UserChangePasswordFragment : Fragment() {
    private val TAG="UserLoginFragment"
    private lateinit var userAccountActivity: UserAccountActivity
    private val job= Job()
    private val scope= CoroutineScope(job)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(activity!=null){
            userAccountActivity=activity as UserAccountActivity
        }
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.user_change_password_fragment, container, false)

        //设置密码不能输入中文
        val passwordEditText=view.findViewById<EditText>(R.id.password)
        passwordEditText.addTextChangedListener(LimitInputTextWeather(passwordEditText))
        val newPasswordEditText=view.findViewById<EditText>(R.id.newPassword)
        newPasswordEditText.addTextChangedListener(LimitInputTextWeather(newPasswordEditText))
        val newPasswordEditText2=view.findViewById<EditText>(R.id.newPassword2)
        newPasswordEditText2.addTextChangedListener(LimitInputTextWeather(newPasswordEditText2))

        val finishButton:Button =view.findViewById(R.id.finishButton)
        val userAccountForgetPassword:TextView=view.findViewById(R.id.userAccountForgetPassword)
        val onClick=View.OnClickListener {
            when(it.id){
                //点击忘记密码，替换到找回密码碎片
                R.id.userAccountForgetPassword->{
                    ActivityUtil.replaceFragment(userAccountActivity,R.id.userAccountFragment,UserRetrievePasswordFragment(),false)
                }
                //点击完成，根据用户输入的原密码、新密码进行验证，当成功时修改密码，失败时给出提示，此部分功能目前未实现
                R.id.finishButton->{
                    //用户登录后才能修改密码
                    val prefs =  userAccountActivity.getSharedPreferences("user", Context.MODE_PRIVATE)
                    val phone = prefs.getString("phone", "")
                    if (!phone.equals("")) {
                        val password = view.findViewById<EditText>(R.id.password).text.toString()
                        val newPassword =view.findViewById<EditText>(R.id.newPassword).text.toString()
                        val newPassword2 =view.findViewById<EditText>(R.id.newPassword2).text.toString()
                        if (password.equals("")) {
                            Toast.makeText(userAccountActivity, "原密码不能为空！", Toast.LENGTH_SHORT).show()
                        } else if (newPassword.equals("")) {
                            Toast.makeText(userAccountActivity, "新密码不能为空！", Toast.LENGTH_SHORT).show()
                        }else if(password.length<6||password.length>6){
                            Toast.makeText(userAccountActivity,"新密码长度必须在6-20位！",Toast.LENGTH_SHORT).show()
                        } else if (!newPassword.equals(newPassword2)) {
                            Toast.makeText(userAccountActivity, "两次输入的新密码不一致！", Toast.LENGTH_SHORT).show()
                        } else {
                            scope.launch(Dispatchers.Main) {
                                try {
                                    val passwordMD5= PasswordUtil.encode(password)
                                    val newPasswordMD5= PasswordUtil.encode(newPassword)
                                    val result = EUniversityNetwork.changePassword(
                                        phone!!,
                                        passwordMD5,
                                        newPasswordMD5
                                    )
                                    when (result.code) {
                                        ResultEnum.INPUT_IS_NULL.code, ResultEnum.USER_NOT_EXIST.code, ResultEnum.PASSWORD_IS_WRONG.code -> {
                                            Toast.makeText(
                                                userAccountActivity,
                                                result.msg,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        ResultEnum.CHANGE_PASSWORD_SUCCESS.code -> {
                                            Toast.makeText(
                                                userAccountActivity,
                                                result.msg,
                                                Toast.LENGTH_SHORT
                                            ).show()
//                                        delay(2000)
                                            userAccountActivity.finish()
                                        }
                                    }
                                }catch (e: SocketTimeoutException){
                                    Toast.makeText(userAccountActivity,"似乎没有网络，无法连接服务器！",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }else{
                        Toast.makeText(userAccountActivity,"请登录后再进行操作！",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        finishButton.setOnClickListener(onClick)
        userAccountForgetPassword.setOnClickListener(onClick)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}