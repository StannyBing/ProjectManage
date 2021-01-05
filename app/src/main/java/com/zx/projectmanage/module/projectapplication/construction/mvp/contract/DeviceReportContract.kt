package com.zx.projectmanage.module.projectapplication.construction.mvp.contract

import android.location.Location
import com.frame.zxmvp.base.BasePresenter
import com.frame.zxmvp.base.IView
import com.frame.zxmvp.base.IModel
import com.zx.projectmanage.base.NormalList
import com.zx.projectmanage.module.projectapplication.construction.bean.BaiduGeocoderBean
import com.zx.projectmanage.module.projectapplication.construction.bean.DeviceInfoBean
import com.zx.projectmanage.module.projectapplication.construction.bean.StepStandardBean
import com.zx.projectmanage.module.projectapplication.construction.bean.*
import okhttp3.RequestBody
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
interface DeviceReportContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun onGeocoderResult(location: Location, geocoderBean: BaiduGeocoderBean)

        fun onStepStandardResult(stepStandardList: List<StepStandardBean>)

        fun onStepDetailResult(stepDetail: StepStandardBean)

        fun onDeviceDeleteResult()

        fun onSaveResult()
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun baiduGeocoderData(url: String): Observable<BaiduGeocoderBean>

        fun stepStandardData(map: HashMap<String, String>): Observable<NormalList<StepStandardBean>>

        fun stepDetailData(id: String): Observable<StepStandardBean>

        fun uploadFileData(body: RequestBody): Observable<FileUploadBean>

        fun saveInfoData(body: RequestBody): Observable<Any>

        fun deviceDeleteData(id: String) : Observable<Any>
    }

    //方法
    abstract class Presenter : BasePresenter<View, Model>() {
        abstract fun doGeocoder(location: Location)

        abstract fun getStepStandard(map: HashMap<String, String>)

        abstract fun getStepDetail(id: String)

        abstract fun saveDataInfo(
            dataList: List<DeviceInfoBean>,
            deviceBean: DeviceListBean?
        )

        abstract fun deleteDevice(id: String)
    }
}

