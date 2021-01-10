package com.jansir.test.model.main.viewmodel

import androidx.lifecycle.*
import com.jansir.core.base.viewmodel.BaseViewModel
import com.jansir.core.http.get
import com.jansir.core.http.http
import com.jansir.test.model.main.entity.Chapter
import com.jansir.test.model.main.repository.MainRepository
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainViewModel : BaseViewModel<MainRepository>() {




    val message: MutableLiveData<String> = MutableLiveData()

    val testLivedata :LiveData<List<Chapter>> by lazy {
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