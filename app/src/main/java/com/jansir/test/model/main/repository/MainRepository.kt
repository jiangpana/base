package com.jansir.test.model.main.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.jansir.core.base.viewmodel.BaseRepository
import com.jansir.test.model.main.dto.ChapterDTO

/**
 * 包名:com.jansir.test.data.main.repository.main
 */
class MainRepository : BaseRepository() {



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