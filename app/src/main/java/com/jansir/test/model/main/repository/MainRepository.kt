package com.jansir.test.model.main.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.jansir.core.base.viewmodel.BaseRepository
import com.jansir.core.http.get
import com.jansir.core.http.http
import com.jansir.test.model.main.entity.Chapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 包名:com.jansir.test.data.main.repository.main
 */
class MainRepository: BaseRepository() {

    fun testReq(message: MutableLiveData<String>): LiveData<List<Chapter>> {
       return message.switchMap {te->
            liveData<List<Chapter>> {
              request()
            }
        }
    }

}