package com.jansir.core.base.viewmodel

import androidx.lifecycle.LiveDataScope
import com.jansir.core.http.get
import com.jansir.core.http.http
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 包名:com.jansir.core.base.viewmodel
 */
abstract class BaseRepository {

    suspend inline fun <reified T> LiveDataScope<T>.request() {
        val result = withContext(Dispatchers.IO) {
            "https://wanandroid.com/wxarticle/chapters/json".http().get<BaseResponse<T>>().await()
        }
        if (result != null) {
            emit(result.data)
        }
    }
}