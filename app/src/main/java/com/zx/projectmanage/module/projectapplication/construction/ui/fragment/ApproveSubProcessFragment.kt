package com.zx.projectmanage.module.projectapplication.construction.ui.fragment

import android.app.Activity
import android.content.Intent
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
import com.zx.projectmanage.module.projectapplication.construction.ui.ApproveScoreActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.DeviceAuditActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.DocumentActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.ProjectProgressActivity
import com.zx.zxutils.util.ZXDialogUtil
import com.zx.zxutils.util.ZXToastUtil
import kotlinx.android.synthetic.main.fragment_approve_sub_process.*
import kotlinx.android.synthetic.main.fragment_approve_sub_process.btn_approve_submit
import kotlinx.android.synthetic.main.fragment_approve_sub_process.dataShow
import kotlinx.android.synthetic.main.fragment_approve_sub_process.materials
import kotlinx.android.synthetic.main.fragment_approve_sub_process.operationGuide
import kotlinx.android.synthetic.main.fragment_approve_sub_process.process_progress
import kotlinx.android.synthetic.main.fragment_approve_sub_process.safetyRegulations
import kotlinx.android.synthetic.main.fragment_approve_sub_process.sv_score
import kotlinx.android.synthetic.main.fragment_procedure_report.*

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
        sv_score.setRightString(if (parcelable?.score == null) "" else parcelable!!.score.toString())
        //设置adapter
        dataShow.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = reportListAdapter
//            addItemDecoration(SimpleDecoration(mContext))
        }
        mPresenter.getDeviceList(parcelable?.detailedProId.toString())
    }

    fun startAction(
        Context: Activity,
        detailedId: String = "",
        subProjectId: String = "",
        deviceListBean: DeviceListBean? = null,
        editable: Boolean
    ) {
        val intent = Intent(activity, DeviceAuditActivity::class.java)
        intent.putExtra("detailedId", detailedId)
        intent.putExtra("subProjectId", subProjectId)
        intent.putExtra("deviceListBean", deviceListBean)
        intent.putExtra("editable", editable)
        startActivityForResult(intent, 0x01)

    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        process_progress.setOnSuperTextViewClickListener {
            ProjectProgressActivity.startAction(
                activity as Activity,
                false,
                parcelable?.detailedProId.toString()
            )
        }
        reportListAdapter.setOnItemClickListener { adapter, view, position ->
            val deviceListBean = adapter.data[position] as DeviceListBean
            if (deviceListBean.auditStatus == "0") {
                showToast("当前设备不可审批")
                return@setOnItemClickListener
            }
            if (deviceListBean.auditStatus?.toInt()!! < 4) {
                startAction(activity!!, deviceListBean.detailedId.toString(), subProjectId, deviceListBean, true)
            } else {
                startAction(activity!!, deviceListBean.detailedId.toString(), subProjectId, deviceListBean, false)
            }


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
            var b = isAllfinish()
            if (b == 0) {
                ApproveScoreActivity.startAction(
                    activity as Activity, false,
                    assessmentId,
                    dto,
                    parcelable?.detailedProId.toString()
                )
            } else {
                ZXDialogUtil.showYesNoDialog(mContext, "提示", "您有设备未完成审批") { _, i ->
                    ZXDialogUtil.dismissDialog()
                }
            }


        }
        btn_audit_reject.setOnClickListener {
            val dto = initDto()
            val b = isAllfinish()
            if (b == 0) {
                mPresenter.postAuditProcess(dto)
            } else {
                ZXDialogUtil.showYesNoDialog(mContext, "提示", "您有设备未完成审批") { _, i ->
                    ZXDialogUtil.dismissDialog()
                }
            }
        }
        btn_audit_pass.setOnClickListener {
            val dto = initDto()
            val b = isAllfinish()
            if (b == 0) {
                mPresenter.postAuditProcess(dto)
            } else {
                ZXDialogUtil.showYesNoDialog(mContext, "提示", "您有设备未完成审批") { _, i ->
                    ZXDialogUtil.dismissDialog()
                }
            }

        }
    }

    /**
     * 获取未审批设备数量
     */
    private fun isAllfinish(): Int {
        var b = 0
        list.forEach {
            if (it.auditStatus?.toInt()!! < 4) {
                b++
            }
        }
        return b
    }

    private fun initDto(): PostAuditDto {
        val dto = PostAuditDto()
        dto.projectId = projectId
        dto.subProjectId = subProjectId
        dto.processDetailedId = parcelable?.processDetailedId.toString()
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
        reportListAdapter.setNewData(
            data
        )
        if (data != null) {
            list.clear()
            list = data
            var b = 0
            data.forEach {
                if (it.auditStatus == "9") {
                    b++
                }
            }

            if (parcelable?.auditFlag == 1) {
                bottom_ll.visibility = View.VISIBLE
                if (b != 0) {
                    btn_audit_reject.visibility = View.VISIBLE
                    btn_audit_pass.visibility = View.GONE
                    btn_approve_submit.visibility = View.GONE
                }
                if (b == 0 && parcelable?.scoreFlag == 1) {
                    btn_audit_reject.visibility = View.GONE
                    btn_audit_pass.visibility = View.GONE
                    btn_approve_submit.visibility = View.VISIBLE
                }
                if (b == 0 && parcelable?.scoreFlag == -1) {
                    btn_audit_reject.visibility = View.GONE
                    btn_audit_pass.visibility = View.VISIBLE
                    btn_approve_submit.visibility = View.GONE
                }
            } else {
                bottom_ll.visibility = View.GONE
            }

        }

    }

    override fun auditProcessResult(data: Any?) {
        activity?.finish()
        ZXToastUtil.showToast("提交成功")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x01 && resultCode == 0x01) {
            mPresenter.getDeviceList(parcelable?.detailedProId.toString())
        }
    }
}
