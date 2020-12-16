package com.jansir.core.base.viewmodel

import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.jansir.core.ContextHolder
import com.jansir.core.ext.loge
import com.jansir.core.ext.toast
import com.jansir.core.http.*

import kotlinx.coroutines.*
import kotlin.coroutines.resume

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/2.
 */
abstract class BaseViewModel : ViewModel(), LifecycleObserver {


    val mStateLiveData = MutableLiveData<StateActionEvent>()//通用事件模型驱动(如：显示对话框、取消对话框、错误提示)


    fun <T> getLiveDataAndEmit(block: suspend LiveDataScope<T>.() -> T): LiveData<T> = liveData {
        try {
            mStateLiveData.value = LoadState
            emit(block())
            mStateLiveData.value = SuccessState
        } catch (e: Exception) {
            mStateLiveData.value = NetErrorState(e.message)
        }
    }


    fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                block()
                mStateLiveData.value = SuccessState
            } catch (e: Exception) {
                e.printStackTrace()
                mStateLiveData.value = DataErrorState
            }
        }

    }

    fun launchWithLoading(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                mStateLiveData.value = LoadState
                block()
                mStateLiveData.value = SuccessState
            } catch (e: Exception) {
                e.printStackTrace()
                mStateLiveData.value =DataErrorState
            }
        }
    }

    suspend inline fun <reified T> get(url: String, vararg params: Pair<String, Any>): T {
        return suspendCancellableCoroutine { continuation ->
            launch {
                val result = withContext(Dispatchers.IO) {
                    url.http().params(*params).get<BaseResponse<T>>().await()
                }
                handleResult(result, continuation)
            }
        }
    }


    suspend inline fun <reified T> put(url: String, vararg params: Pair<String, Any>): T {
        return suspendCancellableCoroutine { continuation ->
            launch {
                val result = withContext(Dispatchers.IO) {
                    url.http().params(*params).put<BaseResponse<T>>().await()
                }
                handleResult(result, continuation)
            }
        }
    }

    suspend inline fun <reified T> delete(url: String, vararg params: Pair<String, Any>): T {
        return suspendCancellableCoroutine { continuation ->
            launch {
                val result = withContext(Dispatchers.IO) {
                    url.http().params(*params).delete<BaseResponse<T>>().await()
                }
                handleResult(result, continuation)
            }
        }
    }




    suspend inline fun <reified T> post(url: String, vararg params: Pair<String, String>): T {
        return suspendCancellableCoroutine { continuation ->
            launch {
                val result = withContext(Dispatchers.IO) {
                    url.http().params(*params).post<BaseResponse<T>>().await()
                }
                handleResult(result, continuation)
            }
        }
    }

    suspend inline fun <reified T> postJson(url: String, vararg params: Pair<String, String>): T {
        return suspendCancellableCoroutine { continuation ->
            launch {
                val json = JsonObject().apply {
                    params.forEach {
                        addProperty(it.first, it.second)
                    }
                }
                val result = withContext(Dispatchers.IO) {
                    url.http().params(*params).postJson<BaseResponse<T>>(json.toString()).await()
                }
                handleResult(result, continuation)
            }
        }
    }

    suspend inline fun <reified T> getList(
        url: String,
        vararg params: Pair<String, String>
    ): MutableList<T> {
        return suspendCancellableCoroutine { continuation ->
            launch {
               val result= withContext(Dispatchers.IO) {
                    url.http().params(*params).get<BaseResponseList<T>>().await()
                }
                if (result == null) {
                    loge("数据为空")
                    mStateLiveData.postValue(NetErrorState())
                } else {
                    result.data.let {
                        continuation.resume(it)
                    }
                    when (result.statusCode) {
                        in 400..500 -> {
                            ContextHolder.sContext.toast(result.message)
                        }
                    }
                }
            }
        }
    }

    suspend inline fun <reified T> postJsonList(
        url: String,
        vararg params: Pair<String, String>
    ): MutableList<T> {
        return suspendCancellableCoroutine { continuation ->
            launch {
                val json = JsonObject().apply {
                    params.forEach {
                        addProperty(it.first, it.second)
                    }
                }
               val result=withContext(Dispatchers.IO) {
                    url.http().params(*params).postJson<BaseResponseList<T>>(json.toString())
                        .await()
                }
                if (result == null) {
                    loge("数据为空")
                    mStateLiveData.postValue(NetErrorState())
                } else {
                    result.data.let {
                        continuation.resume(it)
                    }
                    when (result.statusCode) {
                        in 400..500 -> {
                            ContextHolder.sContext.toast(result.message)
                        }
                    }
                }
            }

        }
    }

    inline fun <reified T> handleResult(
        result: BaseResponse<T>?,
        continuation: CancellableContinuation<T>
    ) {
        if (result == null) {
            loge("数据为空")
            mStateLiveData.postValue(NetErrorState())
        } else {
            when (result.statusCode) {
                in 400..500 -> {
                    mStateLiveData.postValue(DataErrorState)
                    ContextHolder.sContext.toast(result.message)
                }
                in 200..300 -> {
                    result.data.let {
                        continuation.resume(it)
                    }
                }
            }
        }
    }

    //取消所有協程
    fun cancel(){
        viewModelScope.cancel()
    }
}