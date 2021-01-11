package com.jansir.core.base.viewmodel

import androidx.lifecycle.LiveDataScope
import com.google.gson.JsonObject
import com.jansir.core.ext.toast
import com.jansir.core.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 包名:com.jansir.core.base.viewmodel
 */
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
        var url: String = "https://wanandroid.com/wxarticle/chapters/json"
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


    //     "https://wanandroid.com/wxarticle/chapters/json"
    //    suspend CoroutineScope.()->Unit
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


    suspend inline fun <reified T> handleResponse(
        result: BaseResponse<T>?,
        p: RequestParam, scope: LiveDataScope<T>
    ) {
        if (result == null) {
            viewModel.stateLiveData.postValue(StateActionEvent.NetErrorState)
        } else {
            when (result.statusCode) {
                in 400..500 -> {
                    viewModel.stateLiveData.postValue(StateActionEvent.DataErrorState)
                    if (p.isShowToast) {
                        toast(result.message)
                    }
                }
                in 200..300 -> {
                    viewModel.stateLiveData.postValue(StateActionEvent.SuccessState)
                    scope.emit(result.data)
                }
            }
        }
    }

}