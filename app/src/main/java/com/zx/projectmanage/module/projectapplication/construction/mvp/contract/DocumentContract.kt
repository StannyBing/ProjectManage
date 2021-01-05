package com.zx.projectmanage.module.projectapplication.construction.mvp.contract

import com.frame.zxmvp.base.BasePresenter
import com.frame.zxmvp.base.IModel
import com.frame.zxmvp.base.IView
import com.zx.projectmanage.module.projectapplication.construction.bean.FileInfoBean
import rx.Observable
import java.io.File

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
interface DocumentContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun onFileDownloadResult(file: File?)
        fun onFileInfoResult(data: FileInfoBean?)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun getFileInfo(id: String): Observable<FileInfoBean>
    }

    //方法
    abstract class Presenter : BasePresenter<View, Model>() {
        abstract fun getFile(id: String, fileName: String, countLength: Long)
        abstract fun getFileInfo(id: String)
    }
}

