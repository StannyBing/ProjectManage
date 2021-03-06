package com.zx.projectmanage.module.main.ui

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.dueeeke.videocontroller.StandardVideoController

import com.zx.projectmanage.R
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.main.mvp.contract.FeedbackContract
import com.zx.projectmanage.module.main.mvp.model.FeedbackModel
import com.zx.projectmanage.module.main.mvp.presenter.FeedbackPresenter
import com.zx.projectmanage.module.other.ui.CameraActivity
import com.zx.projectmanage.module.projectapplication.construction.func.tool.setHintKtx
import com.zx.zxutils.util.ZXDialogUtil
import com.zx.zxutils.views.BottomSheet.SheetData
import com.zx.zxutils.views.BottomSheet.ZXBottomSheet
import com.zx.zxutils.views.PhotoPicker.widget.ZXPhotoPickerView
import kotlinx.android.synthetic.main.activity_feedback.*


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class FeedbackActivity : BaseActivity<FeedbackPresenter, FeedbackModel>(), FeedbackContract.View {

    private val photoList1: ArrayList<String> = ArrayList()
    var VIDEOPATH = ""

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean) {
            val intent = Intent(activity, FeedbackActivity::class.java)
            activity.startActivity(intent)
            if (isFinish) activity.finish()
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_feedback
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        feedBackContent.setHintKtx(13, "请详细描述您的问题，这至关重要")
        mprv_photo1.init(this, ZXPhotoPickerView.ACTION_SELECT, photoList1)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {
        head.setLeftImageViewClickListener {
            finish()
        }
        video_tool.setRightTextGroupClickListener {
            if (video_tool.rightString == "删除") {
                player.visibility = View.GONE
                VIDEOPATH = ""
                video_tool.setRightString("添加")
            } else {
                getPermission(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)) {
                    val list = mutableListOf<String>("从相册选择视频", "拍摄视频")
                    ZXDialogUtil.showListDialog(mContext, "请选择视频选取方式", "取消", list) { p0, p1 ->
                        when (p1) {
                            1 -> {
                                CameraActivity.startAction(this, false, requestCode = 0x001)
                            }
                            0 -> {
                                val i = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                                startActivityForResult(i, 66)
                            }
                        }
                    }


                }

            }
        }
        btn_approve_submit.setOnClickListener {
            if (feedBackContent.text.toString().isNotEmpty()) {
                if (player.visibility == View.VISIBLE) {
                    photoList1.add(VIDEOPATH)
                    mPresenter.saveDataInfo(feedBackContent.text.toString(), photoList1, 1)
                } else {
                    mPresenter.saveDataInfo(feedBackContent.text.toString(), photoList1, 0)
                }
            } else {
                showToast("请务必描述您的问题")
                return@setOnClickListener
            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //已增加id，可以兼容多个控件同时出现
        mprv_photo1.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 66 && resultCode == Activity.RESULT_OK && null != data) {
            val selectedVideo: Uri? = data.data
            val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
            val cursor: Cursor? = contentResolver.query(
                selectedVideo!!,
                filePathColumn, null, null, null
            )
            cursor?.moveToFirst()
            val columnIndex: Int = cursor?.getColumnIndex(filePathColumn[0])!!
            VIDEOPATH = cursor?.getString(columnIndex)
            cursor.close()
            video_tool.setRightString("删除")
//            submit_vd_ad.setText(VIDEOPATH)
            player.visibility = View.VISIBLE
            player.setUrl(VIDEOPATH) //设置视频地址
            val controller = StandardVideoController(this)
            controller.addDefaultControlComponent("标题", false)
            player.setVideoController(controller) //设置控制
            player.start()
        }
        if (resultCode != Activity.RESULT_OK) {
            return
        }
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onResume() {
        super.onResume()
        player.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }


    override fun onBackPressed() {
        if (!player.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onSaveResult() {
        finish()
        showToast("上传成功")
    }
}
