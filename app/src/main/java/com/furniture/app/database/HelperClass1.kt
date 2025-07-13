package com.furniture.app.database

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class HelperClass1 {
    var imgUrl by mutableStateOf("")
    var json by mutableStateOf("")

    fun updateUrl(url: String){
        Log.d("ll","$imgUrl")
        imgUrl=url
    }
    fun updateJson(Json: String){
        Log.d("ll","$json")
        json=Json
    }
    fun retrieveUrl(): String{
        Log.d("llzz","llzz")
        Log.d("llzz",json)
        return imgUrl
    }
    fun retrieveJson(): String{
        Log.d("llzz",imgUrl)
        return json
    }

    fun HelperClass1(){

    }

    fun  HelperClass1(url: String,Json: String){
        this.imgUrl=url
        this.json=Json
    }
}