package com.appops.newsapp.service

import com.appops.newsapp.util.constants.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServiceManager {
    companion object {
        private val service by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
        }
        val api by lazy {
            service.create(NewsAPI::class.java)
        }

    }
}