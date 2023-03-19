package com.example.testtask2.model

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface BinListApi {

    @GET("{id}")
   fun getBinListData(@Path("id") id: String ): Call<BinData>

    companion object{

        private const val baseURL = "https://lookup.binlist.net/"

        fun create(): BinListApi{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build()
            return retrofit.create(BinListApi::class.java)
        }
    }
}