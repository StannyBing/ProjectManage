package com.zx.projectmanage.module.projectapplication.construction.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.base.BottomSheetTool
import com.zx.projectmanage.base.SimpleDecoration
import com.zx.projectmanage.module.other.ui.CameraActivity
import com.zx.projectmanage.module.projectapplication.approve.bean.BaiduGeocoderBean
import com.zx.projectmanage.module.projectapplication.approve.bean.ConstructionDataBean
import com.zx.projectmanage.module.projectapplication.approve.bean.DataStepInfoBean
import com.zx.projectmanage.module.projectapplication.approve.bean.StepStandardBean
import com.zx.projectmanage.module.projectapplication.approve.func.adapter.ConstructionDataAdapter
import com.zx.projectmanage.module.projectapplication.approve.func.adapter.StepStandardAdapter

import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ConstructionDataContract
import com.zx.projectmanage.module.projectapplication.approve.mvp.model.ConstructionDataModel
import com.zx.projectmanage.module.projectapplication.approve.mvp.presenter.ConstructionDataPresenter
import com.zx.projectmanage.module.projectapplication.approve.func.listener.DataStepListener
import com.zx.zxutils.util.ZXLocationUtil
import com.zx.zxutils.util.ZXSystemUtil
import kotlinx.android.synthetic.main.activity_construction_data.*


/**
 * Create By admin On 2017/7/11
 * 功能：施工资料
 */
class ApproveDataActivity : BaseActivity<ConstructionDataPresenter, ConstructionDataModel>(), ConstructionDataContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, ApproveDataActivity::class.java)
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
        dataList.add(ConstructionDataBean(ConstructionDataBean.Step_Type, "****步骤", "请上传施工的最终效果及施工过程录像", stepInfos = arrayListOf<DataStepInfoBean>().apply {
            add(DataStepInfoBean(thumbnail = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
            add(DataStepInfoBean(thumbnail = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
            add(DataStepInfoBean(thumbnail = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
        }))
        dataList.add(ConstructionDataBean(ConstructionDataBean.Step_Type, "****步骤", "请上传施工的最终效果及施工过程录像", stepInfos = arrayListOf<DataStepInfoBean>().apply {
            add(DataStepInfoBean(thumbnail = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
            add(DataStepInfoBean(thumbnail = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
            add(DataStepInfoBean(thumbnail = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
            add(DataStepInfoBean(thumbnail = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
        }))

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
        dataAdapter.setDataStepListener(object : DataStepListener {
            override fun onFileDelete(stepPos: Int, filePos: Int) {
                dataList[stepPos].stepInfos.removeAt(filePos)
            }
        })
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
    override fun onStepDetailResult(stepDetail: StepStandardBean) {
        selectStepBean = stepDetail
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
