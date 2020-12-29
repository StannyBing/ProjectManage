package com.zx.projectmanage.module.projectapplication.func.tool

import android.view.View
import androidx.viewpager.widget.ViewPager

class BannerPageTransformer : ViewPager.PageTransformer {

    private val maxScale = 1f
    private val minScale = 0.9f

    override fun transformPage(page: View, position: Float) {
        val scale = if (position < 0f) {
            minScale + (position + 1f) * (maxScale - minScale)
        } else if (position > 0f) {
            maxScale - position * (maxScale - minScale)
        } else {
            maxScale
        }
//        page.scaleX = scale
        page.scaleY = scale
    }
}