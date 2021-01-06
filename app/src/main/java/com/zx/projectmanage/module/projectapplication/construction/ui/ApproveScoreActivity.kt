package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.ScoreTemplateBean
import com.zx.projectmanage.module.projectapplication.construction.dto.PostAuditDto
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ApproveScoreAdapter
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ApproveScoreContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ApproveScoreModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ApproveScorePresenter
import kotlinx.android.synthetic.main.activity_approve_score.*
import kotlinx.android.synthetic.main.activity_approve_score.head


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ApproveScoreActivity : BaseActivity<ApproveScorePresenter, ApproveScoreModel>(), ApproveScoreContract.View {
    private var list: MutableList<ScoreTemplateBean> = arrayListOf<ScoreTemplateBean>()
    private val reportListAdapter = ApproveScoreAdapter(list)
    var assessmentId = ""
    var detailedProId = ""
    lateinit var dto: PostAuditDto

    companion object {
        /**
         * 启动器
         */
        fun startAction(
            activity: Activity,
            isFinish: Boolean,
            assessmentId: String,
            dto: PostAuditDto,
            detailedProId: String
        ) {
            val intent = Intent(activity, ApproveScoreActivity::class.java)
            intent.putExtra("assessmentId", assessmentId)
            intent.putExtra("detailedProId", detailedProId)
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
        detailedProId = intent.getStringExtra("detailedProId").toString()
        dto = intent.getSerializableExtra("dto") as PostAuditDto

        super.initView(savedInstanceState)
        swipeRecyler.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = reportListAdapter
//            addItemDecoration(SimpleDecoration(mContext))
            reportListAdapter.setEmptyView(R.layout.item_empty, swipeRecyler)
        }

        mPresenter.getScoreTemple(assessmentId)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        head.setLeftImageViewClickListener {
            finish()
        }
        btn_score_save.setOnClickListener {
            val data = reportListAdapter.data
            var listVos: MutableList<PostAuditDto.ProjectProcessScoresBean> = ArrayList()
            data.forEach {
                val projectProcessScoresBean = PostAuditDto.ProjectProcessScoresBean()
                projectProcessScoresBean.subAssessmentId = it.subAssessmentId
                projectProcessScoresBean.detailedProId = detailedProId
                projectProcessScoresBean.score = it.subAssessText
                projectProcessScoresBean.remarks = et_data_edit_remark.toString().trim()
                listVos.add(projectProcessScoresBean)
            }
            dto.projectProcessScores = listVos
            mPresenter.postAuditProcess(dto)
        }


    }

    override fun onScoreTempleResult(data: MutableList<ScoreTemplateBean>?) {
        if (data != null) {
            reportListAdapter.setNewData(data)
        }
    }

    override fun auditProcessResult(data: Any?) {
        Log.e("TAG", "auditProcessResult: ")
    }
}
