package com.zx.projectmanage.module.projectapplication.func.tool

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.loader.ImageLoader

/**
 * Created by Xiangb on 2019/5/29.
 * 功能：
 */
class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        Glide.with(context!!)
                .load(path)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                .into(imageView!!)
    }
}