package com.zx.projectmanage.api


import com.frame.zxmvp.basebean.BaseRespose
import com.zx.projectmanage.module.main.bean.UserBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.dto.ReportListDto
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

interface ApiService {


    @POST("auth/oauth/token")
    fun doAppLogin(@Body body: RequestBody): Observable<UserBean>


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