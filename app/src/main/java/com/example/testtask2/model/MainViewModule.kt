package com.example.testtask2.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModule : ViewModel() {

    private var binList: MutableLiveData<BinDataaaaaa> = MutableLiveData(BinDataaaaaa())

    fun getBinList() = binList

    fun sendRequest(id: String): MutableLiveData<BinDataaaaaa> {
        GlobalScope.launch {
            val request = BinListApi.create().getBinListData(id).execute()
            if (request.isSuccessful) {
                binList.postValue(request.body() ?: BinDataaaaaa())
            } else {
                binList.postValue(BinDataaaaaa())
            }
        }
        return binList
    }
}
