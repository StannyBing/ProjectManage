package com.zx.projectmanage.api


import com.frame.zxmvp.basebean.BaseRespose
import com.gt.giscollect.base.NormalList
import com.zx.projectmanage.module.main.bean.UserBean
import com.zx.projectmanage.module.projectapplication.construction.bean.*
import com.zx.projectmanage.module.projectapplication.construction.dto.ReportListDto
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

interface ApiService {


    @POST("auth/oauth/token")
    fun doAppLogin(@Body body: RequestBody): Observable<UserBean>

    /**
     * 获取所有项目状态
     */
    @GET("/business/project/projectStatus")
    fun getProjectStatus(): Observable<BaseRespose<Any>>

    /**
     * 获取所有项目期次
     */
    @GET("/admin/dict/type/period")
    fun getProjectPeriod(): Observable<BaseRespose<MutableList<ProjectPeriodBean>>>


    @GET
    fun geocoder(@Url url: String): Observable<BaiduGeocoderBean>

    /**
     * 插叙项目列表
     */
    @GET("/business/app/buildpost/pageProject")
    fun getPageProject(
        @Query("districtCode") districtCode: String?,
        @Query("keyword") keyword: String?,
        @Query("pageNo") pageNo: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("projectStatus") projectStatus: Int?,
        @Query("tenders") tenders: Int?
    ): Observable<BaseRespose<ReportListBean>>


    /**
     * 查询子项目列表
     */
    @GET("/business/app/buildpost/pageSubProject")
    fun getPageSubProject(
        @QueryMap map: Map<String, String>
    ): Observable<BaseRespose<NormalList<ReportSubListBean>>>

    /**
     * 查询工序详情
     */
    @GET("/business//app/buildpost/process/{processId}")
    fun getProcessProjectInfo(
        @Path("processId") processId: String
    ): Observable<BaseRespose<ProjectProcessInfoBean>>


}