package com.lt.zxmap.view.dragView

import android.content.Context
import android.graphics.PointF
import android.graphics.Rect
import android.util.Log

import com.lt.zxmap.view.MeaOptManager
import com.lt.zxmap.view.MeasureHelper
import com.zxmap.zxmapsdk.geometry.LatLng
import com.zxmap.zxmapsdk.maps.MapView
import com.zxmap.zxmapsdk.maps.Projection

import java.util.ArrayList
import kotlin.math.cos
import kotlin.math.sin

/**
 * create by 96212 on 2020/6/16.
 * Email 962123525@qq.com
 * desc
 */
object DragViewManager {
    private var dragDrawView: DragDrawView? = null

    /**
     * 打开绘制
     */
    fun open(context: Context, mapView: MapView, drawType: DragDrawView.DrawType, isLine: Boolean = false, latLngs: (List<LatLng>) -> Unit) {
        dragDrawView = DragDrawView(context)
            .setType(drawType)
            .setIsLine(isLine)
            .setDrawResultListener(object : DragDrawResultListener {
                override fun drawHandPointResult(pointFs: ArrayList<PointF>) {
                    //手绘
                    val latlngs = arrayListOf<LatLng>()
                    val projection = mapView.zxMap.projection
                    pointFs.forEach {
                        latlngs.add(projection.fromScreenLocation(it))
                    }
                    latLngs(latlngs)
//                    dragDrawView?.clear()
                }

                override fun drawCircleResult(radius: Int, pointF: PointF) {
                    val projection = mapView.zxMap.projection
                    val latlngs = arrayListOf<LatLng>()
                    val angle = 1.0
                    for (index in 1..(360 / angle).toInt()) {
                        val pointX = pointF.x + radius * cos(Math.toRadians(angle * index))
                        val pointY = pointF.y + radius * sin(Math.toRadians(angle * index))
                        latlngs.add(projection.fromScreenLocation(PointF(pointX.toFloat(), pointY.toFloat())))
                    }
                    latlngs.add(projection.fromScreenLocation(PointF(pointF.x + radius * cos(Math.toRadians(angle)).toFloat(), pointF.y + radius * sin(Math.toRadians(angle)).toFloat())))
                    latLngs(latlngs)
//                    dragDrawView?.clear()
                }

                override fun drawRectangleResult(rect: Rect) {
                    //屏幕坐标转地图坐标
                    val latlngs = arrayListOf<LatLng>()
                    val leftTop = PointF(rect.left.toFloat(), rect.top.toFloat())
                    val rightTop = PointF(rect.right.toFloat(), rect.top.toFloat())
                    val leftBottom = PointF(rect.left.toFloat(), rect.bottom.toFloat())
                    val rightBottom = PointF(rect.right.toFloat(), rect.bottom.toFloat())
                    val projection = mapView.zxMap.projection
                    latlngs.add(projection.fromScreenLocation(leftTop))
                    latlngs.add(projection.fromScreenLocation(rightTop))
                    latlngs.add(projection.fromScreenLocation(rightBottom))
                    latlngs.add(projection.fromScreenLocation(leftBottom))
                    latlngs.add(projection.fromScreenLocation(leftTop))
                    latLngs(latlngs)
//                    dragDrawView?.clear()
                }
            })
        mapView.addView(dragDrawView)
    }

    /**
     * 退出绘制
     */
    fun exit() {
        dragDrawView?.exit()
    }

    fun resetType(isLine: Boolean) {
        dragDrawView?.setIsLine(isLine)
    }
}
