package com.zx.projectmanage.module.projectapplication.patrol

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zx.projectmanage.R
import com.zx.projectmanage.module.other.mvp.contract.BaseWebContract
import com.zx.projectmanage.module.other.mvp.model.BaseWebModel
import com.zx.projectmanage.module.other.mvp.presenter.BaseWebPresenter
import com.zx.projectmanage.module.other.ui.BaseWebActivity

class PatrolActivity : BaseWebActivity<BaseWebPresenter, BaseWebModel>(), BaseWebContract {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, PatrolActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }
//    override fun getLayoutId(): Int {
//        return R.layout.activity_patrol
//    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
//        loadUrl("file:///android_asset/patrolInspection/patrol.html")
        loadUrl("http://192.168.1.44:5500/ProjectManage-H5/patrolInspection/patrol.html")
    }
}