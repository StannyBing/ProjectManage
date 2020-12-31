package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.InformationBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.BaseInfomationAdapter

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProjectBaseInfomationContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ProjectBaseInfomationModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ProjectBaseInfomationPresenter


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ProjectBaseInfomationActivity : BaseActivity<ProjectBaseInfomationPresenter, ProjectBaseInfomationModel>(), ProjectBaseInfomationContract.View {
    private var list: MutableList<InformationBean> = arrayListOf<InformationBean>()
    val adapter = BaseInfomationAdapter(list)

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, ProjectBaseInfomationActivity::class.java)
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
        list.add(InformationBean("项目概况", "", 1))
        list.add(InformationBean("项目编号", "qz12321321321", 2))
        list.add(InformationBean("项目名称", "唐家沱施工", 2))
        list.add(InformationBean("项目标段", "第一段", 2))
        list.add(InformationBean("建设期次", "1", 2))
        list.add(InformationBean("备案号", "2020091112", 2))
        list.add(InformationBean("项目配置", "", 1))
        list.add(InformationBean("工序方案", "qz12321321321", 2))
        list.add(InformationBean("竣工时间", "2020-1-1", 2))
        list.add(InformationBean("项目状态", "未竣工", 2))
        list.add(InformationBean("总质评分", "99", 2))

    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

}
