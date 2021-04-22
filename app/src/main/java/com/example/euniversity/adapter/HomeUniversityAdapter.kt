package com.example.euniversity.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euniversity.R
import com.example.euniversity.entity.HomeUniversityItem
import com.example.euniversity.ui.home.HomeUniversityDetailsActivity

class HomeUniversityAdapter(val homeUniversityList: List<HomeUniversityItem>):RecyclerView.Adapter<HomeUniversityAdapter.ViewHolder>() {
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val universityImage=view.findViewById<ImageView>(R.id.universityImage)
        val universityName=view.findViewById<TextView>(R.id.universityName)
        val universityAttribute=view.findViewById<TextView>(R.id.universityAttribute)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.home_university_item,parent,false)
        val viewHolder=ViewHolder(view)

        val homeUniversityLayout=view.findViewById<ViewGroup>(R.id.homeUniversityLayout)

        val onClick=View.OnClickListener {
            val position=viewHolder.adapterPosition
            //根据选中的的位置的院校的ID从数据库中查询出院校详细信息，院校的ID可以在HomeFragment中通过本Adapter中增加参数传进来
            // 由于获取ID方法和数据库查询方法未实现，此处用position位置的homeUniversityItem暂时代替，仅供测试
            val homeUniversityItem=homeUniversityList[position]
            val universityIntroduction="清华大学，简称清华，旧称清华学堂、游美肄业馆、清华学校、国立清华大学，" +
                    "是一所位于北京市海淀区清华园的公立大学。始建于1911年，因北京西北郊清华园而得名。初为清政府利用" +
                    "美国退还的部分庚子赔款所建留美预备学校“游美学务处”及附设“肄业馆”，于1925年始设大学部"
            val intent=Intent(parent.context,HomeUniversityDetailsActivity::class.java)
            intent.putExtra("homeUniversityItem",homeUniversityItem)
            intent.putExtra("universityIntroduction",universityIntroduction)
            parent.context.startActivity(intent)
        }

        homeUniversityLayout.setOnClickListener(onClick)

        return viewHolder
    }

    override fun getItemCount(): Int =homeUniversityList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val homeUniversityItem=homeUniversityList[position]
        holder.universityImage.setImageResource(homeUniversityItem.universityImage)
        holder.universityName.text=homeUniversityItem.universityName
        holder.universityAttribute.text=homeUniversityItem.universityAttribute
    }

}