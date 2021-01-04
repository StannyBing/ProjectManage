package com.zx.projectmanage.module.projectapplication.construction.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zx.bui.ui.buidialog.BUIDialog
import com.zx.projectmanage.R
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.base.BottomSheetTool
import com.zx.projectmanage.base.SimpleDecoration
import com.zx.projectmanage.module.other.ui.CameraActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.BaiduGeocoderBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ConstructionDataBean
import com.zx.projectmanage.module.projectapplication.construction.bean.DataStepInfoBean
import com.zx.projectmanage.module.projectapplication.construction.bean.StepStandardBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ConstructionDataAdapter
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.StepStandardAdapter

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionDataContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ConstructionDataModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ConstructionDataPresenter
import com.zx.projectmanage.module.projectapplication.construction.func.listener.DataStepListener
import com.zx.zxutils.util.ZXDialogUtil
import com.zx.zxutils.util.ZXLocationUtil
import com.zx.zxutils.util.ZXSystemUtil
import com.zx.zxutils.util.ZXTimeUtil
import com.zx.zxutils.views.BottomSheet.ZXBottomSheet
import kotlinx.android.synthetic.main.activity_construction_data.*


/**
 * Create By admin On 2017/7/11
 * 功能：施工资料
 */
class ConstructionDataActivity : BaseActivity<ConstructionDataPresenter, ConstructionDataModel>(), ConstructionDataContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, detailedId : String, subProjectId : String) {
            val intent = Intent(activity, ConstructionDataActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    private val dataList = arrayListOf<ConstructionDataBean>()
    private val dataAdapter = ConstructionDataAdapter(dataList)

    private val stepStandardList = arrayListOf<StepStandardBean>()
    private val stepStandardAdapter = StepStandardAdapter(stepStandardList)

    private var selectStepBean: StepStandardBean? = null

    private var cameraPos = 0

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_construction_data
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        dataList.add(ConstructionDataBean(ConstructionDataBean.Edit_Type, "设备ID"))
        dataList.add(ConstructionDataBean(ConstructionDataBean.Edit_Type, "设备名称"))
        dataList.add(ConstructionDataBean(ConstructionDataBean.Select_Type, "规范模板", isDivider = true))
        dataList.add(ConstructionDataBean(ConstructionDataBean.Location_Type, "上报位置", isDivider = true))
        dataList.add(ConstructionDataBean(ConstructionDataBean.Text_Type, "驳回原因", isDivider = true))

        rv_construction_data.apply {
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
        stv_construction_data.setLeftImageViewClickListener {
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
        })
        //保存
        tv_construction_save.setOnClickListener {
            dataList.filter {
                it.type == ConstructionDataBean.Edit_Type
            }.forEach {
                if (it.stringValue.isEmpty()) {
                    showToast("请完善输入")
                    return@setOnClickListener
                }
            }
            if (dataList.first { it.type == ConstructionDataBean.Select_Type }.stringValue.isEmpty()) {
                showToast("请选择规范模板")
                return@setOnClickListener
            }
            if (dataList.first { it.type == ConstructionDataBean.Location_Type }.latitude == null) {
                showToast("请选择上报位置")
                return@setOnClickListener
            }
            dataList.filter { it.type == ConstructionDataBean.Step_Type }.forEach {
                if (it.stepInfos.size < 2) {
                    showToast("请上传步骤文件")
                    return@setOnClickListener
                }
            }
            mPresenter.saveDataInfo(dataList)
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
                dataList.first { it.type == ConstructionDataBean.Select_Type }.apply {
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
        dataList.first { it.type == ConstructionDataBean.Location_Type }.apply {
            longitude = location.longitude
            latitude = location.latitude
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
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStepDetailResult(stepDetail: StepStandardBean) {
        selectStepBean = stepDetail
        dataList.removeIf { it.type == ConstructionDataBean.Step_Type }
        stepDetail.standardSteps?.forEach {
            dataList.add(ConstructionDataBean(ConstructionDataBean.Step_Type, it.stepName, it.standard, stepInfos = arrayListOf<DataStepInfoBean>().apply {
                //                add(DataStepInfoBean(ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=" + it.standardId))
                add(DataStepInfoBean(thumbnail = ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=353732457519910912"))
            }))
        }
        dataAdapter.notifyDataSetChanged()
    }

    override fun onSaveResult() {
        showToast("保存成功")
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
