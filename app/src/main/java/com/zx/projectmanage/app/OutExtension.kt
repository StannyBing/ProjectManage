package com.zx.projectmanage.app

import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

/**
 * Created by Xiangb on 2019/8/1.
 * 功能：
 */
fun <K, Y> Map<K, Y>.toJson(): RequestBody {
    val json = Gson().toJson(this)
    val map = this
//    return RequestBody.create(MediaType.parse("application/json; charset=utf-8; form-data"), )
//    return RequestBody.create(MediaType.parse("multipart/form-data"), json)
    return MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .apply {
            map.keys.forEach {
                this.addFormDataPart(it.toString(), map[it].toString())
            }
        }
        .build()
}

fun Paint.getBaseline(): Float {
    return (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
}
