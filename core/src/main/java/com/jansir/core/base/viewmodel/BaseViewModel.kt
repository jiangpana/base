package com.jansir.core.base.viewmodel

import androidx.lifecycle.*
import com.jansir.core.ext.findClazzFromSuperclassGeneric

import kotlinx.coroutines.*

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/2.
 */
abstract class BaseViewModel <T :BaseRepository>: ViewModel(), LifecycleObserver {


    protected val repository: T by lazy {
        getRep().apply {
            init(this@BaseViewModel)
        }
    }

    private fun getRep(): T {
        return findClazzFromSuperclassGeneric(BaseRepository::class.java).getDeclaredConstructor().newInstance() as T
    }


    //通用事件模型驱动(如：显示对话框、取消对话框、错误提示)
    val stateLiveData = MutableLiveData<StateActionEvent>()

    fun launch(showLoading: Boolean = false, block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                if (showLoading) {
                    stateLiveData.value = StateActionEvent.LoadingState
                }
                block()
                stateLiveData.value = StateActionEvent.SuccessState
            } catch (e: Exception) {
                e.printStackTrace()
                stateLiveData.value = StateActionEvent.DataErrorState
            }
        }

    }

}