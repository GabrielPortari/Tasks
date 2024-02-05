package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseRepository(val context: Context){

    fun isConnectinAvailable(): Boolean{
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeInternet = cm.activeNetwork ?: return false
            val networkCapabilities = cm.getNetworkCapabilities(activeInternet) ?: return false

            result = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }else{
            if(cm.activeNetworkInfo != null){
                result = when(cm.activeNetworkInfo!!.type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return result
    }


    fun <T> executeCall(call: Call<T>, listener: APIListener<T>){
        call.enqueue(object : Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                handleResponse(response, listener)
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    fun <T> handleResponse(response: Response<T>, listener: APIListener<T>){
        if(response.code() == TaskConstants.HTTP.SUCCESS){
            response.body()?.let { listener.onSuccess(it) }
        }else{
            val error = failResponse(response.errorBody()!!.string())
            listener.onFailure(error)
        }
    }

    private fun failResponse(str: String): String{
        return Gson().fromJson(str, String::class.java)
    }

}