package com.zx.projectmanage.module.projectapplication.approve.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.module.projectapplication.approve.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.approve.bean.ProjectProcessInfoBean
import com.zx.projectmanage.module.projectapplication.approve.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.approve.func.adapter.ProcedureListAdapter
import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ProcedureReportContract
import com.zx.projectmanage.module.projectapplication.approve.mvp.model.ProcedureReportModel
import com.zx.projectmanage.module.projectapplication.approve.mvp.presenter.ProcedureReportPresenter
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionDataActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.DocumentActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.ProcedureReportFragment
import com.zx.zxutils.util.ZXToastUtil
import kotlinx.android.synthetic.main.fragment_procedure_report.*

/**
 * Create By admin On 2017/7/11
 * 功能：工序fragment
 */
class ProcedureReportFragment : BaseFragment<ProcedureReportPresenter, ProcedureReportModel>(), ProcedureReportContract.View {
    private var list: MutableList<DeviceListBean> = arrayListOf<DeviceListBean>()
    private val reportListAdapter = com.zx.projectmanage.module.projectapplication.construction.func.adapter.ProcedureListAdapter(list)
    var subProjectId = ""
    var assessmentId = ""
    var parcelable: ProjectProcessInfoBean.DetailedListBean? = null

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
        parcelable = arguments?.getSerializable("bean") as ProjectProcessInfoBean.DetailedListBean
        subProjectId = arguments?.getString("subProjectId").toString()
        assessmentId = arguments?.getString("assessmentId").toString()
        if (parcelable?.showMaterials == 0) {
            materials.visibility = View.GONE
        }
        if (parcelable?.showOperationGuide == 0) {
            operationGuide.visibility = View.GONE
        }
        if (parcelable?.showSafetyRegulations == 0) {
            safetyRegulations.visibility = View.GONE
        }
        //设置adapter
        dataShow.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = reportListAdapter
//            addItemDecoration(SimpleDecoration(mContext))
        }
        mPresenter.getDeviceList(
            hashMapOf(
                "detailId" to parcelable?.processId.toString(),
                "subProjectId" to subProjectId
            )
        )
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        tv_report_addEquip.setOnClickListener {
            ConstructionDataActivity.startAction(requireActivity(), false)
        }
        materials.setOnSuperTextViewClickListener {
            val materials = parcelable?.materials
            if (materials != null) {
                ZXToastUtil.showToast("没有相关资料")
            } else {
                DocumentActivity.startAction(mContext as Activity, false, materials)
            }

        }
        operationGuide.setOnSuperTextViewClickListener {
            if (parcelable?.operationGuide!!.isEmpty()) {
                ZXToastUtil.showToast("没有相关资料")
            } else {
                DocumentActivity.startAction(mContext as Activity, false, parcelable?.operationGuide)
            }

        }
        safetyRegulations.setOnSuperTextViewClickListener {
            if (parcelable?.safetyRegulations!!.isEmpty()) {
                ZXToastUtil.showToast("没有相关资料")
            } else {
                DocumentActivity.startAction(mContext as Activity, false, parcelable?.safetyRegulations!!)
            }

        }
        btn_approve_submit.setOnClickListener {
            ApproveScoreActivity.startAction(activity as Activity, false, assessmentId)
        }
    }


    override fun getDeviceListResult(data: MutableList<DeviceListBean>?) {
        reportListAdapter.setNewData(
            data
        )
    }

}
