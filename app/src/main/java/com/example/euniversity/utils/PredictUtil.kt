package com.example.euniversity.utils
import android.util.Log
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Mean
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import java.lang.Exception

object PredictUtil {
    fun getPredictResult(grades:List<String>,grade: Int):Double{
        val data=doubleArrayOf(grades[0].toDouble(),grades[1].toDouble(),grades[2].toDouble())
        System.out.println(data[0].toString()+" "+data[1].toString()+" "+data[2].toString())
        val mean=Mean().evaluate(data)
        val sd=StandardDeviation().evaluate(data,mean)
        val distribution=NormalDistribution(mean, sd)
        try{
            val result=distribution.cumulativeProbability(grade.toDouble())
            return result;
        }catch (e:Exception){
            Log.e("distribution","result error")
        }
        return 0.0
    }
}