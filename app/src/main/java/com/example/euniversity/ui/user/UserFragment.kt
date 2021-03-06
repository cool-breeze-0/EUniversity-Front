package com.example.euniversity.ui.user

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.euniversity.MainActivity
import com.example.euniversity.R
import com.example.euniversity.adapter.UserItemAdapter
import com.example.euniversity.entity.UserItem
import com.example.euniversity.ui.home.HomeFragment
import com.example.euniversity.utils.ActivityUtil
import kotlinx.android.synthetic.main.user_account_activity.*
import kotlinx.android.synthetic.main.user_fragment.*
import kotlin.concurrent.thread

class UserFragment : Fragment() {
    private val TAG="UserFragment"
    private lateinit var mainActivity:MainActivity
    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG,"onCreateView")
        if(activity!=null){
            mainActivity=activity as MainActivity
        }
        val view=inflater.inflate(R.layout.user_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)


        val userProfileNaiveImage:ImageView=view.findViewById(R.id.userProfileNaiveImage)
        val userLoginButton:Button=view.findViewById(R.id.userLoginButton)
        val userLoginRegister:Button=view.findViewById(R.id.userLoginRegister)

        //初始化登录或未登录状态下的图标、按钮文字等
        val prefs=mainActivity.getSharedPreferences("user",Context.MODE_PRIVATE)
        val phone=prefs.getString("phone","")
        if(!phone.equals("")){
            userProfileNaiveImage.setImageResource(R.drawable.user_profile_login)
            userLoginButton.text="退出登录"
            userLoginRegister.text=phone
        }

        //为userFragment中的各个组件设置点击事件
        val onClick=View.OnClickListener{
            when(it.id){
                //当用户未登录时点击图标或者下面的“登录/注册”文本时会进入登录页面，登录后点击无效
                R.id.userLoginRegister,R.id.userProfileNaiveImage->{
                    if(phone.equals("") ){
                        val intent = Intent(mainActivity, UserAccountActivity::class.java)
                        intent.putExtra("userAccountFragment", "login")
                        startActivity(intent)
                    }
                }
                //用户未登录时点击登录按钮进入登录页面，登录后按钮文本为“退出登录”
                // 点击退出登录并重新加载本页面
                R.id.userLoginButton->{
                    if(phone.equals("")){
                        val intent = Intent(mainActivity, UserAccountActivity::class.java)
                        intent.putExtra("userAccountFragment", "login")
                        startActivity(intent)
                    }else{
                        //清除文件的用户数据即表示无用户登录
                        val editor=prefs.edit()
                        editor.clear()
                        editor.apply()
                        ActivityUtil.replaceFragment(mainActivity,R.id.mainFragment, UserFragment(),false)
                    }
                }
            }
        }
        userProfileNaiveImage.setOnClickListener(onClick)
        userLoginButton.setOnClickListener(onClick)
        userLoginRegister.setOnClickListener(onClick)

        //初始化用户页面的UserItem实例
        initUserItem()

        //当viewModel的变量内容变化时触发观察者事件，将用户页面的选项实例UserItem适配到ListView中
        viewModel.userItems1.observe(viewLifecycleOwner, Observer {
            val userItem1:ListView=view.findViewById(R.id.userItem1)
            val adapter=UserItemAdapter(mainActivity,R.layout.user_item,it)
            userItem1.adapter=adapter
            userItem1.setOnItemClickListener { parent, view, position, id ->
                when(position){
                    //当用户已登录时，点击个人信息跳转到个人信息Activity中，未登录给出提示
                    0->{
                        if(!phone.equals("")) {
                            val intent=Intent(mainActivity,UserProfileInformationActivity::class.java)
                            startActivity(intent)
                            //设置activity进入方式，从右侧进入
                            //mainActivity.overridePendingTransition(R.anim.activity_in,R.anim.activity_out)
                        }else{
                            Toast.makeText(mainActivity,"请登录后再进行操作！",Toast.LENGTH_SHORT).show()
                        }
                    }
                    //当用户已登录时，点击修改密码跳转到用户账户Activity并显示修改密码的碎片，未登录给出提示
                    1->{
                        if(!phone.equals("")) {
                            val intent = Intent(mainActivity, UserAccountActivity::class.java)
                            intent.putExtra("userAccountFragment", "changePassword")
                            startActivity(intent)
                        }else{
                            Toast.makeText(mainActivity,"请登录后再进行操作！",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged()
        })
        viewModel.userItems2.observe(viewLifecycleOwner, Observer {
            val userItem2:ListView=view.findViewById(R.id.userItem2)
            val adapter=UserItemAdapter(mainActivity,R.layout.user_item,it)
            userItem2.adapter=adapter
            userItem2.setOnItemClickListener { parent, view, position, id ->
                when(position){
                    //点击版本更新时，根据是否是最新版本给出提示是否需要下载最新版本，此部分功能目前未实现
                    0->{
                        Toast.makeText(context,"已是最新版本",Toast.LENGTH_SHORT).show()
                    }
                    1->{
                        val intent=Intent(mainActivity,UserApplicationContentActivity::class.java)
                        intent.putExtra("userApplicationContentFragment","aboutUs")
                        startActivity(intent)
                    }
                    2->{
                        val intent=Intent(mainActivity,UserApplicationContentActivity::class.java)
                        intent.putExtra("userApplicationContentFragment","help")
                        startActivity(intent)
                    }
                }
            }
            adapter.notifyDataSetChanged()
        })
        return view
    }

    /**
     *将用户页面的选项实例UserItem列表在viewModel中配置
    */
    private fun initUserItem(){
        val userItem1=ArrayList<UserItem>()
        userItem1.add(UserItem(R.drawable.user_information,"个人信息"))
        userItem1.add(UserItem(R.drawable.user_password_change,"修改密码"))

        val userItem2=ArrayList<UserItem>()
        userItem2.add(UserItem(R.drawable.user_version_refresh,"版本更新"))
        userItem2.add(UserItem(R.drawable.user_about_us,"关于我们"))
        userItem2.add(UserItem(R.drawable.user_help,"帮助"))
        thread {
            viewModel.userItems1.postValue(userItem1)
            viewModel.userItems2.postValue(userItem2)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG,"onActivityCreated")
    }

}