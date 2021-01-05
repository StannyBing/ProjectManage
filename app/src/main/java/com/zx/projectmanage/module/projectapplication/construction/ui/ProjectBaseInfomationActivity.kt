package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity

import com.zx.projectmanage.module.projectapplication.construction.bean.InformationBean
import com.zx.projectmanage.module.projectapplication.construction.bean.InformationListBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.BaseInfomationAdapter
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProjectBaseInfomationContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ProjectBaseInfomationModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ProjectBaseInfomationPresenter
import kotlinx.android.synthetic.main.activity_project_base_infomation.*

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProjectBaseInfomationActivity : BaseActivity<ProjectBaseInfomationPresenter, ProjectBaseInfomationModel>(), ProjectBaseInfomationContract.View {
    private var list: MutableList<InformationListBean> = arrayListOf<InformationListBean>()
    val mAdapter = BaseInfomationAdapter(list)
    var projectId = ""

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, projectId: String) {
            val intent = Intent(activity, ProjectBaseInfomationActivity::class.java)
            intent.putExtra("projectId", projectId)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_project_base_infomation
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        projectId = intent.getStringExtra("projectId").toString()
        //设置adapter
        infomationData.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = mAdapter
//            addItemDecoration(SimpleDecoration(mContext))
        }

        mPresenter.getProjectInformation(projectId)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

    override fun getDataInformationResult(data: InformationBean?) {

        list.add(InformationListBean("项目概况", "", 1))
        list.add(InformationListBean("项目编号", data?.projectNumber, 2))
        list.add(InformationListBean("项目名称", data?.projectName, 2))
        list.add(InformationListBean("项目标段", data?.tenders, 2))
        list.add(InformationListBean("建设期次", data?.buildPeriod, 2))
        list.add(InformationListBean("备案号", data?.recordNo, 2))
        list.add(InformationListBean("项目配置", "", 1))
        list.add(InformationListBean("工序方案", "", 2))
        list.add(InformationListBean("竣工时间", data?.completedTime, 2))
        list.add(InformationListBean("项目状态", data?.projectStatus.toString(), 2))
        list.add(InformationListBean("总质评分", data?.score.toString(), 2))

        val participates = data?.participates
        participates?.forEach {
            if (it?.type == "1") {
                list.add(InformationListBean("监理", "", 1))
                list.add(InformationListBean("单位名称", it.orgName, 2))
                list.add(InformationListBean("资质", "优等", 2))
                list.add(InformationListBean("单位负责人", it.chargeUser, 2))
                list.add(InformationListBean("联系方式", it.contactWay, 2))
                list.add(InformationListBean("监督员", it.superviseUser, 2))
            } else if (it?.type == "2") {
                list.add(InformationListBean("监理", "", 1))
                list.add(InformationListBean("单位名称", it.orgName, 2))
                list.add(InformationListBean("资质", "优等", 2))
                list.add(InformationListBean("单位负责人", it.chargeUser, 2))
                list.add(InformationListBean("联系方式", it.contactWay, 2))
                list.add(InformationListBean("监督员", it.superviseUser, 2))
            } else if (it?.type == "3") {
                list.add(InformationListBean("设计", "", 1))
                list.add(InformationListBean("单位名称", it.orgName, 2))
                list.add(InformationListBean("资质", "优等", 2))
                list.add(InformationListBean("单位负责人", it.chargeUser, 2))
                list.add(InformationListBean("联系方式", it.contactWay, 2))
                list.add(InformationListBean("监督员", it.superviseUser, 2))
            } else if (it?.type == "4") {
                list.add(InformationListBean("施工", "", 1))
                list.add(InformationListBean("单位名称", it.orgName, 2))
                list.add(InformationListBean("资质", "优等", 2))
                list.add(InformationListBean("单位负责人", it.chargeUser, 2))
                list.add(InformationListBean("联系方式", it.contactWay, 2))
                list.add(InformationListBean("监督员", it.superviseUser, 2))
            }
        }
        list.add(InformationListBean("其他", "", 1))
        list.add(InformationListBean("工程概况", data?.projectSurvey, 2))
        list.add(InformationListBean("工程措施", data?.projectMeasures, 2))
        list.add(InformationListBean("备注", data?.remarks, 2))
        mAdapter.setNewData(list)
    }

}
