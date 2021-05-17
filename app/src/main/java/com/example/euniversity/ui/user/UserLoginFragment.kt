package com.example.euniversity.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.euniversity.MainActivity
import com.example.euniversity.R
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.utils.ActivityUtil
import com.example.euniversity.utils.NetworkStatus
import com.example.euniversity.utils.ResultEnum
import kotlinx.coroutines.*
import java.net.SocketTimeoutException


class UserLoginFragment : Fragment() {
    private val TAG="UserLoginFragment"
    private lateinit var userAccountActivity: UserAccountActivity
    private val job=Job()
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
        val view=inflater.inflate(R.layout.user_login_fragment, container, false)

        //为userLoginFragment中的各个组件设置点击事件
        val userAccountForgetPassword:TextView=view.findViewById(R.id.userAccountForgetPassword)
        val userAccountRegister:TextView=view.findViewById(R.id.userAccountRegister)
        val userProtocol=view.findViewById<TextView>(R.id.userProtocol)
        val privatePolicy=view.findViewById<TextView>(R.id.privatePolicy)
        val loginButton:Button=view.findViewById(R.id.loginButton)
        val onClick=View.OnClickListener {
            when(it.id){
                //点击注册新用户，替换到注册用户碎片
                R.id.userAccountRegister->{
                    ActivityUtil.replaceFragment(userAccountActivity,R.id.userAccountFragment,UserRegisterFragment(),false)
                }
                //点击忘记密码，替换到找回密码碎片
                R.id.userAccountForgetPassword->{
                    ActivityUtil.replaceFragment(userAccountActivity,R.id.userAccountFragment,UserRetrievePasswordFragment(),false)
                }
                //点击用户协议或隐私政策可以进入帮助页面查看用户协议或隐私政策
                R.id.userProtocol,R.id.privatePolicy->{
                    val intent=Intent(userAccountActivity,UserApplicationContentActivity::class.java)
                    intent.putExtra("userApplicationContentFragment","help")
                    startActivity(intent)
                }
                //点击立即登录，根据用户输入的手机号，密码，当成功时登录，失败时给出提示，此部分功能目前未实现
                R.id.loginButton->{
                    val phone=view.findViewById<EditText>(R.id.phone).text.toString()
                    val password=view.findViewById<EditText>(R.id.password).text.toString()
                    val checkBox=view.findViewById<CheckBox>(R.id.checkBox)
                    Log.e(TAG,password)
                    if(!checkBox.isChecked){
                        Toast.makeText(userAccountActivity,"请同意用户协议和隐私政策！", Toast.LENGTH_SHORT).show()
                    }else if (phone.equals("")){
                        Toast.makeText(userAccountActivity,"手机号不能为空！",Toast.LENGTH_SHORT).show()
                    }else if(password.equals("")){
                        Toast.makeText(userAccountActivity,"密码不能为空！",Toast.LENGTH_SHORT).show()
                    }else if (phone.length!=11){
                        Toast.makeText(userAccountActivity,"请输入正确的手机号！",Toast.LENGTH_SHORT).show()
                    }else{
                        scope.launch(Dispatchers.Main){
                            try {
                                val result = EUniversityNetwork.login(phone, password)
                                when (result.code) {
                                    ResultEnum.PASSWORD_IS_WRONG.code, ResultEnum.INPUT_IS_NULL.code, ResultEnum.USER_NOT_EXIST.code -> {
                                        Toast.makeText(
                                            userAccountActivity,
                                            result.msg,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    ResultEnum.LOGIN_SUCCESS.code -> {
                                        val prefsEditor = userAccountActivity.getSharedPreferences(
                                            "user",
                                            Context.MODE_PRIVATE
                                        ).edit()
                                        prefsEditor.putString("phone", phone)
                                        prefsEditor.apply()
                                        Toast.makeText(
                                            userAccountActivity,
                                            result.msg,
                                            Toast.LENGTH_SHORT
                                        ).show()
//                                    delay(2000)
                                        val intent =
                                            Intent(userAccountActivity, MainActivity::class.java)
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                    }
                                }
                            }catch (e: SocketTimeoutException){
                                Toast.makeText(userAccountActivity,"似乎没有网络，无法连接服务器！",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        loginButton.setOnClickListener(onClick)
        userAccountForgetPassword.setOnClickListener(onClick)
        userAccountRegister.setOnClickListener(onClick)
        userProtocol.setOnClickListener(onClick)
        privatePolicy.setOnClickListener(onClick)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}