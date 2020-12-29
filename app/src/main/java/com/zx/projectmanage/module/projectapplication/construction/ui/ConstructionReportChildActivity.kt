package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.base.SimpleDecoration
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ReportChildListAdapter

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportChildContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ConstructionReportChildModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ConstructionReportChildPresenter
import com.zx.zxutils.util.ZXToastUtil
import kotlinx.android.synthetic.main.activity_construction_report.*
import kotlinx.android.synthetic.main.activity_construction_report_child.head
import kotlinx.android.synthetic.main.activity_construction_report_child.swipeRecyler


/**
 * Create By admin On 2017/7/11
 * 功能： 项目列表
 */
class ConstructionReportChildActivity : BaseActivity<ConstructionReportChildPresenter, ConstructionReportChildModel>(), ConstructionReportChildContract.View {
    private var list: MutableList<ReportListBean> = arrayListOf<ReportListBean>()
    private val reportListAdapter = ReportChildListAdapter(list)

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, no: Int, name: String) {
            val intent = Intent(activity, ConstructionReportChildActivity::class.java)
            intent.putExtra("projectName", name)
            intent.putExtra("projectNo", no)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_construction_report_child
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //获取title并设置
        head.setCenterString(intent.getStringExtra("projectName"))
        //模拟数据
        for (index in 0..20) {
            list.add(ReportListBean(index))
        }
        //设置列表adapter
        swipeRecyler.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = reportListAdapter
            addItemDecoration(SimpleDecoration(mContext))
        }
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        reportListAdapter.setOnItemClickListener { adapter, view, position ->
            MacroReportInfoActivity.startAction(mContext as Activity, false)

        }

        //头部点击事件
        head.setRightImageViewClickListener {
            ZXToastUtil.showToast("点击右侧筛选")
        }.setLeftImageViewClickListener {
            finish()
        }

    }

}
