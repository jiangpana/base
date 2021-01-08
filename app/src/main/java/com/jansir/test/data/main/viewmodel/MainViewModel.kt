package com.jansir.test.data.main.viewmodel

import androidx.lifecycle.*
import com.jansir.core.base.viewmodel.BaseViewModel
import com.jansir.core.http.get
import com.jansir.core.http.http
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel : BaseViewModel() {


    val message: MutableLiveData<String> = MutableLiveData()

    val testLivedata = message.switchMap {te->
        liveData {

            val result =withContext (Dispatchers.IO){
                "https://wanandroid.com/wxarticle/chapters/json".http().get<String>().await()
            }
             if(result != null){
                 emit(result)
             }else{
                 emit("result ==null")
             }

        }
    }

    lateinit var test: LiveData<String>
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