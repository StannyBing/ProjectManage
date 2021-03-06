package com.zx.projectmanage.module.projectapplication.main.ui

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.module.projectapplication.construction.ui.ConstructionReportActivity
import com.zx.projectmanage.module.projectapplication.main.bean.ApplicationFuncBean
import com.zx.projectmanage.module.projectapplication.main.func.adater.ApplicationFuncsAdapter
import com.zx.projectmanage.module.projectapplication.main.mvp.contract.ApplicationMainContract
import com.zx.projectmanage.module.projectapplication.main.mvp.model.ApplicationMainModel
import com.zx.projectmanage.module.projectapplication.main.mvp.presenter.ApplicationMainPresenter
import com.zx.projectmanage.module.projectapplication.patrol.PatrolActivity
import kotlinx.android.synthetic.main.fragment_application_main.*

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
        images.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa")
        images.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinakd20200323ac%2F233%2Fw640h393%2F20200323%2F0a1c-ireifzh8572883.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1611290788&t=716ab4f8829340e491eeaa262987beaa")
        banner_application_info
            .isAutoLoop(true)
            .setLoopTime(5000)
            .setBannerGalleryEffect(8, 8, 5, 0.85f)
            .setAdapter(object : BannerImageAdapter<String>(images) {
                override fun onBindView(holder: BannerImageHolder, data: String?, position: Int, size: Int) {
                    Glide.with(mContext)
                        .load(data)
//                        .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                        .into(holder.imageView)
                }
            })
            .setOnBannerListener { data, position ->

            }
            .start()

    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        funcAdapter.setOnItemClickListener { adapter, view, position ->
            val any = adapter.data[position] as ApplicationFuncBean
            when (any.name) {
                "施工上报" -> {
                    ConstructionReportActivity.startAction(mContext as Activity, false, 0)
                }
                "施工审核" -> {
                    ConstructionReportActivity.startAction(mContext as Activity, false, 1)
                }
                "巡查巡检" -> {
                    PatrolActivity.startAction(mContext as Activity, false)
                }
            }


        }
    }
}
