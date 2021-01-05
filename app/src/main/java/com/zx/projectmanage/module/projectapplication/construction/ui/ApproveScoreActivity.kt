package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.ScoreTemplateBean
import com.zx.projectmanage.module.projectapplication.construction.dto.PostAuditDto

import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ApproveScoreContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ApproveScoreModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ApproveScorePresenter
import kotlinx.android.synthetic.main.activity_approve_score.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveScoreActivity : BaseActivity<ApproveScorePresenter, ApproveScoreModel>(), ApproveScoreContract.View {

    var assessmentId = ""
    lateinit var dto: PostAuditDto

    companion object {
        /**
         * 启动器
         */
        fun startAction(
            activity: Activity, isFinish: Boolean, assessmentId: String, dto: PostAuditDto
        ) {
            val intent = Intent(activity, ApproveScoreActivity::class.java)
            intent.putExtra("assessmentId", assessmentId)
            intent.putExtra("dto", dto)
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
        assessmentId = intent.getStringExtra("assessmentId").toString()
        dto = intent.getSerializableExtra("dto") as PostAuditDto
        mPresenter.getScoreTemple(assessmentId)
        super.initView(savedInstanceState)

    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        head.setLeftImageViewClickListener {
            finish()
        }
    }

    override fun onScoreTempleResult(data: ScoreTemplateBean?) {

    }

}
