package com.example.euniversity.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.euniversity.R
import com.example.euniversity.utils.ActivityUtil

class UserChangePasswordFragment : Fragment() {
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
        val view=inflater.inflate(R.layout.user_change_password_fragment, container, false)

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
                    userAccountActivity.finish()
                }
            }
        }
        finishButton.setOnClickListener(onClick)
        userAccountForgetPassword.setOnClickListener(onClick)

        return view
    }
}