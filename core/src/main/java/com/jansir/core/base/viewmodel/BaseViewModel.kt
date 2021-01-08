package com.jansir.core.base.viewmodel

import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.jansir.core.ContextHolder
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

    //通用事件模型驱动(如：显示对话框、取消对话框、错误提示)
    val mStateLiveData = MutableLiveData<StateActionEvent>()


    inner class PARAM {
        var mUrl: String = ""
        var mShowLoading: Boolean = false
        var mShowToast: Boolean = false
        var mParams: Array<out Pair<String, Any>> = arrayOf()
    }

    inner class LiveDataBuilder {


        val p = PARAM()

        fun get(): LiveDataBuilder {
            return this
        }

        fun post(): LiveDataBuilder {
            return this
        }

        fun delete(): LiveDataBuilder {
            return this
        }

        fun postJson(): LiveDataBuilder {
            return this
        }

        fun put(): LiveDataBuilder {
            return this
        }

        fun url(url: String): LiveDataBuilder {
            p.mUrl = url
            return this
        }

        fun params(vararg params: Pair<String, Any>): LiveDataBuilder {
            p.mParams = params
            return this
        }

        fun showLoading(loading: Boolean): LiveDataBuilder {
            p.mShowLoading = loading
            return this
        }

        inline fun <reified T> build(crossinline processData: T.() -> Unit = {}): LiveData<T> {
            return newLiveData(
                p, processData = processData
            )
        }
    }

    fun test() {

      val data=  LiveDataBuilder()
            .get()
            .params()
            .url("")
            .build<BaseViewModel> { }
    }

    inline fun <reified T> newLiveData(
        p: PARAM, crossinline processData: T.() -> Unit
    ): LiveData<T> {
        return emit {
            get(p, processData = processData)
        }
    }


    fun <T> emit(block: suspend LiveDataScope<T>.() -> T): LiveData<T> = liveData {
        try {
            emit(block())
        } catch (e: Exception) {
        }
    }

    fun launch(showLoading: Boolean = false, block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                if (showLoading) {
                    mStateLiveData.value = LoadState
                }
                block()
                mStateLiveData.value = SuccessState
            } catch (e: Exception) {
                e.printStackTrace()
                mStateLiveData.value = DataErrorState
            }
        }

    }


    suspend inline fun <reified T> get(
        p: PARAM, crossinline processData: T.() -> Unit
    ): T {
        return suspendCancellableCoroutine { continuation ->
            launch(p.mShowLoading) {
                val result = withContext(Dispatchers.IO) {
                    p.mUrl.http().params(*p.mParams).get<BaseResponse<T>>().await()
                }
                handleResult(result, continuation, processData)
            }
        }
    }


    suspend inline fun <reified T> put(
        url: String, vararg params: Pair<String, Any>,
        showLoading: Boolean = false, crossinline processData: T.() -> Unit
    ): T {
        return suspendCancellableCoroutine { continuation ->
            launch(showLoading) {
                val result = withContext(Dispatchers.IO) {
                    url.http().params(*params).put<BaseResponse<T>>().await()
                }
                handleResult(result, continuation, processData)
            }
        }
    }

    suspend inline fun <reified T> delete(
        url: String, vararg params: Pair<String, Any>,
        showLoading: Boolean = false, crossinline processData: T.() -> Unit
    ): T {
        return suspendCancellableCoroutine { continuation ->
            launch(showLoading) {
                val result = withContext(Dispatchers.IO) {
                    url.http().params(*params).delete<BaseResponse<T>>().await()
                }
                handleResult(result, continuation, processData)
            }
        }
    }


    suspend inline fun <reified T> post(
        url: String, vararg params: Pair<String, String>,
        showLoading: Boolean = false, crossinline processData: T.() -> Unit
    ): T {
        return suspendCancellableCoroutine { continuation ->
            launch(showLoading) {
                val result = withContext(Dispatchers.IO) {
                    url.http().params(*params).post<BaseResponse<T>>().await()
                }
                handleResult(result, continuation, processData)
            }
        }
    }

    suspend inline fun <reified T> postJson(
        url: String, vararg params: Pair<String, String>,
        showLoading: Boolean = false, crossinline processData: T.() -> Unit
    ): T {
        return suspendCancellableCoroutine { continuation ->
            launch(showLoading) {
                val json = JsonObject().apply {
                    params.forEach {
                        addProperty(it.first, it.second)
                    }
                }
                val result = withContext(Dispatchers.IO) {
                    url.http().params(*params).postJson<BaseResponse<T>>(json.toString()).await()
                }

                handleResult(result, continuation, processData)
            }
        }
    }


    inline fun <reified T> handleResult(
        result: BaseResponse<T>?,
        continuation: CancellableContinuation<T>, crossinline processData: T.() -> Unit
    ) {
        if (result == null) {
            mStateLiveData.postValue(NetErrorState())
        } else {
            when (result.statusCode) {
                in 400..500 -> {
                    mStateLiveData.postValue(DataErrorState)
                    toast(result.message)
                }
                in 200..300 -> {
                    result.data.let {
                        processData(it)
                        continuation.resume(it)
                    }
                }
            }
        }
    }

}