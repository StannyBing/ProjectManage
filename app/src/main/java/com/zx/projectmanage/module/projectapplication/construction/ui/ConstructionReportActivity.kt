package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.ReportListBean
import com.zx.projectmanage.module.projectapplication.construction.func.adapter.ReportListAdapter
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.ConstructionReportContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.ConstructionReportModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.ConstructionReportPresenter
import com.zx.zxutils.util.ZXToastUtil
import com.zx.zxutils.views.SwipeRecylerView.ZXSRListener
import kotlinx.android.synthetic.main.activity_construction_report.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class ConstructionReportActivity : BaseActivity<ConstructionReportPresenter, ConstructionReportModel>(), ConstructionReportContract.View {
    private var list: MutableList<ReportListBean> = arrayListOf<ReportListBean>()
    private val reportListAdapter = ReportListAdapter(list)

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, ConstructionReportActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_construction_report
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
        val srListener: Any = swipeRecyler.setLayoutManager(LinearLayoutManager(this))
            .setAdapter(reportListAdapter)
            .setPageSize(15)
            .autoLoadMore()
            .setSRListener(object : ZXSRListener<String> {
                override fun onItemClick(item: String, position: Int) {
                    ZXToastUtil.showToast("点击:$item")
                }

                override fun onItemLongClick(item: String, position: Int) {
                    ZXToastUtil.showToast("长按:$item")
                }

                override fun onRefresh() {

                }

                override fun onLoadMore() {
                    Handler().postDelayed(Runnable { }, 1000)

                }
            })
        swipeRecyler.notifyDataSetChanged()
//        addList();
        //        addList();
        swipeRecyler.refreshData(list, 0)
    }

}
