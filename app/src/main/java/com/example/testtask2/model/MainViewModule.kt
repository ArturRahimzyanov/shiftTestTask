package com.example.testtask2.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModule : ViewModel() {

    private var bin: MutableLiveData<BinDataaaaaa> = MutableLiveData(BinDataaaaaa())

    fun getBin() = bin

    @OptIn(DelicateCoroutinesApi::class)
    fun sendRequest(id: String): MutableLiveData<BinDataaaaaa> {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        GlobalScope.launch(handler) {
            val request = BinListApi.create().getBinListData(id).execute()
            if (request.isSuccessful) {
                bin.postValue(request.body() ?: BinDataaaaaa())
            }
        }
        return bin
    }
}
