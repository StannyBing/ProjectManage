package com.zx.projectmanage.module.main.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService

import com.zx.projectmanage.module.main.mvp.contract.FeedbackContract
import com.zx.projectmanage.module.projectapplication.construction.bean.FileUploadBean
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class FeedbackModel : BaseModel(), FeedbackContract.Model {
    override fun uploadFileData(body: RequestBody): Observable<FileUploadBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .uploadFile(body)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }

    override fun saveInfoData(body: RequestBody): Observable<Any> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .postFeedBack(body)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}