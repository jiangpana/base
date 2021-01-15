package com.jansir.test.data.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.jansir.core.base.viewmodel.BaseViewModel
import com.jansir.test.data.main.dto.ChapterDTO
import com.jansir.test.data.main.repository.MainRepository
import com.orhanobut.logger.Logger


class MainViewModel : BaseViewModel<MainRepository>() {

    val message: MutableLiveData<String> = MutableLiveData()

    val testLivedata: LiveData<List<ChapterDTO>> by lazy {
        repository.testReq(message)
    }

    fun test() {
        message.value = "abcd"
    }

    val userId: MutableLiveData<String> = MutableLiveData()

    val user = userId.switchMap { id ->
        liveData {
            Logger.e("emit")
            emit(id)
        }
    }
}