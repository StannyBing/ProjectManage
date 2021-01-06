package com.zx.projectmanage.module.projectapplication.construction.func.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.allen.library.SuperTextView
import com.zx.projectmanage.R
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.bean.ScoreTemplateBean
import com.zx.projectmanage.module.projectapplication.construction.func.listener.DataStepListener
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportChildActivity
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter
import com.zx.zxutils.util.ZXToastUtil

class ApproveScoreAdapter(dataList: List<ScoreTemplateBean>) : ZXQuickAdapter<ScoreTemplateBean, ZXBaseHolder>(R.layout.item_approve_score_edit, dataList) {
    private var listener: DataStepListener? = null
    override fun convert(helper: ZXBaseHolder, item: ScoreTemplateBean) {
        helper.setText(R.id.tv_data_edit_name, item.subAssessmentName)
        helper.setText(R.id.et_score_edit_value, item.subAssessText)
        helper.setText(R.id.tv_data_edit_info, item.standard)
        helper.getView<EditText>(R.id.et_score_edit_value).hint = "请输入评分共(${item.fraction})"
        helper.getView<EditText>(R.id.et_score_edit_value).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val info = helper.getView<EditText>(R.id.et_score_edit_value).text.toString()
                if (info.toInt() > item.fraction!!) {
                    ZXToastUtil.showToast("请输入正确的分数")
                } else {
                    listener?.onEditChange(helper.adapterPosition, info)
                }

            }
        })
    }
}