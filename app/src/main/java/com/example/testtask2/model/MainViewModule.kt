package com.example.testtask2.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModule : ViewModel() {

    private var bin: MutableLiveData<BinData> = MutableLiveData(BinData())
    private var isFailure : MutableLiveData<Int> = MutableLiveData()

    fun getBin() = bin
    fun getIsFailure() = isFailure

    fun sendRequest(id: String): MutableLiveData<BinData> {
        viewModelScope.launch(Dispatchers.IO) {
             BinListApi.create().getBinListData(id).enqueue(object : Callback<BinData>{

                override fun onResponse(call: Call<BinData>, response: Response<BinData>) {
                    bin.value = response.body() ?: BinData()
                }

                override fun onFailure(call: Call<BinData>, t: Throwable) {
                    isFailure.value = 1
                }
            })
        }
        return bin
    }

    companion object{
        const val TAG = "logs"
    }
}

