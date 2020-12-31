package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.base.SimpleDecoration
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectProcessInfoBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ProcedureListAdapter
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ReportListAdapter
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProcedureReportContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ProcedureReportModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ProcedureReportPresenter
import com.zx.zxutils.util.ZXToastUtil
import kotlinx.android.synthetic.main.activity_construction_report.*
import kotlinx.android.synthetic.main.activity_construction_report.head
import kotlinx.android.synthetic.main.activity_construction_report.swipeRecyler
import kotlinx.android.synthetic.main.activity_construction_report_child.*
import kotlinx.android.synthetic.main.fragment_procedure_report.*

/**
 * Create By admin On 2017/7/11
 * 功能：工序fragment
 */
class ProcedureReportFragment : BaseFragment<ProcedureReportPresenter, ProcedureReportModel>(), ProcedureReportContract.View {
    private var list: MutableList<ReportListBean> = arrayListOf<ReportListBean>()
    private val reportListAdapter = ProcedureListAdapter(list)

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_procedure_report
    }

    companion object {
        /**
         * 启动器
         */
        fun newInstance(bundle: Bundle? = null): ProcedureReportFragment {
            val fragment = ProcedureReportFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        val parcelable = arguments?.getSerializable("bean") as ProjectProcessInfoBean.DetailedListBean
        if (parcelable.showMaterials == 0) {
            materials.visibility = View.GONE
        }
        if (parcelable.showOperationGuide == 0) {
            operationGuide.visibility = View.GONE
        }
        if (parcelable.showSafetyRegulations == 0) {
            safetyRegulations.visibility = View.GONE
        }
        //设置adapter
        dataShow.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = reportListAdapter
//            addItemDecoration(SimpleDecoration(mContext))
        }
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }
}
