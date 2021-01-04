package com.zx.projectmanage.module.projectapplication.construction.mvp.contract

import android.location.Location
import com.frame.zxmvp.base.BasePresenter
import com.frame.zxmvp.base.IView
import com.frame.zxmvp.base.IModel
import com.gt.giscollect.base.NormalList
import com.zx.projectmanage.module.projectapplication.approve.bean.BaiduGeocoderBean
import com.zx.projectmanage.module.projectapplication.approve.bean.StepStandardBean
import rx.Observable

/**
 * Create By admin On 2017/7/11
 * 功能：
 */
interface ConstructionDataContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun onGeocoderResult(location: Location, geocoderBean: BaiduGeocoderBean)

        fun onStepStandardResult(stepStandardList : List<StepStandardBean>)

        fun onStepDetailResult(stepDetail : StepStandardBean)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun baiduGeocoderData(url : String) : Observable<BaiduGeocoderBean>

        fun stepStandardData(map: HashMap<String, String>) : Observable<NormalList<StepStandardBean>>

        fun stepDetailData(id : String) : Observable<StepStandardBean>
    }

    //方法
    abstract class Presenter : BasePresenter<View, Model>() {
        abstract fun doGeocoder(location : Location)

        abstract fun getStepStandard(map: HashMap<String, String>)

        abstract fun getStepDetail(id : String)
    }
}

