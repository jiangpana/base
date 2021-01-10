package com.jansir.core.base.activity

import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jansir.core.R
import com.jansir.core.base.viewmodel.BaseListViewModel
import com.jansir.core.databinding.ActivityBaseRefreshListBinding

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/2.
 */
abstract class BaseListVMActivity<VM : BaseListViewModel<*>> : BaseVMActivity<ActivityBaseRefreshListBinding,VM>() {


    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun initView() {
        recyclerView = binding.rvBase
        swipeRefreshLayout = binding.slBase
        initRvAndAdapter()
    }

    override fun initData() {
        refresh()
    }

    private fun initRvAndAdapter() {
        provideAdapter().apply {
            setHasStableIds(true)
            setEmptyView(View.inflate(this@BaseListVMActivity, R.layout.layout_base_empty_data, null))
            loadMoreModule.setOnLoadMoreListener { loadMore() }
            loadMoreModule.isAutoLoadMore = true
            //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        }
        recyclerView.apply {
            layoutManager = provideLayoutManager()
            adapter = provideAdapter()
            setItemViewCacheSize(5)
        }

    }


    override fun initListener() {
        swipeRefreshLayout.setOnRefreshListener { refresh() }
    }

    override fun startObserve() {
        super.startObserve()
        viewModel.loadMoreState.observe(this) { data ->
            provideAdapter().loadMoreModule.isEnableLoadMore = true
            when (data) {
                BaseListViewModel.LoadMoreState.HAS_MORE -> {
                    provideAdapter().loadMoreModule.loadMoreComplete()
                }
                BaseListViewModel.LoadMoreState.NO_MORE -> {
                    provideAdapter().loadMoreModule.loadMoreEnd();
                }

            }
        }
    }

    override fun handleNetWorkError() {
        super.handleNetWorkError()
        swipeRefreshLayout.isEnabled = true
        swipeRefreshLayout.isRefreshing = false
        if (viewModel.pageInfo.isFirstPage) {
            provideAdapter().loadMoreModule.isEnableLoadMore = false
        } else {
            provideAdapter().loadMoreModule.loadMoreFail()
        }
    }

    //上拉刷新
    open protected fun refresh() {
        swipeRefreshLayout.isRefreshing = true
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        provideAdapter().loadMoreModule.isEnableLoadMore = false
        // 下拉刷新，需要重置页数
        viewModel.pageInfo.reset()
        viewModel.getFirstData()
    }

    /**
     * 加载更多
     */
    open protected fun loadMore() {
        swipeRefreshLayout.isEnabled = false
        viewModel.loadMore()
    }


    abstract fun provideLayoutManager(): RecyclerView.LayoutManager

    abstract fun provideAdapter(): BaseQuickAdapter<*, *>
}