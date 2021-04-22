package com.example.euniversity.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.euniversity.MainActivity
import com.example.euniversity.R
import com.example.euniversity.utils.ActivityUtil


class UserLoginFragment : Fragment() {
    private val TAG="UserLoginFragment"
    private lateinit var userAccountActivity: UserAccountActivity
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
        val loginButton:Button=view.findViewById(R.id.loginButton)
        val getVerificationCodeTextView:TextView=view.findViewById(R.id.getVerificationCodeTextView)
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
                //点击立即登录，根据用户输入的手机号，密码，验证码进行验证，当成功时登录，失败时给出提示，此部分功能目前未实现
                R.id.loginButton->{
                    userAccountActivity.finish()
                }
                //点击获取验证码，根据用户输入的手机号，使用商业的验证码发送消息给用户手机，此部分功能目前未实现
                R.id.getVerificationCodeTextView->{
                    Log.e(TAG,"发送验证码")
                }
            }
        }
        getVerificationCodeTextView.setOnClickListener(onClick)
        loginButton.setOnClickListener(onClick)
        userAccountForgetPassword.setOnClickListener(onClick)
        userAccountRegister.setOnClickListener(onClick)

        return view
    }

}