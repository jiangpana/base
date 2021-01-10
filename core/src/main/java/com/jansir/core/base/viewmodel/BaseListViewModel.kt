package com.jansir.core.base.viewmodel

import androidx.lifecycle.MutableLiveData

/**
 * author: jansir
 * e-mail: xxx
 * date: 2020/5/6.
 *
 * 用于分页的base viewModel
 */
abstract class BaseListViewModel<T :BaseRepository> : BaseViewModel<T>() {

    class PageInfo {

        //第几页
        @Volatile
        var page = 1
        @Synchronized
        fun nextPage() {
            page++
        }
        fun reset() {
            page = 1
        }

        val isFirstPage: Boolean
            get() = page == 1
    }

    //一页多少数据，不够一页则是没有数据了
    val PAGE_SIZE = 10

    val pageInfo = PageInfo()

    // 加载更多状态  true 已经是最后一页了
    val loadMoreState = MutableLiveData<LoadMoreState>()


    abstract fun loadMore()
    abstract fun getFirstData()

    sealed class LoadMoreState {
        object HAS_MORE : LoadMoreState()
        object NO_MORE : LoadMoreState()
    }
}