package com.example.euniversity.ui.user

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.euniversity.R
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.network.response.Like
import com.example.euniversity.network.response.Response
import com.example.euniversity.network.response.User
import com.example.euniversity.utils.ResultEnum
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class UserRegisterFragment : Fragment() {
    private lateinit var userAccountActivity:UserAccountActivity
    private val TAG="UserRegisterFragment"
    private val job = Job()
    private val scope= CoroutineScope(job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(activity!=null){
            userAccountActivity=activity as UserAccountActivity
        }
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.user_register_fragment, container, false)

        val userProtocol=view.findViewById<TextView>(R.id.userProtocol)
        val privatePolicy=view.findViewById<TextView>(R.id.privatePolicy)
        val getVerificationCodeTextView:TextView=view.findViewById(R.id.getVerificationCodeTextView)
        val registerButton:Button=view.findViewById(R.id.registerButton)
        val onClick=View.OnClickListener {
            when(it.id){
                //点击获取验证码，根据用户输入的手机号，使用商业的验证码发送消息给用户手机，此部分功能目前未实现
                R.id.getVerificationCodeTextView->{
                    Log.e(TAG,"发送验证码")
                }
                //点击用户协议或隐私政策可以进入帮助页面查看用户协议或隐私政策
                R.id.userProtocol,R.id.privatePolicy->{
                    val intent= Intent(userAccountActivity,UserApplicationContentActivity::class.java)
                    intent.putExtra("userApplicationContentFragment","help")
                    startActivity(intent)
                }
                //点击立即注册，根据用户输入的手机号，密码，验证码进行验证，当成功时注册，失败时给出提示，验证码功能目前未实现
                R.id.registerButton->{
                    val phone=view.findViewById<EditText>(R.id.phone).text.toString()
                    //val verificationCode=view.findViewById<EditText>(R.id.verificationCode).text.toString()
                    val password=view.findViewById<EditText>(R.id.password).text.toString()
                    val password2=view.findViewById<EditText>(R.id.password2).text.toString()
                    val checkbox=view.findViewById<CheckBox>(R.id.checkbox)
                    if(!checkbox.isChecked){
                        Toast.makeText(userAccountActivity,"请同意用户协议和隐私政策！",Toast.LENGTH_SHORT).show()
                    }else if (phone.equals("")){
                        Toast.makeText(userAccountActivity,"手机号不能为空！",Toast.LENGTH_SHORT).show()
                    }else if(password.equals("")){
                        Toast.makeText(userAccountActivity,"密码不能为空！",Toast.LENGTH_SHORT).show()
                    }else if(!password2.equals(password)){
                        Toast.makeText(userAccountActivity,"两次输入的密码不一致！",Toast.LENGTH_SHORT).show()
                    }else if (phone.length!=11){
                        Toast.makeText(userAccountActivity,"请输入正确的手机号！",Toast.LENGTH_SHORT).show()
                    }else{
                        scope.launch(Dispatchers.Main) {
                            try {
                                val result = EUniversityNetwork.register(phone, password)
                                when (result.code) {
                                    ResultEnum.INPUT_IS_NULL.code, ResultEnum.USER_IS_EXIST.code, ResultEnum.REGISTER_FAILD.code -> {
                                        Toast.makeText(
                                            userAccountActivity,
                                            result.msg,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    ResultEnum.REGISTER_SUCCESS.code -> {
                                        Toast.makeText(
                                            userAccountActivity,
                                            result.msg + "，请登陆！",
                                            Toast.LENGTH_SHORT
                                        ).show()
//                                    delay(2000)
                                        userAccountActivity.finish()
                                    }
                                }
                                Toast.makeText(userAccountActivity, result.msg, Toast.LENGTH_SHORT)
                                    .show()
                            } catch (e: SocketTimeoutException) {
                                Toast.makeText(
                                    userAccountActivity,
                                    "似乎没有网络，无法连接服务器！",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
        getVerificationCodeTextView.setOnClickListener(onClick)
        registerButton.setOnClickListener(onClick)
        userProtocol.setOnClickListener(onClick)
        privatePolicy.setOnClickListener(onClick)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}