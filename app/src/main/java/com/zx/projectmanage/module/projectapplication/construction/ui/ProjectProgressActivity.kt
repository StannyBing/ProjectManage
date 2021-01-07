package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.ProcessProgressBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ProcessProgressAdapter
import com.zx.projectmanage.module.projectapplication.construction.func.tool.TimeLineItemDecoration

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProjectProgressContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ProjectProgressModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ProjectProgressPresenter
import kotlinx.android.synthetic.main.activity_project_progress.*


/**
 * Create By admin On 2017/7/11
 * 功能：工序进度
 */
class ProjectProgressActivity : BaseActivity<ProjectProgressPresenter, ProjectProgressModel>(), ProjectProgressContract.View {
    var dataList = ArrayList<ProcessProgressBean>()
    var detailedProId = ""
    var timeLineAdapter = ProcessProgressAdapter(dataList)

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

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        time_line_list.layoutManager = linearLayoutManager
        time_line_list.adapter = timeLineAdapter
        timeLineAdapter.setEmptyView(R.layout.item_empty, time_line_list)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        head.setLeftImageViewClickListener {
            finish()
        }

    }

    override fun getProgressResult(data: MutableList<ProcessProgressBean>?) {
        if (data != null) {
            if (data.size>0){
                time_line_list.addItemDecoration(TimeLineItemDecoration(this, data))
                timeLineAdapter.setNewData(data)
            }

        }
    }

}
