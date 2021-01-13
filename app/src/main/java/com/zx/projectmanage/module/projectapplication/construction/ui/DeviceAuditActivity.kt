package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.app.toJson2
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.base.SimpleDecoration
import com.zx.projectmanage.module.projectapplication.construction.bean.DataStepInfoBean
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceInfoBean
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.bean.StepStandardBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.DeviceInfoAdapter

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.DeviceAuditContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.DeviceAuditModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.DeviceAuditPresenter
import com.zx.zxutils.util.ZXDialogUtil
import kotlinx.android.synthetic.main.activity_device_audit.*


/**
 * Create By admin On 2017/7/11
 * 功能：设备审核
 */
class DeviceAuditActivity : BaseActivity<DeviceAuditPresenter, DeviceAuditModel>(), DeviceAuditContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(
            activity: Activity,
            isFinish: Boolean,
            deviceListBean: DeviceListBean? = null,
            type: Int
        ) {
            val intent = Intent(activity, DeviceAuditActivity::class.java)
            intent.putExtra("deviceListBean", deviceListBean)
            activity.startActivityForResult(intent, 0x01)
            if (isFinish) activity.finish()
        }
    }

    private val dataList = arrayListOf<DeviceInfoBean>()
    private val dataAdapter = DeviceInfoAdapter(dataList)

    private var editable = true

    private var deviceBean: DeviceListBean? = null//设备详情

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_device_audit
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        editable = intent.getBooleanExtra("editable", true)
        if (!editable) {
            ll_deviceaudit_btn.visibility = View.GONE
        }
        dataAdapter.isEditable = false

        deviceBean = intent.getSerializableExtra("deviceListBean") as DeviceListBean

        mPresenter.getDeviceDetail(deviceBean?.standardProId ?: "", deviceBean?.standardId ?: "")

        rv_deviceaudit_data.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = dataAdapter
            addItemDecoration(SimpleDecoration(mContext, heightList = arrayListOf<Int>().apply {
                dataList.forEach {
                    this.add(if (it.isDivider) 10 else 1)
                }
            }))
        }
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        //返回按钮
        stv_deviceaudit_data.setLeftImageViewClickListener {
            super.onBackPressed()
        }
        //驳回
        btn_deviceaudit_reject.setOnClickListener {
            val view = LayoutInflater.from(mContext).inflate(R.layout.layout_device_audit, null, false)
            val etFeedBack = view.findViewById<EditText>(R.id.et_device_audit_feedback)
            ZXDialogUtil.showCustomViewDialog(mContext, "审批意见", view) { dialog: DialogInterface?, which: Int ->
                mPresenter.doReject(
                    hashMapOf(
                        "auditReason" to etFeedBack.text.toString(),

                        "detailedProId" to deviceBean?.detailedProId,
                        "standardProId" to deviceBean?.standardProId,
                        "submitNumber" to deviceBean?.submitNumber
                    ).toJson2()
                )
            }
        }
        //通过
        btn_deviceaudit_pass.setOnClickListener {
            ZXDialogUtil.showYesNoDialog(mContext, "提示", "是否通过该设备？") { dialog, which ->
                val hashMapOf = hashMapOf(
                    "auditReason" to "",
                    "detailedProId" to deviceBean?.detailedProId,
                    "standardProId" to deviceBean?.standardProId,
                    "submitNumber" to deviceBean?.submitNumber
                )
                val body = hashMapOf.toJson2()
                mPresenter.doPass(
                    body
                )
            }
        }
    }

    override fun onRejectResult() {
        showToast("驳回成功")
        setResult(0x01)
        finish()
    }

    override fun onPassResult() {
        showToast("通过成功")
        setResult(0x01)
        finish()
    }

    override fun onDeviceDetailResult(deviceListBean: DeviceListBean) {
        val standardProId = deviceBean?.standardProId
        val standardId = deviceBean?.standardId
        this.deviceBean = deviceListBean
        deviceBean?.standardId = standardId
        deviceBean?.standardProId = standardProId
        dataList.clear()
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "测点名称", stringValue = deviceBean?.gaugingPoint ?: ""))
        if (intent.getStringExtra("processType") == "1") {
            dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "设备ID", stringValue = deviceBean?.equipmentId ?: ""))
            dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "设备名称", stringValue = deviceBean?.equipmentName ?: ""))
        }
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "规范模板", isDivider = true))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "施工负责人", stringValue = deviceBean?.equipmentId ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "上报人员", stringValue = deviceBean?.equipmentName ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "上报位置", stringValue = deviceBean?.postAddr ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "上报时间", isDivider = true, stringValue = deviceBean?.equipmentName ?: ""))
//        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "驳回原因", isDivider = true, stringValue = deviceBean?.remarks ?: ""))

        mPresenter.getStepDetail(standardId ?: "")

        dataAdapter.notifyDataSetChanged()
    }

    /**
     * 模板详情
     */
    override fun onStepDetailResult(stepDetail: StepStandardBean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dataList.removeIf { it.type == DeviceInfoBean.Step_Type }
        } else {
            var i = 0
            while (i < dataList.size) {
                // 判断集合中元素是否和bbb相等.
                if (dataList[i].type == DeviceInfoBean.Step_Type) {
                    dataList.removeAt(i)
                    i-- // 重点 - 一定要注意写!
                }
                i++
            }
        }
        dataList.first { it.name == "规范模板" }.stringValue = stepDetail.name
        deviceBean?.attachementVos?.forEach {
            dataList.add(
                DeviceInfoBean(DeviceInfoBean.Step_Type,
                    it.stepName ?: "",
                    it.standard ?: "",
                    stepInfos = arrayListOf<DataStepInfoBean>().apply {
                        add(DataStepInfoBean(ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=" + it.attachmentId))
                        it.buildPostDetails?.forEach { post ->
                            if (post != null) {
                                add(
                                    DataStepInfoBean(
                                        ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=" + post.attachment,
                                        type = if (post.fileType == 0) {
                                            DataStepInfoBean.Type.PICTURE
                                        } else {
                                            DataStepInfoBean.Type.VIDEO
                                        }
                                    )
                                )
                            }
                        }
                    })
            )
        }
//        stepDetail.standardSteps?.forEach {
//            dataList.add(
//                DeviceInfoBean(
//                    DeviceInfoBean.Step_Type,
//                    it.stepName,
//                    it.standard,
//                    stepInfos = arrayListOf<DataStepInfoBean>().apply {
//                        add(DataStepInfoBean(ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=" + it.attachmentId))
//                        if (it.standardId == deviceBean?.standardId) {
//                            deviceBean?.attachementVos?.forEach { post ->
//                                if (it.stepId == post.stepId) {
//                                    add(
//                                        DataStepInfoBean(
//                                            ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=" + post.attachment,
//                                            type = if (post.fileType == 0) {
//                                                DataStepInfoBean.Type.PICTURE
//                                            } else {
//                                                DataStepInfoBean.Type.VIDEO
//                                            }
//                                        )
//                                    )
//                                }
//                            }
//                        }
////                        add(
////                            DataStepInfoBean(
////                                path = ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=353732457519910912",
////                                thumbnail = ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=353732457519910912"
////                            )
////                        )
//                    },
//                    standardBean = stepDetail,
//                    stepInfoBean = it
//                )
//            )
//        }
        dataAdapter.notifyDataSetChanged()
    }


}
