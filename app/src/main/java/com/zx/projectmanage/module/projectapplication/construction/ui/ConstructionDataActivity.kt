package com.zx.projectmanage.module.projectapplication.construction.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.base.SimpleDecoration
import com.zx.projectmanage.module.projectapplication.construction.bean.BaiduGeocoderBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ConstructionDataBean
import com.zx.projectmanage.module.projectapplication.construction.bean.DataStepInfoBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ConstructionDataAdapter

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionDataContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ConstructionDataModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ConstructionDataPresenter
import com.zx.projectmanage.module.projectapplication.construction.func.listener.DataStepListener
import com.zx.zxutils.util.ZXLocationUtil
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
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, ConstructionDataActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    private val dataList = arrayListOf<ConstructionDataBean>()
    private val dataAdapter = ConstructionDataAdapter(dataList)

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
        dataList.add(ConstructionDataBean(ConstructionDataBean.Select_Type, "上报规范", isDivider = true))
        dataList.add(ConstructionDataBean(ConstructionDataBean.Location_Type, "上报位置", isDivider = true))
        dataList.add(ConstructionDataBean(ConstructionDataBean.Text_Type, "驳回原因", isDivider = true))
        dataList.add(ConstructionDataBean(ConstructionDataBean.Step_Type, "****步骤", "请上传施工的最终效果及施工过程录像", stepInfos = arrayListOf<DataStepInfoBean>().apply {
            add(DataStepInfoBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
            add(DataStepInfoBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
            add(DataStepInfoBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
        }))
        dataList.add(ConstructionDataBean(ConstructionDataBean.Step_Type, "****步骤", "请上传施工的最终效果及施工过程录像", stepInfos = arrayListOf<DataStepInfoBean>().apply {
            add(DataStepInfoBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
            add(DataStepInfoBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
            add(DataStepInfoBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
            add(DataStepInfoBean("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa"))
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
                R.id.tv_data_location_get -> {
                    getPermission(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        val location = ZXLocationUtil.getLocation(this)
                        location?.let {
                            mPresenter.doGeocoder(it)
                        }
                    }
                }
                R.id.iv_data_step_camera -> {

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
     * 逆地址编码
     */
    override fun onGeocoderResult(location: Location, geocoderBean: BaiduGeocoderBean) {
        dataList.first { it.name == "上报位置" }.apply {
            longitude = location.longitude
            latitude = location.latitude
            stringValue = geocoderBean.result?.address ?: "未获取到位置"
            dataAdapter.notifyDataSetChanged()
        }

    }
}
