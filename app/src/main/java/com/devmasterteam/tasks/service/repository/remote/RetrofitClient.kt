package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.repository.local.PersonService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor(){

    companion object{
        //Singleton do retrofit
        private lateinit var INSTANCE: Retrofit

        private fun getRetrofitInstance(): Retrofit{

            val httpClient = OkHttpClient.Builder().build()
            val baseUrl = "http://devmasterteam.com/CursoAndroidAPI/"
            if(!::INSTANCE.isInitialized) {
                synchronized(RetrofitClient::class) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(httpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }

        fun <T> getService(serviceClass: Class<T>): T{
            return getRetrofitInstance().create(serviceClass)
        }
    }
}