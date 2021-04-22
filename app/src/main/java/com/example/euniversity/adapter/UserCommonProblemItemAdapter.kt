package com.example.euniversity.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.euniversity.R

class UserCommonProblemItemAdapter(activity:Activity,val resourceId:Int,data:List<String>) :ArrayAdapter<String>(activity,resourceId,data){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view= LayoutInflater.from(context).inflate(resourceId,parent,false)
        val userCommonProblemItemTextView:TextView=view.findViewById(R.id.userCommonProblemItemTextView)
        val userCommonProblemItem=getItem(position)
        if(userCommonProblemItem!=null){
            userCommonProblemItemTextView.setText(userCommonProblemItem)
        }
        return view
    }
}