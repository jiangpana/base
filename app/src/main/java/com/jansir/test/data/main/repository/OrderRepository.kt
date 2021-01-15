package com.jansir.test.data.main.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jansir.core.base.viewmodel.BaseRepository


class OrderRepository : BaseRepository() {


    fun testReq(message: MutableLiveData<String>): LiveData<List<ChapterDTO>> {
        return message.switchMap { te ->
            liveData<List<ChapterDTO>> {
                httpEmitData {
                    get()
                        .showLoading(true)
                        .showToast(true)
                        .url("https://wanandroid.com/wxarticle/chapters/json")
                }
            }
        }
    }

}