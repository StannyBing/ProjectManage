package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.app.toJson
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectProcessInfoBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ProcedureListAdapter
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProcedureReportContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ProcedureReportModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ProcedureReportPresenter
import com.zx.zxutils.util.ZXDialogUtil
import com.zx.zxutils.util.ZXToastUtil
import kotlinx.android.synthetic.main.fragment_procedure_report.*

/**
 * Create By admin On 2017/7/11
 * 功能：工序fragment
 */
class ProcedureReportFragment : BaseFragment<ProcedureReportPresenter, ProcedureReportModel>(), ProcedureReportContract.View {
    private var list: MutableList<DeviceListBean> = arrayListOf<DeviceListBean>()
    private val reportListAdapter = ProcedureListAdapter(list)
    var subProjectId = ""
    var projectId = ""

    //工序模版id
    var assessmentId = ""
    var type = 0
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
        projectId = arguments?.getString("projectId").toString()
        type = arguments?.getInt("type", 0)!!

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
            ConstructionDataActivity.startAction(requireActivity(), false, parcelable?.id.toString(), subProjectId)
        }
        reportListAdapter.setOnItemChildClickListener { adapter, view, position ->
            val deviceListBean = adapter.data as DeviceListBean
            ConstructionDataActivity.startAction(requireActivity(), false, parcelable?.id.toString(), subProjectId, deviceListBean)
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
            var b = false
            for (deviceListBean in list) {

            }
            if (b) {
                //自动登录
                mPresenter.postSubmit(
                    hashMapOf(
                        "detailedProId" to list[0].detailedProId,
                        "projectId" to projectId,
                        "subProecssId" to parcelable?.id.toString()
                    ).toJson()
                )
            } else {
                ZXDialogUtil.showYesNoDialog(mContext, "提示", "您有设备未完成资料上传") { _, i ->
                    ZXDialogUtil.dismissDialog()
                }
            }
        }
    }


    override fun getDeviceListResult(data: MutableList<DeviceListBean>?) {
        if (data != null) {
            list = data
        }
        reportListAdapter.setNewData(
            data
        )
    }

    override fun postSubmitResult(data: Any?) {

    }

}
