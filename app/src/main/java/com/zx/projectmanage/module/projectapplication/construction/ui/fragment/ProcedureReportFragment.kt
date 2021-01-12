package com.zx.projectmanage.module.projectapplication.construction.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zx.projectmanage.R
import com.zx.projectmanage.app.toJson2
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.base.BottomSheetTool
import com.zx.projectmanage.base.SimpleDecoration
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ProjectProcessInfoBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ProcedureListAdapter
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.Process1DeviceAdapter
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ProcedureReportContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ProcedureReportModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ProcedureReportPresenter
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
class ProcedureReportFragment : BaseFragment<ProcedureReportPresenter, ProcedureReportModel>(), ProcedureReportContract.View {
    var list: MutableList<DeviceListBean> = arrayListOf<DeviceListBean>()
    var fragment1list: MutableList<DeviceListBean> = arrayListOf<DeviceListBean>()
    private val reportListAdapter = ProcedureListAdapter(list)
    private val stepStandardAdapter = Process1DeviceAdapter(fragment1list)
    var subProjectId = ""
    var projectId = ""
    var type = 0
    var parcelable: ProjectProcessInfoBean.DetailedListBean? = null
    var process1ID = ""

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
        sv_score.setRightString(if (parcelable?.score == null) "" else parcelable!!.score.toString())
        //设置adapter
        dataShow.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = reportListAdapter
//            addItemDecoration(SimpleDecoration(mContext))
        }
        getDeviceList()


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
     * View事件设置
     */
    override fun onViewListener() {
        tv_report_addEquip.setOnClickListener {
            startAction(activity!!, parcelable?.id.toString(), subProjectId, null, true, parcelable?.processType)
        }
        process_progress.setOnSuperTextViewClickListener {
            if (list.isNotEmpty()) {
                ProjectProgressActivity.startAction(activity as Activity, false, list[0].detailedProId)
            }
        }
        reportListAdapter.setOnItemClickListener { adapter, view, position ->
            val deviceListBean = adapter.data[position] as DeviceListBean
            if ((deviceListBean.status == "9" && parcelable?.auditStatus == "0") || deviceListBean.status == "-1" || deviceListBean.status == "-2") {
                startAction(activity!!, parcelable?.id.toString(), subProjectId, deviceListBean, true, parcelable?.processType)
            } else {
                startAction(activity!!, parcelable?.id.toString(), subProjectId, deviceListBean, false, parcelable?.processType)
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && list.size == 0 && fragment1list.size > 0) {
            showChooeseDeviceView()
        }
    }

    /**
     * 获取项目设备成功
     */
    override fun getDeviceListResult(data: MutableList<DeviceListBean>?) {
        reportListAdapter.setNewData(
            data
        )
        if (data != null) {
            if (data.size > 0) {
                list.clear()
                list = data
                var b = 0
                data.forEach {
                    if (it.auditStatus == "9") {
                        b++
                    }
                }

                if ((parcelable?.auditStatus == "0" && data.size > 0) || b > 0) {
                    btn_approve_submit.visibility = View.VISIBLE
                }
            } else {
                process1ID = ZXSharedPrefUtil().getString("process1ID")
                fragment1list = ZXSharedPrefUtil().getList(process1ID)
            }
        }


    }

    override fun postSubmitResult(data: Any?) {
        activity?.finish()
        ZXToastUtil.showToast("提交成功")
    }

    override fun selectEquipmentResult(data: Any?) {
        getDeviceList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x01 && resultCode == 0x01) {
            getDeviceList()
        }
    }

    private fun getDeviceList() {
        mPresenter.getDeviceList(
            hashMapOf(
                "detailId" to parcelable?.id.toString(),
                "subProjectId" to subProjectId
            )
        )
    }

    /**
     * 选择当前工序项目
     */
    private fun showChooeseDeviceView() {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_step_standard, null, false)
        val etSearch = view.findViewById<EditText>(R.id.et_standard_search)
        val btnSearch = view.findViewById<Button>(R.id.btn_standard_search)
        btnSearch.visibility = View.GONE
        etSearch.visibility = View.GONE
        val rvStandard = view.findViewById<RecyclerView>(R.id.rv_standard_list)
        rvStandard.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = stepStandardAdapter
            addItemDecoration(SimpleDecoration(mContext))
        }
        stepStandardAdapter.setNewData(fragment1list)
        stepStandardAdapter.setOnItemClickListener { adapter, view, position ->
            val any = adapter.data[position] as DeviceListBean
            fragment1list[position].isSelect = !any.isSelect
            stepStandardAdapter.notifyDataSetChanged()
        }
        val bottomSheet = BottomSheetTool.showBottomSheet(mContext, "请选择设备", view, {
            val data = stepStandardAdapter.data as List<DeviceListBean>
            val arrayIds = StringBuffer()
            for ((index, datum) in data.withIndex()) {
                if (datum.isSelect) {
                    if (index == data.size - 1) {
                        arrayIds.append(datum.id)
                    } else {
                        arrayIds.append(datum.id)
                        arrayIds.append(",")
                    }
                }
            }
            if (arrayIds.isNotEmpty()) {
                mPresenter.selectEquipment(
                    hashMapOf(
                        "currentDetailedProId" to parcelable?.processDetailedProId.toString(),
                        "firstDetailedProId" to process1ID,
                        "firstProcessStandardProIds" to arrayIds.toString()
                    )
                )
                it.dismiss()
            } else {
                ZXToastUtil.showToast("必须选择至少一个设备")
            }

        })
    }
}
