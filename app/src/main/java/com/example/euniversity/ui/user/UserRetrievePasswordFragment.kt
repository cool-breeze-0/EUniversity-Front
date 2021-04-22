package com.example.euniversity.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.euniversity.R

class UserRetrievePasswordFragment : Fragment() {
    private lateinit var userAccountActivity:UserAccountActivity
    private val TAG="UserRetrievePasswordFragment"
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
        val view=inflater.inflate(R.layout.user_retrieve_password_fragment, container, false)

        val getVerificationCodeTextView: TextView =view.findViewById(R.id.getVerificationCodeTextView)
        val finishButton: Button =view.findViewById(R.id.finishButton)
        val onClick=View.OnClickListener {
            when(it.id){
                //点击获取验证码，根据用户输入的手机号，使用商业的验证码发送消息给用户手机，此部分功能目前未实现
                R.id.getVerificationCodeTextView->{
                    Log.e(TAG,"发送验证码")
                }
                //点击完成，根据用户输入的手机号，密码，验证码进行验证，当成功时完成更新密码，失败时给出提示，此部分功能目前未实现
                R.id.finishButton->{
                    userAccountActivity.finish()
                }
            }
        }
        getVerificationCodeTextView.setOnClickListener(onClick)
        finishButton.setOnClickListener(onClick)

        return view
    }
}