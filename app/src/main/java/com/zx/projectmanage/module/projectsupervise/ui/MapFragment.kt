package com.zx.projectmanage.module.projectsupervise.ui

import android.graphics.PointF
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.lt.zxmap.tool.MapTool
import com.zx.projectmanage.R
import com.zx.projectmanage.api.ApiConfigModule
import com.zx.projectmanage.base.BaseFragment
import com.zx.projectmanage.module.projectsupervise.mvp.contract.MapContract
import com.zx.projectmanage.module.projectsupervise.mvp.model.MapModel
import com.zx.projectmanage.module.projectsupervise.mvp.presenter.MapPresenter
import com.zx.zxutils.util.ZXSystemUtil
import com.zxmap.zxmapsdk.ZXMapApp
import com.zxmap.zxmapsdk.camera.CameraUpdateFactory
import com.zxmap.zxmapsdk.geometry.LatLng
import com.zxmap.zxmapsdk.maps.ZXMap
import kotlinx.android.synthetic.main.fragment_map.*

/**
 * Create By admin On 2017/7/11
 * 功能：地图界面
 */
class MapFragment : BaseFragment<MapPresenter, MapModel>(), MapContract.View {
    companion object {
        /**
         * 启动器
         */
        fun newInstance(): MapFragment {
            val fragment = MapFragment()
            val bundle = Bundle()

            fragment.arguments = bundle
            return fragment
        }
    }

    private var mMapTool: MapTool? = null
    private var zxMap: ZXMap? = null

    /**
     * layout配置
     */
    override fun getLayoutId(): Int {
        ZXMapApp.getInstance(requireActivity())
        return R.layout.fragment_map
    }

    /**
     * 初始化
     */
    override fun initView(savedInstanceState: Bundle?) {
        map_view.onCreate(savedInstanceState)
        map_view.addStyle(ApiConfigModule.MAP_URL, "base_image")
        map_view.getMapAsync {
            this.zxMap = it
            mMapTool = MapTool(requireActivity(), zxMap)

            it.uiSettings.apply {
                compassImage = ContextCompat.getDrawable(mContext, R.drawable.map_ic_compass)
                compassGravity = Gravity.LEFT
                setCompassMargins(ZXSystemUtil.dp2px(70f), ZXSystemUtil.dp2px(25f), 0, 0)
                isRotateGesturesEnabled = false
            }

            //默认重庆中心点
            val mLatLng = LatLng(29.5667063930001, 106.547398158) //默认重庆中心点
            val pointF = it.projection.toScreenLocation(mLatLng)
            val newLatlng = it.projection.fromScreenLocation(PointF(pointF.x + ZXSystemUtil.dp2px(300f), pointF.y))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatlng, 10.0))
            //启动定位
            mMapTool?.doLocation()
        }
        super.initView(savedInstanceState)
    }

    /**
     * View事件设置
     */
    override fun onViewListener() {

    }

    override fun onStart() {
        super.onStart()
        map_view.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
        mMapTool?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
        mMapTool?.onPause()
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map_view.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (map_view != null) {
            map_view.onDestroy()
        }
    }


    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }
}
