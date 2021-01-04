package com.zx.projectmanage.module.projectapplication.approve.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.approve.bean.InformationBean
import com.zx.projectmanage.module.projectapplication.approve.func.adapter.BaseInfomationAdapter

import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ProjectBaseInfomationContract
import com.zx.projectmanage.module.projectapplication.approve.mvp.model.ProjectBaseInfomationModel
import com.zx.projectmanage.module.projectapplication.approve.mvp.presenter.ProjectBaseInfomationPresenter
import com.zx.projectmanage.module.projectapplication.construction.ui.ProjectBaseInfomationActivity


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProjectBaseInfomationActivity : BaseActivity<ProjectBaseInfomationPresenter, ProjectBaseInfomationModel>(), ProjectBaseInfomationContract.View {
    private var list: MutableList<InformationBean> = arrayListOf<InformationBean>()
    val adapter = BaseInfomationAdapter(list)
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
        mPresenter.getProjectInformation(projectId)

    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

    override fun getDataInformationResult(data: InformationBean?) {

        list.add(InformationBean("项目概况", "", 1))
        list.add(InformationBean("项目编号", data?.projectNumber, 2))
        list.add(InformationBean("项目名称", data?.projectName, 2))
        list.add(InformationBean("项目标段", data?.tenders, 2))
        list.add(InformationBean("建设期次", data?.recordNo, 2))
        list.add(InformationBean("备案号", data?.recordNo, 2))
        list.add(InformationBean("项目配置", "", 1))
        list.add(InformationBean("工序方案", "qz12321321321", 2))
        list.add(InformationBean("竣工时间", "2020-1-1", 2))
        list.add(InformationBean("项目状态", "", 2))
        list.add(InformationBean("总质评分", data?.score.toString(), 2))
        list.add(InformationBean("监理", "", 1))
        list.add(InformationBean("单位名称", "远方科技", 2))
        list.add(InformationBean("资质", "优等", 2))
        list.add(InformationBean("单位负责人", "xx", 2))
        list.add(InformationBean("联系方式", "", 2))
        list.add(InformationBean("监督员", "", 2))
        list.add(InformationBean("设计", "", 1))
        list.add(InformationBean("单位名称", "远方科技", 2))
        list.add(InformationBean("资质", "优等", 2))
        list.add(InformationBean("单位负责人", "xx", 2))
        list.add(InformationBean("联系方式", "", 2))
        list.add(InformationBean("监督员", "", 2))
        list.add(InformationBean("施工", "", 1))
        list.add(InformationBean("单位名称", "远方科技", 2))
        list.add(InformationBean("资质", "优等", 2))
        list.add(InformationBean("单位负责人", "xx", 2))
        list.add(InformationBean("联系方式", "", 2))
        list.add(InformationBean("监督员", "", 2))
        list.add(InformationBean("其他", "", 1))
        list.add(InformationBean("工程概况", "远方科技", 2))
        list.add(InformationBean("资质", "优等", 2))
        list.add(InformationBean("单位负责人", "xx", 2))
        list.add(InformationBean("工程措施", "", 2))
        list.add(InformationBean("备注", "", 2))
    }

}
