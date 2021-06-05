package com.example.euniversity.utils
import android.util.Log
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Mean
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import java.lang.Exception

object PredictUtil {
    fun getPredictResult(grades:List<String>,grade: Int):Double{
        //数据转化为double类型
        val data=doubleArrayOf(grades[0].toDouble(),grades[1].toDouble(),grades[2].toDouble())
        val mean=Mean().evaluate(data)//样本的均值
        val sd=StandardDeviation().evaluate(data,mean)//样本的方差
        //根据样本均值和方差构建正态分布
        val distribution=NormalDistribution(mean, sd)
        //计算概率
        try{
            val result=distribution.cumulativeProbability(grade.toDouble())
            return result;
        }catch (e:Exception){
            Log.e("distribution","result error")
        }
        return 0.0
    }
}