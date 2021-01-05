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
//        mPresenter.getProcessProgress(detailedProId)

        for (i in 1..20) {
            var timeLineBean = ProcessProgressBean()
            timeLineBean.postName = "(职务$i)"
            timeLineBean.realName = "姓名$i"
            when {
                i % 2 == 0 -> timeLineBean.auditStatus = "已通过"
                i % 3 == 0 -> timeLineBean.auditStatus = "已驳回"
                else -> timeLineBean.auditStatus = "提交资料"
            }
            timeLineBean.createTime = "i"
            dataList.add(timeLineBean)
        }
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        time_line_list.layoutManager = linearLayoutManager
        time_line_list.addItemDecoration(TimeLineItemDecoration(this, dataList))
        var timeLineAdapter = ProcessProgressAdapter(dataList)
        time_line_list.adapter = timeLineAdapter
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

    override fun getProgressResult(data: MutableList<ProcessProgressBean>?) {
    }

}
