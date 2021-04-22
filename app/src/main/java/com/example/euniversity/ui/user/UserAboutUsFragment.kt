package com.example.euniversity.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.euniversity.R

class UserAboutUsFragment : Fragment() {

    private lateinit var userApplicationContentActivity: UserApplicationContentActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(activity!=null){
            userApplicationContentActivity=activity as UserApplicationContentActivity
        }
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.user_about_us_fragment, container, false)

        val toolbarTitle:TextView=userApplicationContentActivity.findViewById(R.id.toolbarTitle)
        toolbarTitle.setText(R.string.user_about_us_toolbar_title)

        return view
    }
}