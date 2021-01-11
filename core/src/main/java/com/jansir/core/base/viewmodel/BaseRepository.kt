package com.jansir.core.base.viewmodel

import androidx.lifecycle.LiveDataScope
import com.google.gson.JsonObject
import com.jansir.core.ext.toast
import com.jansir.core.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class BaseRepository {

    lateinit var viewModel: BaseViewModel<*>

    fun init(viewModel: BaseViewModel<*>) {
        this.viewModel = viewModel
    }


    fun <T : BaseViewModel<*>> getVm(): T = viewModel as T


    sealed class RequestMethod {
        object POST : RequestMethod()
        object GET : RequestMethod()
        object DELETE : RequestMethod()
        object PUT : RequestMethod()

        data class POSTJSON(val json: String) : RequestMethod()
    }

    inner class RequestParam {
        var url: String = ""
        var isShowLoading: Boolean = false
        var isShowToast: Boolean = false
        var params: Array<out Pair<String, Any>> = arrayOf()
        var method: RequestMethod = RequestMethod.GET


        fun get(): RequestParam {
            method = RequestMethod.GET
            return this
        }

        fun post(): RequestParam {
            method = RequestMethod.POST
            return this
        }

        fun delete(): RequestParam {
            method = RequestMethod.DELETE
            return this
        }

        fun postJson(vararg params: Pair<String, String>): RequestParam {
            val json = JsonObject().apply {
                params.forEach {
                    addProperty(it.first, it.second)
                }
            }
            method = RequestMethod.POSTJSON(json.toString())
            return this
        }

        fun put(): RequestParam {
            method = RequestMethod.PUT
            return this
        }

        fun url(url: String): RequestParam {
            this.url = url
            return this
        }

        fun params(vararg params: Pair<String, Any>): RequestParam {
            this.params = params
            return this
        }

        fun showLoading(loading: Boolean): RequestParam {
            isShowLoading = loading
            return this
        }

        fun showToast(toast: Boolean): RequestParam {
            isShowToast = toast
            return this
        }

    }


    /**
     * http 请求并发送数据,使用如下
        get()
           .showLoading(true)
           .showToast(true)
           .url("https://wanandroid.com/wxarticle/chapters/json")
     *
     * @param T  http data 泛型
     * @param block 此作用域用于设置请求的信息
     * @receiver
     */
    protected suspend inline fun <reified T> LiveDataScope<T>.httpEmitData(block: RequestParam.() -> Unit) {
        val params = RequestParam()
        block(params)
        if(params.isShowLoading){
            viewModel.stateLiveData.postValue(StateActionEvent.LoadingState)
        }
        val result = when (params.method) {
            RequestMethod.GET -> withContext(Dispatchers.IO) {
                params.url.http().params(*params.params).get<BaseResponse<T>>()
                    .await()
            }
            RequestMethod.POST -> withContext(Dispatchers.IO) {
                params.url.http().params(*params.params)
                    .post<BaseResponse<T>>().await()
            }
            RequestMethod.PUT -> withContext(Dispatchers.IO) {
                params.url.http().params(*params.params).put<BaseResponse<T>>()
                    .await()
            }
            RequestMethod.DELETE -> withContext(Dispatchers.IO) {
                params.url.http().params(*params.params)
                    .delete<BaseResponse<T>>().await()
            }
            is RequestMethod.POSTJSON -> withContext(Dispatchers.IO) {
                params.url.http().params(*params.params)
                    .postJson<BaseResponse<T>>((params.method as RequestMethod.POSTJSON).json)
                    .await()
            }
        }
        handleResponse(result, params, this)
    }

    /**
     * Handle response
     *
     * @param T
     * @param value
     * @param requestParam
     * @param scope
     */
    suspend inline fun <reified T> handleResponse(
        value: BaseResponse<T>?,
        requestParam: RequestParam, scope: LiveDataScope<T>
    ) {
        if (value == null) {
            viewModel.stateLiveData.postValue(StateActionEvent.NetErrorState)
        } else {
            when (value.statusCode) {
                in 400..500 -> {
                    viewModel.stateLiveData.postValue(StateActionEvent.DataErrorState)
                    if (requestParam.isShowToast) {
                        toast(value.message)
                    }
                }
                in 200..300 -> {
                    viewModel.stateLiveData.postValue(StateActionEvent.SuccessState)
                    scope.emit(value.data)
                }
            }
        }
    }

}