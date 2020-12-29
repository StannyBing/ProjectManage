package com.zx.projectmanage.api


import com.zx.projectmanage.module.main.bean.UserBean
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

interface ApiService {



    @POST("auth/oauth/token")
    fun doAppLogin(@Body body: RequestBody): Observable<UserBean>


}