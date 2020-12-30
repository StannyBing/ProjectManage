package com.zx.projectmanage.module.projectapplication.construction.func.tool

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import androidx.appcompat.widget.AppCompatEditText
import kotlinx.android.synthetic.main.activity_construction_report.*

/**
Date:2020/12/30
Time:4:58 PM
author:qingsong
 */
fun AppCompatEditText.setHintKtx(size: Int, content: String) {
    val s = SpannableString(content)
    val textSize = AbsoluteSizeSpan(size, true)
    s.setSpan(textSize, 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    hint = s
}