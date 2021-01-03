package com.jansir.core.base.activity

import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.jansir.core.R
import com.jansir.core.databinding.ActivityBaseImageBrowseBinding
import com.jansir.core.ext.getExtraString
import com.jansir.core.ext.getExtraStringList
import com.jansir.core.ext.inflateLazyVB
import com.jansir.core.util.StatusBarUtil


/**
 * TAG_ARTIST: jansir.
 * package: com.reachjunction.exifmaster.ui.
 * date: 2019/7/25.
 */

class ImageBrowseActivity : BaseActivity<ActivityBaseImageBrowseBinding>() {


    companion object {
        val EXTRA_IMAGE_PATH = "image_path"
        val EXTRA_IMAGE_LIST = "image_list"
    }


    override val isUseBaseTitleBar: Boolean
        get() = false


    override fun initView() {
        imagePath = getExtraString(EXTRA_IMAGE_PATH);
        imageList = getExtraStringList((EXTRA_IMAGE_LIST))
        //设置全屏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        imagePath?.apply {
            if (contains("gif")) {
                loadGif();
            } else {
                loadImage()
            }
        }

    }

    override fun initListener() {
    }

    private var imagePath: String? = ""
    private var imageList: ArrayList<String>? = null
    private var imagePosition = 0


    //加载gif图片
    private fun loadGif() {
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
    private fun loadImage() {
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
                    binding.pvImage.setOnClickListener({ view -> finish() })
                }
            })
    }

    override fun initData() {
    }


}