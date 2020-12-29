package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionDataContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ConstructionDataModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ConstructionDataPresenter


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
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

}
