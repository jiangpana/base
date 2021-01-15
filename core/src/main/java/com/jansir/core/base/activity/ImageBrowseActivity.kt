package com.jansir.core.base.activity

import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.jansir.core.databinding.ActivityBaseImageBrowseBinding
import com.jansir.core.ext.getExtraString
import com.jansir.core.ext.getExtraStringList
import com.jansir.core.util.StatusBarUtil


/**
 * TAG_ARTIST: jansir.
 * package: com.reachjunction.exifmaster.ui.
 * date: 2019/7/25.
 */

class ImageBrowseActivity : BaseActivity<ActivityBaseImageBrowseBinding>() {


    companion object {
        const val EXTRA_IMAGE_PATH = "image_path"
        const val EXTRA_IMAGE_LIST = "image_list"
    }

    override val isUseBaseTitleBar: Boolean
        get() = false

    private val imagePath by lazy {
        getExtraString(EXTRA_IMAGE_PATH)
    }
    private val imageList by lazy {
        getExtraStringList(EXTRA_IMAGE_LIST)
    }

    override fun initView() {
        //设置全屏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
    }

    override fun initListener() {

    }

    override fun initData() {
        imagePath?.apply {
            if (contains("gif")) {
                displayGif();
            } else {
                displayImage()
            }
        }
    }

    /**
     *
     * 加载gif图片
     */
    private fun displayGif() {
        Glide.with(this@ImageBrowseActivity)
            .load(imagePath)
            .apply(RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
            .into(object : DrawableImageViewTarget(binding.pvImage) {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    super.onResourceReady(resource, transition)
                    binding.pvImage.setImageDrawable(resource)
                    binding.pb.visibility = View.GONE
                    binding.pvImage.setOnClickListener { view -> finish() }
                }

            })
    }

    //加载图片
    private fun displayImage() {
        Glide.with(this@ImageBrowseActivity)
            .load(imagePath)
            .apply(RequestOptions().fitCenter())
            .into(object : DrawableImageViewTarget(binding.pvImage) {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    super.onResourceReady(resource, transition)
                    binding.pvImage.setImageDrawable(resource)
                    binding.pb.visibility = View.GONE
                    binding.pvImage.setOnClickListener { finish() }
                }
            })
    }


}