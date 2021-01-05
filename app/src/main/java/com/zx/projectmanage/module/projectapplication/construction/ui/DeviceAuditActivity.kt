package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.app.toJson2
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.base.SimpleDecoration
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceInfoBean
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceListBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.DeviceInfoAdapter

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.DeviceAuditContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.DeviceAuditModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.DeviceAuditPresenter
import com.zx.zxutils.util.ZXDialogUtil
import kotlinx.android.synthetic.main.activity_device_audit.*
import kotlinx.android.synthetic.main.activity_device_report.*


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
        deviceBean = intent.getSerializableExtra("deviceListBean") as DeviceListBean

        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "设备ID", stringValue = deviceBean?.equipmentId ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "设备名称", stringValue = deviceBean?.equipmentName ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "规范模板", isDivider = true))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "施工负责人", stringValue = deviceBean?.equipmentId ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "上报人员", stringValue = deviceBean?.equipmentName ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "上报位置", stringValue = deviceBean?.postAddr ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "上报时间", isDivider = true, stringValue = deviceBean?.equipmentName ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "驳回原因", isDivider = true, stringValue = deviceBean?.remarks ?: ""))

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
            ZXDialogUtil.showCustomViewDialog(mContext, "审批意见", view) { dialog: DialogInterface?, which: Int ->
                mPresenter.doReject(
                    hashMapOf(
                        "auditReason" to "",
                        "detailedProId" to "",
                        "standardProId" to "",
                        "submitNumber" to ""
                    ).toJson2()
                )
            }
        }
        //通过
        btn_deviceaudit_pass.setOnClickListener {
            ZXDialogUtil.showYesNoDialog(mContext, "提示", "是否通过该设备？") { dialog, which ->
                mPresenter.doPass(
                    hashMapOf(
                        "auditReason" to "",
                        "detailedProId" to "",
                        "standardProId" to "",
                        "submitNumber" to ""
                    ).toJson2()
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

}