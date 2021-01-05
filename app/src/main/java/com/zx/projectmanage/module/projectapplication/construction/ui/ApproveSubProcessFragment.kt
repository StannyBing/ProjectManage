package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.module.projectapplication.construction.bean.ApproveProcessInfoBean
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.dto.PostAuditDto
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ApproveListAdapter
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ApproveSubProcessContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ApproveSubProcessModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ApproveSubProcessPresenter
import com.zx.zxutils.util.ZXToastUtil
import kotlinx.android.synthetic.main.fragment_approve_sub_process.*

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveSubProcessFragment : BaseFragment<ApproveSubProcessPresenter, ApproveSubProcessModel>(), ApproveSubProcessContract.View {
    private var list: MutableList<DeviceListBean> = arrayListOf<DeviceListBean>()
    private val reportListAdapter = ApproveListAdapter(list)
    var subProjectId = ""
    var projectId = ""

    //工序模版id
    var assessmentId = ""
    var parcelable: ApproveProcessInfoBean? = null

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_approve_sub_process
    }

    companion object {
        /**
         * 启动器
         */
        fun newInstance(bundle: Bundle? = null): ApproveSubProcessFragment {
            val fragment = ApproveSubProcessFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        parcelable = arguments?.getSerializable("bean") as ApproveProcessInfoBean
        subProjectId = arguments?.getString("subProjectId").toString()
        projectId = arguments?.getString("projectId").toString()
        assessmentId = arguments?.getString("assessmentId").toString()

        if (parcelable?.materials == null) {
            materials.visibility = View.GONE
        }
        if (parcelable?.operationGuide == null) {
            operationGuide.visibility = View.GONE
        }
        if (parcelable?.safetyRegulations == null) {
            safetyRegulations.visibility = View.GONE
        }
        if (parcelable?.scoreFlag == 1) {
            btn_approve_submit.visibility = View.VISIBLE
        }
        if (parcelable?.auditFlag == 1) {
            btn_audit_reject.visibility = View.VISIBLE
            btn_audit_pass.visibility = View.VISIBLE
        }
        sv_score.setRightString(parcelable?.score.toString())
        //设置adapter
        dataShow.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = reportListAdapter
//            addItemDecoration(SimpleDecoration(mContext))
        }
        mPresenter.getDeviceList(parcelable?.detailedProId.toString())
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        process_progress.setOnSuperTextViewClickListener {
            ProjectProgressActivity.startAction(activity as Activity, false, parcelable?.detailedProId.toString())
        }
        reportListAdapter.setOnItemClickListener { adapter, view, position ->
            val deviceListBean = adapter.data[position] as DeviceListBean
            DeviceAuditActivity.startAction(requireActivity(), false, deviceListBean, 1)

        }
        materials.setOnSuperTextViewClickListener {
            val material = parcelable?.materials
            if (material != null) {
                ZXToastUtil.showToast("没有相关资料")
            } else {
                DocumentActivity.startAction(mContext as Activity, false, material)
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
            val dto = initDto()
            ApproveScoreActivity.startAction(
                activity as Activity, false,
                assessmentId,
                dto
            )
        }
        btn_audit_reject.setOnClickListener {
            val dto = initDto()
            mPresenter.postAuditProcess(dto)
        }
    }

    private fun initDto(): PostAuditDto {
        val dto = PostAuditDto()
        dto.projectId = projectId
        dto.subProjectId = subProjectId
        var listVos: MutableList<PostAuditDto.ReportEquipmentVosBean> = ArrayList()
        list.forEach {
            val reportEquipmentVosBean = PostAuditDto.ReportEquipmentVosBean()
            reportEquipmentVosBean.auditStatus = it.auditStatus
            reportEquipmentVosBean.detailedProId = it.detailedProId
            reportEquipmentVosBean.equipmentName = it.equipmentName
            reportEquipmentVosBean.standardId = it.standardId
            reportEquipmentVosBean.standardProId = it.standardProId
            listVos.add(reportEquipmentVosBean)
        }

        dto.reportEquipmentVos = listVos
        return dto
    }


    override fun getDeviceListResult(data: MutableList<DeviceListBean>?) {
        if (data != null) {
            list = data
        }
        reportListAdapter.setNewData(
            data
        )
    }

    override fun auditProcessResult(data: Any?) {

    }
}
