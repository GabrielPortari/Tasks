package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.APIListener
import com.google.gson.Gson
import retrofit2.Response

open class BaseRepository {

    fun <T> handleResponse(response: Response<T>, listener: APIListener<T>){
        if(response.code() == TaskConstants.HTTP.SUCCESS){
            // Retorno do body
            response.body()?.let { listener.onSuccess(it) }
        }else{
            // Tratamento do JSON para string
            val error = failResponse(response.errorBody()!!.string())
            // Retorno da string de erro
            listener.onFailure(error)
        }
    }

    private fun failResponse(str: String): String{
        return Gson().fromJson(str, String::class.java)
    }

}