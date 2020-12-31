package com.zx.projectmanage.module.projectapplication.construction.mvp.contract


import com.frame.zxmvp.base.BasePresenter
import com.frame.zxmvp.base.IView
import com.frame.zxmvp.base.IModel
import com.frame.zxmvp.basebean.BaseRespose
import com.gt.giscollect.base.NormalList
import com.zx.projectmanage.module.main.bean.UserBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectPeriodBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectStatusBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.dto.ReportListDto
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
interface ConstructionReportContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun getDataResult(baseRespose: ReportListBean?)
        fun getProjectStatusResult(baseRespose: Any?)
        fun getProjectPeriodResult(data: MutableList<ProjectPeriodBean>?)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun getPageProject(
            districtCode: String? = null,
            keyword: String? = null,
            pageNo: Int? = 1,
            pageSize: Int? = 10,
            projectStatus: Int? = null,
            tenders: Int? = null
        ): Observable<ReportListBean>

        fun getProjectStatus(): Observable<Any>
        fun getProjectPeriod(): Observable<MutableList<ProjectPeriodBean>>
    }

    //方法
    abstract class Presenter : BasePresenter<View, Model>() {
        abstract fun getPageProject(
            districtCode: String? = null,
            keyword: String? = null,
            pageNo: Int? = 1,
            pageSize: Int? = 10,
            projectStatus: Int? = null,
            tenders: Int? = null
        )

        abstract fun getProjectStatus()
        abstract fun getProjectPeriod()

    }
}

