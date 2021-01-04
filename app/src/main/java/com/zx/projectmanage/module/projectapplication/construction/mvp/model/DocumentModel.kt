package com.zx.projectmanage.module.projectapplication.construction.mvp.model

import com.frame.zxmvp.base.BaseModel
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSchedulers
import com.zx.projectmanage.api.ApiService
import com.zx.projectmanage.module.projectapplication.approve.bean.FileInfoBean

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.DocumentContract
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class DocumentModel : BaseModel(), DocumentContract.Model {
    override fun getFileInfo(id: String): Observable<FileInfoBean> {
        return mRepositoryManager.obtainRetrofitService(ApiService::class.java)
            .getFile(id)
            .compose(RxHelper.handleResult())
            .compose(RxSchedulers.io_main())
    }


}