package com.zx.projectmanage.module.other.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.zx.projectmanage.R
import com.zx.projectmanage.app.ConstStrings
import com.zx.projectmanage.base.BaseActivity
import com.zx.projectmanage.module.other.func.tool.CameraView.GTCameraView
import com.zx.projectmanage.module.other.func.tool.CameraView.listener.CameraListener

import com.zx.projectmanage.module.other.mvp.contract.CameraContract
import com.zx.projectmanage.module.other.mvp.model.CameraModel
import com.zx.projectmanage.module.other.mvp.presenter.CameraPresenter
import com.zx.zxutils.util.ZXFileUtil
import com.zx.zxutils.util.ZXTimeUtil
import com.zx.zxutils.views.ZXStatusBarCompat
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat


/**
 * Create By admin On 2017/7/11
 * 功能：
 */
class CameraActivity : BaseActivity<CameraPresenter, CameraModel>(), CameraContract.View {

    companion object {
        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, cameraType: Int = -1) {
            val intent = Intent(activity, CameraActivity::class.java)
            intent.putExtra("cameraType", cameraType)
            activity.startActivityForResult(intent, 0x02)
            if (isFinish) activity.finish()
        }

        /**
         * 启动器
         */
        fun startAction(activity: Activity, isFinish: Boolean, cameraType: Int = -1, requestCode: Int) {
            val intent = Intent(activity, CameraActivity::class.java)
            intent.putExtra("cameraType", cameraType)
            activity.startActivityForResult(intent, requestCode)
            if (isFinish) activity.finish()
        }

        /**
         * 启动器
         */
        fun startAction(fragment: Fragment, isFinish: Boolean, cameraType: Int = -1, requestCode: Int) {
            val intent = Intent(fragment.context, CameraActivity::class.java)
            intent.putExtra("cameraType", cameraType)
            fragment.startActivityForResult(intent, requestCode)
        }

        /**
         * 启动器
         */
        fun startAction(fragment: Fragment, isFinish: Boolean, cameraType: Int = -1, requestCode: Int, filePath: String) {
            val intent = Intent(fragment.context, CameraActivity::class.java)
            intent.putExtra("cameraType", cameraType)
            intent.putExtra("filePath", filePath)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        return R.layout.activity_camera
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        ZXStatusBarCompat.translucent(this)

        val cameraType = intent.getIntExtra("cameraType", 0)
        val filepath = if (intent.hasExtra("filePath")) intent.getStringExtra("filePath") else ConstStrings.getCachePath()
        if (ZXFileUtil.isFileExists(ConstStrings.getCachePath())) {
            File(ConstStrings.getCachePath()).mkdirs()
        }
        //设置视频保存路径
        camera_view.setSaveVideoPath(filepath)
            .setCameraMode(if (cameraType == 1) GTCameraView.BUTTON_STATE_ONLY_CAPTURE else if (cameraType == 2) GTCameraView.BUTTON_STATE_ONLY_RECORDER else GTCameraView.BUTTON_STATE_BOTH)
            .setMediaQuality(GTCameraView.MEDIA_QUALITY_HIGH)
            .setMaxVedioDuration(30)
            .showAlbumView(true)
            .setCameraLisenter(object : CameraListener {
                override fun onCaptureCommit(bitmap: Bitmap) {
                    val time = ZXTimeUtil.getTime(System.currentTimeMillis(), SimpleDateFormat("yyyyMMdd_HHmmss"))
                    val name = time
                    val path = filepath + "/" + time + ".jpg"
                    bitmapToFile(bitmap, File(path))
                    val intent = Intent()
                    intent.putExtra("type", 1)
                    intent.putExtra("path", path)
                    intent.putExtra("name", name)
                    intent.putExtra("vedioPath", "")
                    setResult(0x02, intent)
                    finish()
                }

                override fun onRecordCommit(url: String, firstFrame: Bitmap) {
                    val time = ZXTimeUtil.getTime(System.currentTimeMillis(), SimpleDateFormat("yyyyMMdd_HHmmss"))
                    val name = time + ".mp4"
                    val path = filepath + "/" + time + ".jpg"
                    bitmapToFile(firstFrame, File(path))
                    val intent = Intent()
                    intent.putExtra("type", 2)
                    intent.putExtra("path", path)
                    intent.putExtra("vedioPath", url)
                    intent.putExtra("name", name)
                    setResult(0x02, intent)
                    finish()
                }

                override fun onActionSuccess(type: CameraListener.CameraType) {

                }

                override fun onError(errorType: CameraListener.ErrorType) {
                    //打开Camera失败回调
                }
            })
        if (cameraType == 1) {
            camera_view.setTip("轻触拍照")
        } else if (cameraType == 2) {
            camera_view.setTip("长按摄像")
        }

        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

    private fun bitmapToFile(bitmap: Bitmap, file: File?) {
        val baos = ByteArrayOutputStream()
        var options = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)
        while (baos.toByteArray().size / 1024 > 300) {
            baos.reset()
            options -= 10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)
        }
        try {
            val fos = FileOutputStream(file)
            fos.write(baos.toByteArray())
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        camera_view.onPause()
    }

    override fun onResume() {
        super.onResume()
        camera_view.onResume()
    }

}
