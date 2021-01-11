package com.jansir.core.base.viewmodel

import com.google.gson.annotations.SerializedName

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/11/23.
 */

  data class BaseResponse<T>(
        @SerializedName("data")
        val `data`: T,
        @SerializedName("code")
        val code: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("section")
        val section: String,
        @SerializedName("statusCode")
        val statusCode: Int,
        @SerializedName("type")
        val type: String
)


