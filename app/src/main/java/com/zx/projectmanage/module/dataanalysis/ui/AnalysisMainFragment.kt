package com.zx.projectmanage.module.dataanalysis.ui

import android.os.Bundle
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.module.dataanalysis.mvp.contract.AnalysisMainContract
import com.zx.projectmanage.module.dataanalysis.mvp.model.AnalysisMainModel
import com.zx.projectmanage.module.dataanalysis.mvp.presenter.AnalysisMainPresenter

/**
 * Create By admin On 2017/7/11
 * 功能：数据分析
 */
class AnalysisMainFragment : BaseFragment<AnalysisMainPresenter, AnalysisMainModel>(),
    AnalysisMainContract.View {
    companion object {
        /**
         * 启动器
         */
        fun newInstance(): AnalysisMainFragment {
            val fragment = AnalysisMainFragment()
            val bundle = Bundle()

            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_analysis_main
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
