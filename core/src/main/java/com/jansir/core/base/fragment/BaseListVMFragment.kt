package com.jansir.core.base.fragment

import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jansir.core.base.viewmodel.BaseListViewModel
import com.jansir.core.databinding.LayoutBaseEmptyDataBinding


/**
 * author: jansir
 * e-mail: xxx
 * date: 2020/5/6.
 */

abstract class BaseListVMFragment<VM : BaseListViewModel<*>>() :
    BaseVMFragment<VM, com.jansir.core.databinding.ActivityBaseRefreshListBinding>() {


    private val recyclerView: RecyclerView by lazy {
        binding.rvBase
    }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        binding.slBase
    }

    override fun initView() {
        initRecyclerViewAdapter()
    }

    override fun initData() {
        refresh()
    }

    private fun initRecyclerViewAdapter() {
        provideAdapter().apply {
            setHasStableIds(true)
            setEmptyView(LayoutBaseEmptyDataBinding.inflate(layoutInflater).root)
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

    /**
     * TODO
     * 刷新
     */
    @Synchronized
    protected open fun refresh() {
        swipeRefreshLayout.isRefreshing = true
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        provideAdapter().loadMoreModule.isEnableLoadMore = false
        // 下拉刷新，需要重置页数
        viewModel.pageInfo.reset()
        viewModel.requestFirstData()
    }

    /**
     * TODO
     * 加载更多
     */
    protected open fun loadMore() {
        swipeRefreshLayout.isEnabled = false
        viewModel.loadMore()
    }


    abstract fun provideLayoutManager(): RecyclerView.LayoutManager
    abstract fun provideAdapter(): BaseQuickAdapter<*, *>

}