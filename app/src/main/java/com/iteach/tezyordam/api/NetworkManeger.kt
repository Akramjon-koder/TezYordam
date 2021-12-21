package com.iteach.taxi.api

import android.content.Context
import com.iteach.tezyordam.BuildConfig
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManeger {

    private val client = OkHttpClient.Builder().also { client ->
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)}
    }.build()

    var retrofit :Retrofit? = null
    var api :Api?= null

    fun getApiService(context:Context) :Api{
        if(api == null){
//            val client = OkHttpClient.Builder()
//                .addInterceptor(ChuckerInterceptor(context))
//                .build()
            retrofit = Retrofit.Builder().
            addConverterFactory(GsonConverterFactory.create()).
            addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
            client(client).
            baseUrl(Api_urls.BASE_URL).
            build()

            api = retrofit!!.create(Api::class.java)
        }
        return api!!
    }
}