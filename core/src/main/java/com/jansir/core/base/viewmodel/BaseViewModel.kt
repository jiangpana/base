package com.jansir.core.base.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jansir.core.ext.findClazzFromSuperclassGeneric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/2.
 */

abstract class BaseViewModel<T : BaseRepository> :
    ViewModel(), LifecycleObserver {

    val repository: T by lazy {
        getRep()
    }

    private fun getRep(): T {
        return findClazzFromSuperclassGeneric(BaseRepository::class.java).getDeclaredConstructor()
            .newInstance() as T
    }

    //通用事件模型驱动(如：显示对话框、取消对话框、错误提示)
    val stateLiveData = MediatorLiveData<StateActionEvent>()

    init {
        repository.apply {
            requestSuccess = {
                stateLiveData.value = StateActionEvent.SuccessState
            }
            requestDataError = {
                stateLiveData.value = StateActionEvent.DataErrorState
            }
            requestLoading = {
                stateLiveData.value = StateActionEvent.LoadingState
            }
            requestNetWorkError = {
                stateLiveData.value = StateActionEvent.NetErrorState
            }
        }

    }

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