package com.zx.projectmanage.module.projectapplication.construction.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.zx.projectmanage.R
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.projectapplication.construction.bean.FileInfoBean
import com.zx.projectmanage.module.projectapplication.construction.mvp.contract.DocumentContract
import com.zx.projectmanage.module.projectapplication.construction.mvp.model.DocumentModel
import com.zx.projectmanage.module.projectapplication.construction.mvp.presenter.DocumentPresenter
import com.zx.zxutils.util.ZXToastUtil
import kotlinx.android.synthetic.main.activity_document.*

import java.io.File


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class DocumentActivity : BaseActivity<DocumentPresenter, DocumentModel>(), DocumentContract.View {

    var id: String = ""

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, id: String?) {
            val intent = Intent(activity, DocumentActivity::class.java)
            intent.putExtra("id", id)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_document
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        id = intent.getStringExtra("id").toString().split(",")[0]
        mPresenter.getFileInfo(id)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        head.setLeftImageViewClickListener {
            finish()
        }
    }

    override fun onFileDownloadResult(file: File?) {
        ZXToastUtil.showToast(file?.absolutePath)
    }

    override fun onFileInfoResult(data: FileInfoBean?) {
        if (data?.type == "png") {
            Glide.with(mContext)
                .load(ApiConfigModule.BASE_IP + "admin/sys-file/getFileById?id=${id}")
                .into(iv_photo)
        } else {
            mPresenter.getFile(id, data!!.original, data.fileSize.toLong())
        }


    }

}
