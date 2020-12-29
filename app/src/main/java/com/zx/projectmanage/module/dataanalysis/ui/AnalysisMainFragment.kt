package com.zx.projectmanage.module.dataanalysis.ui

import android.os.Bundle
import com.bumptech.glide.Glide
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.module.dataanalysis.mvp.contract.AnalysisMainContract
import com.zx.projectmanage.module.dataanalysis.mvp.model.AnalysisMainModel
import com.zx.projectmanage.module.dataanalysis.mvp.presenter.AnalysisMainPresenter
import kotlinx.android.synthetic.main.fragment_analysis_main.*

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
        Glide.with(mContext)
            .load("http://192.168.1.248:9002/zdhjc/test/%E3%80%90%E6%8E%A8%E7%89%B9%E5%B0%8F%E8%A7%86%E9%A2%91%E3%80%91%E6%99%9A%E5%AE%89%E5%A4%A7%E8%B1%A1%E6%AD%8C_%E5%93%94%E5%93%A9%E5%93%94%E5%93%A9%20(%E3%82%9C-%E3%82%9C)%E3%81%A4%E3%83%AD%20%E5%B9%B2%E6%9D%AF~-bilibili.mp4")
            .into(iv_test)
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }
}
