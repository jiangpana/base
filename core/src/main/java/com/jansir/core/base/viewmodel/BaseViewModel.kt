package com.jansir.core.base.viewmodel

import androidx.lifecycle.*
import com.google.gson.JsonObject
import com.jansir.core.ext.toast
import com.jansir.core.http.*
import com.orhanobut.logger.Logger

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


    open class PARAM {
        var url: String = "https://wanandroid.com/wxarticle/chapters/json"
        var isShowLoading: Boolean = false
        var isShowToast: Boolean = false
        var params: Array<out Pair<String, Any>> = arrayOf()
        var method: RequestMethod = RequestMethod.GET
    }

    sealed class RequestMethod {
        object POST : RequestMethod()
        object GET : RequestMethod()
        object DELETE : RequestMethod()
        object PUT : RequestMethod()
        data class POSTJSON(val json: String) : RequestMethod()
    }

    inner class LiveDataBuilder {

        val p = PARAM()
        fun get(): LiveDataBuilder {
            p.method = RequestMethod.GET
            return this
        }

        fun post(): LiveDataBuilder {
            p.method = RequestMethod.POST
            return this
        }

        fun delete(): LiveDataBuilder {
            p.method = RequestMethod.DELETE
            return this
        }

        fun postJson(vararg params: Pair<String, String>): LiveDataBuilder {
            val json = JsonObject().apply {
                params.forEach {
                    addProperty(it.first, it.second)
                }
            }
            p.method = RequestMethod.POSTJSON(json.toString())
            return this
        }

        fun put(): LiveDataBuilder {
            p.method = RequestMethod.PUT
            return this
        }

        fun url(url: String): LiveDataBuilder {
            p.url = url
            return this
        }

        fun params(vararg params: Pair<String, Any>): LiveDataBuilder {
            p.params = params
            return this
        }

        fun showLoading(loading: Boolean): LiveDataBuilder {
            p.isShowLoading = loading
            return this
        }

        fun showToast(toast: Boolean): LiveDataBuilder {
            p.isShowToast = toast
            return this
        }

        inline fun <reified T> build() = newLiveData<T>(p)
    }
//    crossinline processData: T.() -> Unit = {}

    inline fun <reified T> newLiveData(
        p: PARAM
    ): LiveData<T> {
        return liveData {
            request<T>(p)?.let { emit(it) }
        }
    }

    suspend inline fun <reified T> request(
        p: PARAM
    ): T? {
        return suspendCancellableCoroutine { continuation ->
            launch(p.isShowLoading) {
                val response = withContext(Dispatchers.IO) {
                    when (p.method) {
                        RequestMethod.DELETE -> p.url.http().params(*p.params)
                            .delete<BaseResponse<T>>().await()
                        RequestMethod.GET -> p.url.http().params(*p.params)
                            .get<BaseResponse<T>>().await()
                        RequestMethod.PUT -> p.url.http().params(*p.params)
                            .put<BaseResponse<T>>().await()
                        RequestMethod.POST -> p.url.http().params(*p.params)
                            .post<BaseResponse<T>>().await()
                        is RequestMethod.POSTJSON -> p.url.http().params(*p.params)
                            .postJson<BaseResponse<T>>((p.method as RequestMethod.POSTJSON).json)
                            .await()

                    }

                }
                handleResponse(response, continuation, p)
            }
        }
    }

    fun launch(showLoading: Boolean = false, block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                if (showLoading) {
                    mStateLiveData.value = StateActionEvent.LoadState
                }
                block()
                mStateLiveData.value = StateActionEvent.SuccessState
            } catch (e: Exception) {
                e.printStackTrace()
                mStateLiveData.value = StateActionEvent.DataErrorState
            }
        }

    }


    inline fun <reified T> handleResponse(
        result: BaseResponse<T>?,
        continuation: CancellableContinuation<T?>
        , p: PARAM
    ) {
        if (result == null) {
            Logger.e("数据为空")
            mStateLiveData.postValue(StateActionEvent.NetErrorState)
            continuation.resume(null)
        } else {
            when (result.statusCode) {
                in 400..500 -> {
                    if (p.isShowToast) {
                        toast(result.message)
                    }
                    mStateLiveData.postValue(StateActionEvent.DataErrorState)
                }
                in 200..300 -> {
//                    processData(result.data)
                    continuation.resume(result.data)
                }
            }
        }
    }

}