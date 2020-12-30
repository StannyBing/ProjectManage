package com.zx.projectmanage.api


import com.zx.projectmanage.module.main.bean.UserBean
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

interface ApiService {



    @POST("auth/oauth/token")
    fun doAppLogin(@Body body: RequestBody): Observable<UserBean>

    @GET
    fun geocoder(@Url url : String) : Observable<BaiduGeocoderBean>

    @GET("/app/buildpost/pageProject")
    fun getPageProject(
        @Query("districtCode") districtCode: String?,
        @Query("keyword") keyword: String?,
        @Query("pageNo") pageNo: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("projectStatus") projectStatus: Int?,
        @Query("tenders") tenders: Int?
    ): Observable<BaseRespose<ReportListBean>>


}