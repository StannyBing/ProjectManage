package com.zx.projectmanage.module.projectsupervise.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.module.projectsupervise.mvp.contract.SuperviseMainContract
import com.zx.projectmanage.module.projectsupervise.mvp.model.SuperviseMainModel
import com.zx.projectmanage.module.projectsupervise.mvp.presenter.SuperviseMainPresenter
import com.zx.zxutils.util.ZXFragmentUtil
import kotlinx.android.synthetic.main.fragment_supervise_main.*

/**
 * Create By admin On 2017/7/11
 * 功能：项目监管
 */
class SuperviseMainFragment : BaseFragment<SuperviseMainPresenter, SuperviseMainModel>(),
    SuperviseMainContract.View {
    companion object {
        /**
         * 启动器
         */
        fun newInstance(): SuperviseMainFragment {
            val fragment = SuperviseMainFragment()
            val bundle = Bundle()

            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var mapFragment: MapFragment

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_supervise_main
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        ZXFragmentUtil.addFragment(childFragmentManager, MapFragment.newInstance().apply { mapFragment = this }, R.id.fm_map)
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        mapLayer.setOnClickListener {
            if (layers.visibility == View.GONE) {
                layers.visibility = View.VISIBLE
                layers.animation = AnimationUtils.makeInAnimation(mContext, false)
            } else {
                layers.visibility = View.GONE
                layers.animation = AnimationUtils.makeOutAnimation(mContext, true)
            }

        }
    }
}
