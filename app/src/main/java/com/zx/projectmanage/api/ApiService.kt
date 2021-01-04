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


    /**
     * 用户登录
     */
    @POST("auth/oauth/token")
    fun doAppLogin(@Body body: RequestBody): Observable<UserBean>

    /**
     * 获取所有项目状态
     */
    @GET("/business/project/projectStatus")
    fun getProjectStatus(): Observable<BaseRespose<Any>>

    /**
     * 反向地理编码
     */
    @GET
    fun geocoder(@Url url: String): Observable<BaiduGeocoderBean>

    /**
     * 获取所有项目期次
     */
    @GET("/admin/dict/type/period")
    fun getProjectPeriod(): Observable<BaseRespose<MutableList<ProjectPeriodBean>>>

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

    /**
     * 获取工序步骤模板
     */
    @GET("/business/standard/page")
    fun getStepStandard(@QueryMap map: Map<String, String>): Observable<BaseRespose<NormalList<StepStandardBean>>>

    /**
     * 查询模板详情
     */
    @GET("/business/standard/info/{id}")
    fun getStepDetail(
        @Path("id") id: String
    ): Observable<BaseRespose<StepStandardBean>>

    /**
     * 上传文件
     */
    @POST("admin/sys-file/upload")
    fun uploadFile(@Body body: RequestBody) : Observable<BaseRespose<FileUploadBean>>

    /**
     * 新增施工上报
     */
    @POST("business/app/buildpost/save")
    fun saveDataInfo(@Body body: RequestBody) : Observable<BaseRespose<Any>>
}