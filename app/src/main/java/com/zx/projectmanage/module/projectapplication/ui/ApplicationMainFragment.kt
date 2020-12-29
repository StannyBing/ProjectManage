package com.zx.projectmanage.module.projectapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.BannerScroller
import com.youth.banner.view.BannerViewPager
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.module.projectapplication.bean.ApplicationFuncBean
import com.zx.projectmanage.module.projectapplication.func.adater.ApplicationFuncsAdapter
import com.zx.projectmanage.module.projectapplication.func.tool.BannerPageTransformer
import com.zx.projectmanage.module.projectapplication.func.tool.GlideImageLoader
import com.zx.projectmanage.module.projectapplication.mvp.contract.ApplicationMainContract
import com.zx.projectmanage.module.projectapplication.mvp.model.ApplicationMainModel
import com.zx.projectmanage.module.projectapplication.mvp.presenter.ApplicationMainPresenter
import kotlinx.android.synthetic.main.fragment_application_main.*
import java.lang.reflect.Field

/**
 * Create By admin On 2017/7/11
 * 功能：项目应用
 */
class ApplicationMainFragment : BaseFragment<ApplicationMainPresenter, ApplicationMainModel>(),
    ApplicationMainContract.View {
    companion object {
        /**
         * 启动器
         */
        fun newInstance(): ApplicationMainFragment {
            val fragment = ApplicationMainFragment()
            val bundle = Bundle()

            fragment.arguments = bundle
            return fragment
        }
    }

    private val funcList = arrayListOf<ApplicationFuncBean>()
    private val funcAdapter = ApplicationFuncsAdapter(funcList)

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_application_main
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        funcList.add(ApplicationFuncBean("施工上报", R.drawable.icon_func_sgsb))
        funcList.add(ApplicationFuncBean("施工审核", R.drawable.icon_func_sgsh))
        funcList.add(ApplicationFuncBean("打卡考勤", R.drawable.icon_func_dkkq))
        funcList.add(ApplicationFuncBean("考勤管理", R.drawable.icon_func_kqgl))
        funcList.add(ApplicationFuncBean("维护任务", R.drawable.icon_func_whrw))
        funcList.add(ApplicationFuncBean("维护审核", R.drawable.icon_func_whsh))
        funcList.add(ApplicationFuncBean("巡查巡检", R.drawable.icon_func_xcxj))
        funcList.add(ApplicationFuncBean("单位信息", R.drawable.icon_func_dwxx))
        funcList.add(ApplicationFuncBean("工作计划", R.drawable.icon_func_gzjh))
        funcList.add(ApplicationFuncBean("安全咨询", R.drawable.icon_func_aqzx))
        funcList.add(ApplicationFuncBean("通讯录", R.drawable.icon_func_txl))
        rv_application_list.apply {
            layoutManager = GridLayoutManager(mContext, 4)
            adapter = funcAdapter
        }

        initBanner()
        super.initView(savedInstanceState)
    }

    private fun initBanner() {
        val images = arrayListOf<String>()
        images.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa")
        images.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Foss.huangye88.net%2Flive%2Fimport%2Fnews%2Fa66fe8cb5404d9db0b28bda82e1fb3f2.jpg&refer=http%3A%2F%2Foss.huangye88.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290806&t=c7e88cafea59540cee5a619de8388a33")
        images.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimages.shobserver.com%2Fnews%2F690_390%2F2018%2F12%2F2%2Fbaa716ec-9983-426d-b445-f629085b9944.jpg&refer=http%3A%2F%2Fimages.shobserver.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290829&t=85b0659903b09403297cbd19b91975a6")
        banner_application_info
            .setImageLoader(GlideImageLoader())
            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            .setPageTransformer(true, BannerPageTransformer())
            .isAutoPlay(true)
            .setDelayTime(6000)
            .setOnBannerListener { index ->

            }
            .setImages(images)
            .start()

        try {
            val mField: Field = Banner::class.java.getDeclaredField("viewPager")
            mField.isAccessible = true
            val method = mField.declaringClass.getDeclaredMethod("setPageMargin", Int::class.java)
            method.invoke(mField, 50)
        } catch (e: Exception) {
            Log.e(tag, e.message!!)
        }
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }
}
