package com.zx.projectmanage.module.main.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zx.projectmanage.R
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.app.ConstStrings
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.base.SimpleDecoration
import com.zx.projectmanage.base.UserManager
import com.zx.projectmanage.module.main.bean.UserSettingBean
import com.zx.projectmanage.module.main.bean.VersionBean
import com.zx.projectmanage.module.main.func.adapter.UserSettingAdapter
import com.zx.projectmanage.module.main.mvp.contract.UserContract
import com.zx.projectmanage.module.main.mvp.model.UserModel
import com.zx.projectmanage.module.main.mvp.presenter.UserPresenter
import com.zx.projectmanage.module.system.ui.LoginActivity
import com.zx.zxutils.util.ZXAppUtil
import com.zx.zxutils.util.ZXDialogUtil
import com.zx.zxutils.util.ZXFileUtil
import com.zx.zxutils.util.ZXSystemUtil
import kotlinx.android.synthetic.main.fragment_user.*
import java.io.File

/**
 * Create By admin On 2017/7/11
 * 功能：个人中心
 */
class UserFragment : BaseFragment<UserPresenter, UserModel>(), UserContract.View {
    companion object {
        /**
         * 启动器
         */
        fun newInstance(): UserFragment {
            val fragment = UserFragment()
            val bundle = Bundle()

            fragment.arguments = bundle
            return fragment
        }
    }

    private val settingList = arrayListOf<UserSettingBean>()
    private val settingAdapter = UserSettingAdapter(settingList)

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.fragment_user
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        Glide.with(mContext)
            .load("${ApiConfigModule.BASE_IP}admin/sys-file/getFile?fileName=${UserManager.user?.user_info?.avatar}")
            .apply(RequestOptions.circleCropTransform())
            .into(iv_user_headicon)

        settingList.add(UserSettingBean("版本更新"))
        settingList.add(UserSettingBean("二维码下载"))
        settingList.add(UserSettingBean("版本信息"))
        settingList.add(UserSettingBean("清除缓存"))
        settingList.add(UserSettingBean("使用帮助"))
        settingList.add(UserSettingBean("意见反馈"))
        settingList.add(UserSettingBean("修改密码", showMargin = true))
        settingList.add(UserSettingBean("退出登录"))

        rv_user_setting.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = settingAdapter
            addItemDecoration(SimpleDecoration(mContext))
        }

        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        settingAdapter.setOnItemClickListener { adapter, view, position ->
            when (settingList[position].name) {
                "版本信息" -> {
                    ZXDialogUtil.showInfoDialog(mContext, "版本信息", "当前版本为：${ZXSystemUtil.getVersionName()}, 版本号：${ZXSystemUtil.getVersionCode()}")
                }
                "清除缓存" -> {
                    showLoading("正在清理中...")
                    clearFile()
                    Thread {
                        Glide.get(mContext).clearDiskCache()
                    }.start()
                    Glide.get(mContext).clearMemory()
                    handler.postDelayed({
                        dismissLoading()
                        showToast("清理完成")
                    }, 1000)
                }
                "退出登录" -> {
                    ZXDialogUtil.showYesNoDialog(mContext, "提示", "是否退出登录？") { dialog, which ->
                        UserManager.loginOut()
                        LoginActivity.startAction(requireActivity(), true)
                    }
                }
                "意见反馈" -> {
                    FeedbackActivity.startAction(activity!!, false)
                }
                else -> {
                    showToast("正在建设中")
                }
            }
        }
    }

    private fun clearFile() {
        val localFile = File(ConstStrings.getLocalPath())
        if (localFile.isDirectory) {
            localFile.listFiles().forEach {
                ZXFileUtil.deleteFiles(it)
            }
        } else {
            ZXFileUtil.deleteFiles(localFile)
        }
    }

    override fun onVersionResult(versionBean: VersionBean) {
        if (ZXSystemUtil.getVersionCode() < versionBean.versionCode) {
            val content = if (versionBean.content.isNullOrEmpty()) {
                ""
            } else {
                "-" + versionBean.content
            }
            ZXDialogUtil.showYesNoDialog(
                mContext,
                "提示",
                "检测到最新版本：${versionBean.versionName}${content}，是否立即更新"
            ) { dialog, which ->
                mPresenter.downloadApk(versionBean)
            }
        } else {
            showToast("已是最新版本")
        }
    }

    override fun onDownloadProgress(progress: Int) {
        ZXDialogUtil.showLoadingDialog(mContext, "下载中...", if (progress >= 100) 99 else progress)
    }

    override fun onApkDownloadResult(file: File) {
        ZXDialogUtil.dismissLoadingDialog()
        ZXAppUtil.installApp(mActivity, file.path)
    }
}
