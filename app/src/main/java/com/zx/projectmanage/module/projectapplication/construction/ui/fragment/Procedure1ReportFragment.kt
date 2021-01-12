package com.zx.projectmanage.module.projectapplication.construction.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.app.toJson2
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectProcessInfoBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ProcedureListAdapter
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProcedureOneReportContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ProcedureOneReportModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ProcedureOneReportPresenter
import com.zx.projectmanage.module.projectapplication.construction.ui.DeviceReportActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.DocumentActivity
import com.zx.projectmanage.module.projectapplication.construction.ui.ProjectProgressActivity
import com.zx.zxutils.util.ZXDialogUtil
import com.zx.zxutils.util.ZXSharedPrefUtil
import com.zx.zxutils.util.ZXToastUtil
import kotlinx.android.synthetic.main.fragment_procedure_report.*

/**
 * Create By admin On 2017/7/11
 * 功能：工序fragment
 */
class Procedure1ReportFragment : BaseFragment<ProcedureOneReportPresenter, ProcedureOneReportModel>(), ProcedureOneReportContract.View {
    var list: MutableList<DeviceListBean> = arrayListOf<DeviceListBean>()
    var allList: MutableList<DeviceListBean> = arrayListOf<DeviceListBean>()
    private val reportListAdapter = ProcedureListAdapter(list)
    var subProjectId = ""
    var projectId = ""
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
        fun newInstance(bundle: Bundle? = null): Procedure1ReportFragment {
            val fragment = Procedure1ReportFragment()
            fragment.arguments = bundle
            return fragment
        }

    }

    fun startAction(
        Context: Activity,
        detailedId: String = "",
        subProjectId: String = "",
        deviceListBean: DeviceListBean? = null,
        editable: Boolean? = null,
        processType: String? = null
    ) {
        val intent = Intent(activity, DeviceReportActivity::class.java)
        intent.putExtra("detailedId", detailedId)
        intent.putExtra("subProjectId", subProjectId)
        intent.putExtra("deviceListBean", deviceListBean)
        intent.putExtra("editable", editable)
        intent.putExtra("processType", processType)
        startActivityForResult(intent, 0x01)

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
        sv_score.setRightString(if (parcelable?.score == null) "" else parcelable!!.score.toString())
        //设置adapter
        dataShow.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = reportListAdapter
//            addItemDecoration(SimpleDecoration(mContext))
        }
        mPresenter.getDeviceList(
            hashMapOf(
                "detailId" to parcelable?.id.toString(),
                "subProjectId" to subProjectId
            )
        )
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        tv_report_addEquip.setOnClickListener {
            startAction(activity!!, parcelable?.id.toString(), subProjectId, null, true)
        }
        process_progress.setOnSuperTextViewClickListener {
            if (list.size > 0) {
                ProjectProgressActivity.startAction(activity as Activity, false, list[0].detailedProId)
            }
        }
        reportListAdapter.setOnItemClickListener { adapter, view, position ->
            val deviceListBean = adapter.data[position] as DeviceListBean
            if ((deviceListBean.status == "9" && parcelable?.auditStatus == "0") || deviceListBean.status == "-1" || deviceListBean.status == "-2") {
                startAction(activity!!, parcelable?.id.toString(), subProjectId, deviceListBean, true)
            } else {
                startAction(activity!!, parcelable?.id.toString(), subProjectId, deviceListBean, false)
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
            var b = 0
            for (deviceListBean in list) {
                val toInt = deviceListBean.status?.toInt()
                if (toInt == -1 || toInt == 9) {
                    b++
                }
            }
            if (b == list.size) {
                //自动登录
                mPresenter.postSubmit(
                    hashMapOf(
                        "detailedProId" to list[0].detailedProId,
                        "projectId" to projectId,
                        "subProecssId" to parcelable?.id.toString()
                    ).toJson2()
                )
            } else {
                ZXDialogUtil.showYesNoDialog(mContext, "提示", "您有设备未完成资料上传") { _, i ->
                    ZXDialogUtil.dismissDialog()
                }
            }

        }
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
            if (parcelable?.auditStatus == "0" || data.size == 0) {
                tv_report_addEquip.visibility = View.VISIBLE
            }
            if ((parcelable?.auditStatus == "0" && data.size > 0) || b > 0) {
                btn_approve_submit.visibility = View.VISIBLE
            }
            if (parcelable?.sort == 0) {
                allList = data
                ZXSharedPrefUtil().putString("process1ID", parcelable?.id.toString())
                ZXSharedPrefUtil().putList(parcelable?.id.toString(), allList)
            }
        }

    }

    override fun postSubmitResult(data: Any?) {
        ZXToastUtil.showToast("提交成功")
        activity!!.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x01 && resultCode == 0x01) {
            mPresenter.getDeviceList(
                hashMapOf(
                    "detailId" to parcelable?.id.toString(),
                    "subProjectId" to subProjectId
                )
            )
        }
    }

}
