package com.example.testtask2.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModule : ViewModel() {

    private var bin: MutableLiveData<BinDataaaaaa> = MutableLiveData(BinDataaaaaa())

    fun getBin() = bin

    fun sendRequest(id: String): MutableLiveData<BinDataaaaaa> {
        GlobalScope.launch {
            val request = BinListApi.create().getBinListData(id).execute()
            if (request.isSuccessful) {
                bin.postValue(request.body() ?: BinDataaaaaa())
            }
        }
        return bin
    }

    companion object{
        const val TAG = "logs"
    }
}
