package com.zx.projectmanage.module.projectapplication.construction.mvp.presenter

import com.frame.zxmvp.basebean.BaseRespose
import com.frame.zxmvp.baserx.RxHelper
import com.frame.zxmvp.baserx.RxSubscriber
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.base.UserManager
import com.zx.projectmanage.module.main.bean.UserBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.dto.ReportListDto
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportContract
import com.zx.zxutils.util.ZXToastUtil


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionReportPresenter : ConstructionReportContract.Presenter() {
    override fun getPageProject(
        districtCode: String?,
        keyword: String?,
        pageNo: Int?,
        pageSize: Int?,
        projectStatus: Int?,
        tenders: Int?
    ) {
        ApiConfigModule.COOKIE = ""
        mModel.getPageProject(districtCode, keyword, pageNo, pageSize, projectStatus, tenders)
            .compose(RxHelper.bindToLifecycle(mView))
            .subscribe(object : RxSubscriber<ReportListBean>(mView) {
                override fun _onNext(t: ReportListBean?) {
                    mView.getDataResult(t)
                }

                override fun _onError(code: Int, message: String?) {
                    mView.handleError(code, "请求失败，请检查网络后再试")
                    mView.getDataResult(null)

                }
            })
    }


}