package com.zx.projectmanage.api


import com.frame.zxmvp.basebean.BaseRespose
import com.zx.projectmanage.base.NormalList
import com.zx.projectmanage.module.main.bean.UserBean
import com.zx.projectmanage.module.projectapplication.construction.bean.BaiduGeocoderBean
import com.zx.projectmanage.module.projectapplication.construction.bean.FileInfoBean
import com.zx.projectmanage.module.projectapplication.construction.bean.FileUploadBean
import com.zx.projectmanage.module.projectapplication.construction.bean.StepStandardBean
import com.zx.projectmanage.module.projectapplication.construction.bean.*
import com.zx.projectmanage.module.projectapplication.construction.dto.PostAuditDto
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
     * 查询项目列表
     */
    @GET("/business/app/buildpost/pageProject")
    fun getPageProject(
        @Query("districtCode") districtCode: String?,
        @Query("keyword") keyword: String?,
        @Query("pageNo") pageNo: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("projectStatus") projectStatus: String?,
        @Query("tenders") tenders: Int?,
        @Query("buildPeriod") buildPeriod: String?
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
    @GET("/business/app/buildpost/process/{subProjectId}/{processId}")
    fun getProcessProjectInfo(
        @Path("subProjectId") subProjectId: String,
        @Path("processId") processId: String
    ): Observable<BaseRespose<ProjectProcessInfoBean>>

    /**
     * 审批查询工序详情
     */
    @GET("/business/report/app/findProcess/{projectId}/{subProjectId}")
    fun getApproveProcessProjectInfo(
        @Path("projectId") projectId: String,
        @Path("subProjectId") subProjectId: String
    ): Observable<BaseRespose<MutableList<ApproveProcessInfoBean>>>

    /**
     * 查询工序已添加设备
     */
    @GET("/business/app/buildpost/list")
    fun getDeviceList(
        @QueryMap map: Map<String, String>
    ): Observable<BaseRespose<MutableList<DeviceListBean>>>

    /**
     * 审批查询工序设备
     */
    @GET("/business/report/app/equipments/{detailedProId}")
    fun getDeviceList(
        @Path("detailedProId") detailedProId: String
    ): Observable<BaseRespose<MutableList<DeviceListBean>>>

    /**
     * 提交审核
     */
    @POST("/business/report/app/submit")
    fun postSubmit(
        @Body body: RequestBody
    ): Observable<BaseRespose<Any>>

    /**
     * 查询工序进度
     */
    @GET("/business/report/app/progress/{detailedProId}")
    fun getProcessProgress(
        @Path("detailedProId") detailedProId: String
    ): Observable<BaseRespose<MutableList<ProcessProgressBean>>>

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
     * 获取工序文件根据iD
     */
    @GET("/admin/sys-file/getFileInfoById")
    fun getFile(
        @Query("id") id: String
    ): Observable<BaseRespose<FileInfoBean>>

    /**
     * 查询项目信息
     */
    @GET("/business/project/info/{projectId}")
    fun getProjectInformation(
        @Path("projectId") projectId: String
    ): Observable<BaseRespose<InformationBean>>


    /**
     * 审批项目列表
     */
    @GET("/business/report/app/findProjects")
    fun getApproveProject(
        @Query("districtCode") districtCode: String?,
        @Query("projectName") keyword: String?,
        @Query("pageNo") pageNo: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("projectStatus") projectStatus: String?,
        @Query("tenders") tenders: Int?,
        @Query("buildPeriod") buildPeriod: String?
    ): Observable<BaseRespose<ReportListBean>>

    /**
     * 查询考核模版
     */
    @GET("/business/zdhjcassessment/sub/{assessmentId}")
    fun getScoreTemple(
        @Path("assessmentId") subAssessmentId: String
    ): Observable<BaseRespose<MutableList<ScoreTemplateBean>>>


    /**
     * 查询子项目列表
     */
    @GET("/business/report/app/findSubProjects")
    fun getApproveSubProject(
        @QueryMap map: Map<String, String>
    ): Observable<BaseRespose<NormalList<ReportSubListBean>>>

    /**
     * 上传文件
     */
    @POST("admin/sys-file/upload")
    fun uploadFile(@Body body: RequestBody): Observable<BaseRespose<FileUploadBean>>


    @POST("/business/report/app/auditProcess")
    fun auditProcess(@Body body: PostAuditDto): Observable<BaseRespose<Any>>

    /**
     * 新增施工上报
     */
    @POST("business/app/buildpost/save")
    fun saveDataInfo(@Body body: RequestBody): Observable<BaseRespose<Any>>

    /**
     * 更新施工上报
     */
    @POST("business/app/buildpost/update")
    fun updateDataInfo(@Body body: RequestBody): Observable<BaseRespose<Any>>

    /**
     * 设备驳回
     */
    @POST("business/report/app/auditReject")
    fun doDeviceReject(@Body body: RequestBody): Observable<BaseRespose<Any>>

    /**
     * 设备通过
     */
    @POST("business/report/app/auditPass")
    fun doDevicePass(@Body body: RequestBody): Observable<BaseRespose<Any>>

    /**
     * 删除设备
     */
    @DELETE("business/app/buildpost/delete")
    fun deleteDevice(@Query("ids") ids: String): Observable<BaseRespose<Any>>

    /**
     * 设备详情
     */
    @GET("business/report/app/equipmentDetail/{standardProId}/{standardId}")
    fun getDeviceDetail(@Path("standardProId") standardProId: String, @Path("standardId") standardId: String): Observable<BaseRespose<DeviceListBean>>
}