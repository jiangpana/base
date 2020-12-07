package com.jansir.core.base.fragment


/**
 * author: jansir
 * e-mail: xxx
 * date: 2020/5/6.
 */
//abstract class BasePagingVMFragment<M, VM : BaseLPagingModel<M>, VH : RecyclerView.ViewHolder> :
//    BaseVMFragment<VM>(),
//    OnLoadMoreListener, OnRefreshListener {
//
//    private val mAdapter: PagedListAdapter<M, VH> by lazy { getAdapter() }
//
//    override fun initView() {
//        mRefreshLayout.run {
//            setOnRefreshListener(this@BasePagingVMFragment)
//            setOnLoadMoreListener(this@BasePagingVMFragment)
//        }
//
//        recyclerView.layoutManager = LinearLayoutManager(_mActivity)
//        recyclerView.adapter = mAdapter
//
//        viewModel.mBoundaryData.observe(this, Observer {
//           if(it ==true){
//               mMultipleStatusView.viewState = MultiStateView.ViewState.CONTENT
//           }
//        })
//        viewModel.loadMoreState.observe(this, Observer {
//            mRefreshLayout.setEnableLoadMore(it)//上拉加载进度条只有在Paging加载更多失败时才有效(用于规避Paging加载更多失败后，无法再次加载问题)
//        })
//
//        afterViewCreated()
//    }
//
//    override fun initData() {
//        mRefreshLayout.autoRefreshAnimationOnly()
//        viewModel.pagedList.observe(this, Observer<PagedList<M>> {
//            mAdapter.submitList(it)
//        })
//    }
//
//    override fun onRefresh(refreshLayout: RefreshLayout) {
//        viewModel.refresh()
//    }
//
//    override fun onLoadMore(refreshLayout: RefreshLayout) {
//        viewModel.loadMoreRetry()
//    }
//
//    override fun dismissLoading() {
//        mRefreshLayout.run {
//            finishRefresh()
//            finishLoadMore()
//        }
//    }
//
//    abstract fun afterViewCreated()
//
//    abstract fun getAdapter(): PagedListAdapter<M, VH>
//
//
//}