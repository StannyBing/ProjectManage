package com.zx.projectmanage.module.projectapplication.approve.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.approve.bean.ScoreTemplateBean

import com.zx.projectmanage.module.projectapplication.approve.mvp.contract.ApproveScoreContract
import com.zx.projectmanage.module.projectapplication.approve.mvp.model.ApproveScoreModel
import com.zx.projectmanage.module.projectapplication.approve.mvp.presenter.ApproveScorePresenter


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveScoreActivity : BaseActivity<ApproveScorePresenter, ApproveScoreModel>(), ApproveScoreContract.View {

    var assessmentId = ""

    companion object {
        /**
         * 启动器
         */
        fun startAction(
            activity: Activity, isFinish: Boolean, assessmentId: String
        ) {
            val intent = Intent(activity, ApproveScoreActivity::class.java)
            intent.putExtra("assessmentId", assessmentId)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_approve_score
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mPresenter.getScoreTemple(assessmentId)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

    override fun onScoreTempleResult(data: ScoreTemplateBean?) {

    }

}
