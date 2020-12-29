package com.zx.projectmanage.module.main.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.zx.projectmanage.R
import com.zx.projectmanage.app.MyApplication
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.dataanalysis.ui.AnalysisMainFragment

import com.zx.projectmanage.module.main.mvp.contract.MainContract
import com.zx.projectmanage.module.main.mvp.model.MainModel
import com.zx.projectmanage.module.main.mvp.presenter.MainPresenter
import com.zx.projectmanage.module.projectapplication.main.ui.ApplicationMainFragment
import com.zx.projectmanage.module.projectsupervise.ui.SuperviseMainFragment
import com.zx.zxutils.views.TabViewPager.ZXTabViewPager
import com.zx.zxutils.views.ZXStatusBarCompat
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class MainActivity : BaseActivity<MainPresenter, MainModel>(), MainContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZXStatusBarCompat.translucent(this)
        ZXStatusBarCompat.setStatusBarDarkMode(this)
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        tvp_main_layout.setManager(supportFragmentManager)
            .setTabScrollable(false)
            .setViewpagerCanScroll(false)
            .setTabLayoutGravity(ZXTabViewPager.TabGravity.GRAVITY_BOTTOM)
            .addTab(SuperviseMainFragment.newInstance(), "监管", R.drawable.selector_main_supervise)
            .addTab(ApplicationMainFragment.newInstance(), "应用", R.drawable.selector_main_application)
            .addTab(AnalysisMainFragment.newInstance(), "统计", R.drawable.selector_main_analysis)
            .addTab(UserFragment.newInstance(), "我的", R.drawable.selector_main_user)
            .setTitleColor(
                ContextCompat.getColor(this, R.color.default_text_color),
                ContextCompat.getColor(this, R.color.colorAccent)
            )
            .setTabTextSize(10)
            .setIndicatorHeight(0)
            .setTablayoutHeight(50)
            .setTabImageSize(20)
            .setTablayoutBackgroundColor(ContextCompat.getColor(this, R.color.white))
            .showDivider()
            .build()
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        tvp_main_layout.tabLayout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.position) {
                    0, 3 -> {
                        ZXStatusBarCompat.setStatusBarDarkMode(this@MainActivity)
                    }
                    1, 2 -> {
                        ZXStatusBarCompat.setStatusBarLightMode(this@MainActivity)
                    }
                }
            }
        })
    }

    private var backMills = 0L
    override fun onBackPressed() {
        if (backMills == 0L || System.currentTimeMillis() - backMills > 2000) {
            showToast("再次点击，退出应用")
            backMills = System.currentTimeMillis()
        } else {
//            MyApplication.instance.finishAll()
            super.onBackPressed()
        }
    }

}
