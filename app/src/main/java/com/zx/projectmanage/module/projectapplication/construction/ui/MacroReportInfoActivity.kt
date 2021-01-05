package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectProcessInfoBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.MacroReportInfoContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.MacroReportInfoModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.MacroReportInfoPresenter
import com.zx.zxutils.views.TabViewPager.ZXTabViewPager
import kotlinx.android.synthetic.main.activity_contruction_macro_report_info.*


/**
 * Create By admin On 2017/7/11
 * 功能： 施工上报宏观查看
 */
class MacroReportInfoActivity : BaseActivity<MacroReportInfoPresenter, MacroReportInfoModel>(), MacroReportInfoContract.View {
    //工序list
    lateinit var listProcedure: MutableList<String>
    var processId = ""
    var projectId = ""
    var subProjectId = ""
    var assessmentId = ""
    var type = 0

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_contruction_macro_report_info
    }

    companion object {
        /**
         * 启动器
         */
        fun startAction(
            activity: Activity,
            isFinish: Boolean,
            processId: String,
            projectId: String,
            subProject: String,
            type: Int,
            assessmentId: String
        ) {
            val intent = Intent(activity, MacroReportInfoActivity::class.java)
            intent.putExtra("processId", processId)
            intent.putExtra("projectId", projectId)
            intent.putExtra("subProject", subProject)
            intent.putExtra("type", type)
            intent.putExtra("assessmentId", assessmentId)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }


    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        processId = intent.getStringExtra("processId").toString()
        projectId = intent.getStringExtra("projectId").toString()
        subProjectId = intent.getStringExtra("subProject").toString()
        mPresenter.getProcessInfo(processId)
    }

    private fun initTab(detailedList: List<ProjectProcessInfoBean.DetailedListBean?>?) {
        //设置工序Tab
        tvp_macro_report_layout.setManager(supportFragmentManager)
            .setTabScrollable(false)
            .setViewpagerCanScroll(false)
            .setTabLayoutGravity(ZXTabViewPager.TabGravity.GRAVITY_TOP)

        if (detailedList != null) {

            for (s in detailedList) {
                val bundle = Bundle()
                bundle.putSerializable("bean", s)
                bundle.putString("subProjectId", subProjectId)
                bundle.putString("projectId", projectId)
                bundle.putString("assessmentId", assessmentId)
                bundle.putInt("type", type)
                tvp_macro_report_layout.addTab(ProcedureReportFragment.newInstance(bundle), s?.subProcessName)
            }
        }
        tvp_macro_report_layout.setTitleColor(
            ContextCompat.getColor(this, R.color.default_text_color),
            ContextCompat.getColor(this, R.color.colorAccent)
        )
            .setTabTextSize(10)
            .setIndicatorHeight(8)
            .setIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setTablayoutHeight(50)
            .setTablayoutBackgroundColor(ContextCompat.getColor(this, R.color.white))
            .showDivider()
            .build()
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        tvp_macro_report_layout.tabLayout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {

            }
        })
        head.setLeftImageViewClickListener { finish() }

        basicInformation.setOnSuperTextViewClickListener {
            ProjectBaseInfomationActivity.startAction(this, false, projectId)
        }
    }

    override fun getDataProcessResult(data: ProjectProcessInfoBean?) {

        initTab(data?.detailedList)
    }

}
