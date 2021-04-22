package com.example.euniversity.ui.predict

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.euniversity.R
import kotlinx.android.synthetic.main.predict_result_activity.*

class PredictResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.predict_result_activity)

        val result=intent.getIntExtra("result",0)
        predictResultData.setText("${result}%")
        //根据概率给出不同的警示信息
        if(result>90){
            predictResultWarning.setText("录取率很高，不要松懈哦")
        }else if(result>75){
            predictResultWarning.setText("录取率较高，继续努力哦")
        }else if(result>60){
            predictResultWarning.setText("录取率一般，加油哦")
        }else{
            predictResultWarning.setText("录取率较低，努力奋斗哦")
        }
    }
    fun onClick(v: View) {
        when (v.id) {
            //返回按钮功能与返回键一致
            R.id.predictResultBackButton -> {
                finish()
            }
        }
    }
}