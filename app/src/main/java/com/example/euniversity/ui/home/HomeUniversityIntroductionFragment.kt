package com.example.euniversity.ui.home

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.euniversity.MainActivity
import com.example.euniversity.R
import com.example.euniversity.network.EUniversityNetwork
import com.example.euniversity.network.ServiceCreator
import com.example.euniversity.utils.ActivityUtil
import com.example.euniversity.utils.ResultEnum
import kotlinx.android.synthetic.main.home_university_details_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomeUniversityIntroductionFragment : Fragment() {
    private val job= Job()
    private val scope= CoroutineScope(job)
    private lateinit var homeUniversityDetailsActivity:HomeUniversityDetailsActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(activity!=null){
            homeUniversityDetailsActivity=activity as HomeUniversityDetailsActivity
        }
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.home_university_introduction_fragment, container, false)
        scope.launch(Dispatchers.Main){
            try {
                val result= EUniversityNetwork.findUniversityIntroductionById(homeUniversityDetailsActivity.getUniversityId());
                when(result.code){
                    ResultEnum.UNIVERSITY_NOT_EXIST.code->{
                        Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                    }
                    ResultEnum.FIND_SUCCESS.code->{
                        val introduction=view.findViewById<TextView>(R.id.introduction)
                        introduction.text=Html.fromHtml(result.data[0])
                    }
                }
            } catch (e: SocketTimeoutException) {
                Toast.makeText(homeUniversityDetailsActivity, "似乎没有网络，无法连接服务器！", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view;
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}