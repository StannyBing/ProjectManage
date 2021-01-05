package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.ProcessProgressBean

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProjectProgressContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ProjectProgressModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ProjectProgressPresenter


/**
 * Create By admin On 2017/7/11
 * 功能：工序进度
 */
class ProjectProgressActivity : BaseActivity<ProjectProgressPresenter, ProjectProgressModel>(), ProjectProgressContract.View {

    var detailedProId = ""

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, detailedProId: String?) {
            val intent = Intent(activity, ProjectProgressActivity::class.java)
            intent.putExtra("detailedProId", detailedProId)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_project_progress
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        detailedProId = intent.getStringExtra("detailedProId").toString()
        super.initView(savedInstanceState)
        mPresenter.getProcessProgress(detailedProId)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

    override fun getProgressResult(data: MutableList<ProcessProgressBean>?) {
    }

}