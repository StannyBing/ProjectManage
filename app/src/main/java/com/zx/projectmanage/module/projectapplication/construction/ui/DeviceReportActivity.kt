package com.zx.projectmanage.module.projectapplication.construction.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zx.projectmanage.R
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.app.toJson2
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.base.BottomSheetTool
import com.zx.projectmanage.base.SimpleDecoration
import com.zx.projectmanage.module.other.ui.CameraActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.*
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.DeviceInfoAdapter
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.StepStandardAdapter
import com.zx.projectmanage.module.projectapplication.construction.func.listener.DataStepListener
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.DeviceReportContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.DeviceReportModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.DeviceReportPresenter
import com.zx.zxutils.util.ZXDialogUtil
import com.zx.zxutils.util.ZXLocationUtil
import com.zx.zxutils.util.ZXLogUtil
import com.zx.zxutils.util.ZXSystemUtil
import kotlinx.android.synthetic.main.activity_device_report.*


/**
 * Create By admin On 2017/7/11
 * 功能：施工资料
 */
class DeviceReportActivity : BaseActivity<DeviceReportPresenter, DeviceReportModel>(), DeviceReportContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(
            activity: Activity,
            isFinish: Boolean,
            detailedId: String = "",
            subProjectId: String = "",
            deviceListBean: DeviceListBean? = null,
            type: Int
        ) {
            val intent = Intent(activity, DeviceReportActivity::class.java)
            intent.putExtra("detailedId", detailedId)
            intent.putExtra("subProjectId", subProjectId)
            intent.putExtra("deviceListBean", deviceListBean)
            activity.startActivityForResult(intent, 0x01)
            if (isFinish) activity.finish()
        }
    }

    private val dataList = arrayListOf<DeviceInfoBean>()
    private val dataAdapter = DeviceInfoAdapter(dataList)

    private val stepStandardList = arrayListOf<StepStandardBean>()
    private val stepStandardAdapter = StepStandardAdapter(stepStandardList)

    private var selectStepBean: StepStandardBean? = null//当前选中的步骤
    private lateinit var deviceBean: DeviceListBean//设备详情

    private var editable = true

    private var cameraPos = 0

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_device_report
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        editable = intent.getBooleanExtra("editable", true)
        if (!editable) {
            ll_devicereport_btn.visibility = View.GONE
        }
        dataAdapter.editable = editable

        deviceBean = if (intent.hasExtra("deviceListBean") && intent.getSerializableExtra("deviceListBean") != null) {
            intent.getSerializableExtra("deviceListBean") as DeviceListBean
        } else {
            DeviceListBean()
        }
        deviceBean.detailedId = intent.getStringExtra("detailedId")
        deviceBean.subProjectId = intent.getStringExtra("subProjectId")

        dataList.add(DeviceInfoBean(DeviceInfoBean.Edit_Type, "设备ID", stringValue = deviceBean.equipmentId ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Edit_Type, "设备名称", stringValue = deviceBean.equipmentName ?: ""))
        dataList.add(DeviceInfoBean(DeviceInfoBean.Select_Type, "规范模板", isDivider = true))
        dataList.add(
            DeviceInfoBean(
                DeviceInfoBean.Location_Type,
                "上报位置",
                isDivider = true,
                stringValue = deviceBean.postAddr ?: "",
                latitude = deviceBean.latitude ?: "",
                longitude = deviceBean.longitude ?: ""
            )
        )
//        dataList.add(DeviceInfoBean(DeviceInfoBean.Text_Type, "驳回原因", isDivider = true, stringValue = deviceBean?.remarks ?: ""))

        deviceBean?.standardId?.apply {
            mPresenter.getStepDetail(this)
        }

        rv_devicereport_data.apply {
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
        stv_devicereport_data.setLeftImageViewClickListener {
            super.onBackPressed()
        }
        //点击事件
        dataAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tv_data_location_get -> {//定位
                    getPermission(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        val location = ZXLocationUtil.getLocation(this)
                        location?.let {
                            mPresenter.doGeocoder(it)
                        }
                    }
                }
                R.id.iv_data_step_camera -> {//照片选择
                    if (dataList[position].stepInfos.size > 9) {
                        showToast("附件资料数量不能超过9个")
                        return@setOnItemChildClickListener
                    }
                    getPermission(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)) {
                        cameraPos = position
                        CameraActivity.startAction(this, false, requestCode = 0x001)
                    }
                }
                R.id.tv_data_select_value -> {//工序选择
                    showStepStandardView()
                }
            }
        }
        //删除附件
        dataAdapter.setDataStepListener(object : DataStepListener {
            override fun onFileDelete(stepPos: Int, filePos: Int) {
                dataList[stepPos].stepInfos.removeAt(filePos)
            }

            override fun onEditChange(pos: Int, info: String) {
                dataList[pos].stringValue = info
            }
        })
        //删除
        btn_devicereport_delete.setOnClickListener {
            ZXDialogUtil.showYesNoDialog(mContext, "提示", "是否删除该设备？") { dialog, which ->
                mPresenter.deleteDevice(deviceBean?.id ?: "")
            }
        }
        //保存
        btn_devicereport_save.setOnClickListener {
            ZXDialogUtil.showYesNoDialog(mContext, "提示", "是否保存设备信息？") { dialog, which ->
                dataList.filter {
                    it.type == DeviceInfoBean.Edit_Type
                }.forEach {
                    if (it.stringValue.isEmpty()) {
                        showToast("请完善输入")
                        return@showYesNoDialog
                    }
                }
                if (dataList.first { it.type == DeviceInfoBean.Select_Type }.stringValue.isEmpty()) {
                    showToast("请选择规范模板")
                    return@showYesNoDialog
                }
                if (dataList.first { it.type == DeviceInfoBean.Location_Type }.latitude == null) {
                    showToast("请选择上报位置")
                    return@showYesNoDialog
                }
                dataList.filter { it.type == DeviceInfoBean.Step_Type }.forEach {
                    if (it.stepInfos.size < 2) {
                        showToast("请上传步骤文件")
                        return@showYesNoDialog
                    }
                }
                mPresenter.saveDataInfo(dataList, deviceBean)
            }
        }
    }

    /**
     * 选择施工工序
     */
    private fun showStepStandardView() {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_step_standard, null, false)
        val etSearch = view.findViewById<EditText>(R.id.et_standard_search)
        val btnSearch = view.findViewById<Button>(R.id.btn_standard_search)
        val rvStandard = view.findViewById<RecyclerView>(R.id.rv_standard_list)
        rvStandard.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = stepStandardAdapter
            addItemDecoration(SimpleDecoration(mContext))
        }
        stepStandardAdapter.setOnItemClickListener { adapter, view, position ->
            stepStandardList.forEachIndexed { index, stepStandardBean ->
                stepStandardBean.isSelect = index == position
            }
            stepStandardAdapter.notifyDataSetChanged()
        }
        etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ZXSystemUtil.closeKeybord(this)
                btnSearch.performClick()
            }
            return@setOnEditorActionListener true
        }
        btnSearch.setOnClickListener {
            mPresenter.getStepStandard(
                hashMapOf(
                    "name" to etSearch.text.toString(),
                    "pageNo" to "1",
                    "pageSize" to "999"
                )
            )
        }
        if (stepStandardList.isEmpty()) {
            mPresenter.getStepStandard(
                hashMapOf(
                    "name" to "",
                    "pageNo" to "1",
                    "pageSize" to "999"
                )
            )
        }
        val bottomSheet = BottomSheetTool.showBottomSheet(mContext, "请选择规范模板", view, {
            stepStandardList.filter { it.isSelect }.run {
                ifEmpty {
                    return@run null
                }
                dataList.first { it.type == DeviceInfoBean.Select_Type }.apply {
                    stringValue = first().name
                    standardBean = first()
                }

                mPresenter.getStepDetail(first().id)
            }
            it.dismiss()
        })

//        val dialog = ZXDialogUtil.showListDialog(mContext, "请选择规范模板", "取消", arrayListOf<String>().apply {
//            stepStandardList.forEach {
//                add(it.name)
//            }
//        }) { dialog, which ->
//
//        }
//        dialog.window?.attributes = dialog.window?.attributes?.apply {
//            height = ZXSystemUtil.dp2px(350f)
//        }
    }

    /**
     * 逆地址编码
     */
    override fun onGeocoderResult(location: Location, geocoderBean: BaiduGeocoderBean) {
        dataList.first { it.type == DeviceInfoBean.Location_Type }.apply {
            longitude = location.longitude.toString()
            latitude = location.latitude.toString()
            stringValue = geocoderBean.result?.address ?: "未获取到位置"
            dataAdapter.notifyDataSetChanged()
        }
    }

    /**
     * 规范模板
     */
    override fun onStepStandardResult(stepStandardList: List<StepStandardBean>) {
        this.stepStandardList.clear()
        this.stepStandardList.addAll(stepStandardList)
        stepStandardAdapter.notifyDataSetChanged()
    }

    /**
     * 模板详情
     */
    override fun onStepDetailResult(stepDetail: StepStandardBean) {
        selectStepBean = stepDetail
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
        dataList.first { it.name == "规范模板" }.standardBean = stepDetail
        stepDetail.standardSteps?.forEach {
            dataList.add(
                DeviceInfoBean(
                    DeviceInfoBean.Step_Type,
                    it.stepName,
                    it.standard,
                    stepInfos = arrayListOf<DataStepInfoBean>().apply {
                        add(DataStepInfoBean(ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=" + it.standardId))
                        if (it.standardId == deviceBean.standardId) {
                            deviceBean?.postDetails?.forEach { post ->
                                if (it.stepId == post?.stepId) {
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
                        }
//                        add(
//                            DataStepInfoBean(
//                                path = ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=353732457519910912",
//                                thumbnail = ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=353732457519910912"
//                            )
//                        )
                    },
                    standardBean = stepDetail,
                    stepInfoBean = it
                )
            )
        }
        dataAdapter.notifyDataSetChanged()
    }

    override fun onDeviceDeleteResult() {
        showToast("删除成功")
        setResult(0x01)
        finish()
    }

    override fun onSaveResult() {
        showToast("保存成功")
        setResult(0x01)
        finish()
    }

    /**
     * 照片回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x001 && data != null) {
            val fileBean = DataStepInfoBean(
                if (!data.getStringExtra("vedioPath").isNullOrEmpty()) {
                    data.getStringExtra("vedioPath") ?: ""
                } else {
                    data.getStringExtra("path") ?: ""
                },
                data.getStringExtra("path") ?: "",
                if (data.getStringExtra("vedioPath").isNullOrEmpty()) {
                    DataStepInfoBean.Type.PICTURE
                } else {
                    DataStepInfoBean.Type.VIDEO
                }
            )
            dataList[cameraPos].stepInfos.add(fileBean)
            dataAdapter.notifyDataSetChanged()
        }
    }
}
