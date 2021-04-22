package com.example.euniversity.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.euniversity.R
import com.example.euniversity.entity.UserItem
import kotlinx.android.synthetic.main.user_item.view.*

class UserItemAdapter(activity: Activity, val resourceId:Int,data:List<UserItem>):ArrayAdapter<UserItem>(activity,resourceId,data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view=LayoutInflater.from(context).inflate(resourceId,parent,false)
        val userItemImage:ImageView=view.findViewById(R.id.userItemImage)
        val userItemText:TextView=view.findViewById(R.id.userItemText)
        val userItem=getItem(position)
        if(userItem!=null){
            userItemImage.setImageResource(userItem.imageId)
            userItemText.text=userItem.name
        }
        return view
    }
}