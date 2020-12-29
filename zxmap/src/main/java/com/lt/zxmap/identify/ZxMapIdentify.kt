package com.lt.zxmap.identify

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.lt.zxmap.R
import com.lt.zxmap.tool.FilterTool
import com.zx.zxutils.http.ZXHttpListener
import com.zx.zxutils.http.ZXHttpTool
import com.zx.zxutils.util.ZXLogUtil
import com.zxmap.zxmapsdk.annotations.Icon
import com.zxmap.zxmapsdk.annotations.IconFactory
import com.zxmap.zxmapsdk.annotations.MarkerViewOptions
import com.zxmap.zxmapsdk.camera.CameraUpdateFactory
import com.zxmap.zxmapsdk.geojson.*
import com.zxmap.zxmapsdk.geometry.LatLng
import com.zxmap.zxmapsdk.maps.ZXMap
import com.zxmap.zxmapsdk.maps.ZXMap.InfoWindowAdapter
import com.zxmap.zxmapsdk.style.functions.Function
import com.zxmap.zxmapsdk.style.functions.stops.Stop
import com.zxmap.zxmapsdk.style.functions.stops.Stops
import com.zxmap.zxmapsdk.style.layers.*
import com.zxmap.zxmapsdk.style.layers.Filter
import com.zxmap.zxmapsdk.style.sources.GeoJsonSource
import com.zxmap.zxmapsdk.style.sources.VectorSource
import org.json.JSONObject
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Xiangb on 2020/2/25.
 * 功能：
 */
abstract class ZxMapIdentify(
    val context: Context,
    val zxMap: ZXMap,
    var config: ConfigBuilder = ConfigBuilder()
) {
    //参数配置
    private var latLng: LatLng? = null //触碰点坐标
    private var highLightFeature: Feature? = null //高亮图层
    private var markerViewOptions: MarkerViewOptions? = null //标记点配置器
    private var queryTitle = "" //查询标题
    private val queryList = arrayListOf<QueryInfo>() //查询结果
    private var queryView: View? = null
    private var funcList = arrayListOf<FuncInfo>()//功能按钮

    private val dynamicViewList = arrayListOf<View>()

    init {
        initInfoWindow()
    }

    /**
     * 接入地图点击事件
     *
     * @return 该地图点击事件是否被消费
     */
    fun onMapClick(latLng: LatLng): Boolean {
        clearStatus()
        this.latLng = latLng
        //要素查询
        val pointF = zxMap.projection.toScreenLocation(latLng)
        val space = (zxMap.cameraPosition.zoom / 7.0).toFloat()
        val rectF =
            RectF(pointF.x - space, pointF.y - space, pointF.x + space * 5, pointF.y + space * 5)
        val features = zxMap.queryRenderedFeatures(rectF, *queryLayers())
        if (features.size > 0) {
            highLightFeature = features[features.size - 1]
            clickFeature(latLng, features, highLightFeature!!)
            return true
        }
        return false
    }


    /**
     * 设置进行要素查询的图层
     */
    abstract fun queryLayers(): Array<String>

    /**
     * 弹框弹出操作
     */
    abstract fun infoWindowShown()

    abstract fun getStyleUrlByLayerId(id: String): String

    /**
     * 获取到点击的要素
     *
     * @param feature
     */
    abstract fun clickFeature(
        latLng: LatLng,
        features: List<Feature>,
        highLightFeature: Feature
    )

    /**
     * 图层变化
     */
    open fun layerChange(id: String, layer: Layer?, isAdd: Boolean) {

    }

    /**
     * 重设高亮图层的feature
     */
    fun setHighLightFeature(geoJson: String) {
        var feature: Feature? = null
        try {
            feature = if (geoJson.contains("MultiPolygon")) {
                Feature.fromGeometry(MultiPolygon.fromJson(geoJson))
            } else if (geoJson.contains("Polygon")) {
                Feature.fromGeometry(Polygon.fromJson(geoJson))
            } else if (geoJson.contains("MultiPoint")) {
                Feature.fromGeometry(MultiPoint.fromJson(geoJson))
            } else if (geoJson.contains("Point")) {
                Feature.fromGeometry(Point.fromJson(geoJson))
            } else if (geoJson.contains("MultiLineString")) {
                Feature.fromGeometry(MultiLineString.fromJson(geoJson))
            } else if (geoJson.contains("LineString")) {
                Feature.fromGeometry(LineString.fromJson(geoJson))
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (feature != null) {
            highLightFeature = feature
        }
    }


    fun setQueryTitle(title: String) {
        if (title.isNotEmpty()) {
            queryTitle = title
        }

    }

    /**
     * 填入查询结果参数集合
     *
     * @param title
     * @param view
     */
    fun setQueryInfos(title: String, view: View?, funcList: ArrayList<FuncInfo> = arrayListOf()) {
        if (latLng == null) {
            clearStatus()
        } else if (view == null) {
            Toast.makeText(context, "未查询到要素信息", Toast.LENGTH_SHORT).show()
            clearStatus()
        } else { //添加标记
            markerViewOptions =
                MarkerViewOptions().position(latLng!!).title("identify").icon(getIcon()).alpha(0.0f)
            zxMap.addMarker(markerViewOptions!!)
            //添加数据
            val identifyPoint =
                PointF(
                    zxMap.projection.toScreenLocation(latLng).x,
                    zxMap.projection.toScreenLocation(latLng).y
                )
            zxMap.animateCamera(
                CameraUpdateFactory.newLatLng(
                    zxMap.projection.fromScreenLocation(
                        identifyPoint
                    )
                ), 1000
            )
            addHighLightFeature()
            this.funcList.clear()
            if (funcList.isNotEmpty()) this.funcList.addAll(funcList)
            queryView = view
            queryTitle = title
            zxMap.selectMarker(markerViewOptions!!.marker)
            infoWindowShown()
        }
    }

    /**
     * 填入查询结果参数集合
     *
     * @param title
     * @param queryInfos
     */
    fun setQueryInfos(
        title: String,
        queryInfos: ArrayList<QueryInfo>,
        funcList: ArrayList<FuncInfo> = arrayListOf()
    ) {
        if (latLng == null) {
            clearStatus()
        } else if (queryInfos.isEmpty()) {
            Toast.makeText(context, "未查询到要素信息", Toast.LENGTH_SHORT).show()
            clearStatus()
        } else { //添加标记
            markerViewOptions =
                MarkerViewOptions().position(latLng!!).title("identify").icon(getIcon()).alpha(0.0f)
            zxMap.addMarker(markerViewOptions!!)
            //添加数据
            val identifyPoint =
                PointF(
                    zxMap.projection.toScreenLocation(latLng).x + config.popupOffsetX,
                    zxMap.projection.toScreenLocation(latLng).y + config.popupOffsetY
                )
            zxMap.animateCamera(
                CameraUpdateFactory.newLatLng(
                    zxMap.projection.fromScreenLocation(
                        identifyPoint
                    )
                ), 1000
            )
            addHighLightFeature()
            this.funcList.clear()
            if (funcList.isNotEmpty()) this.funcList.addAll(funcList)
            queryList.clear()
            queryList.addAll(queryInfos)
            queryTitle = title
            zxMap.selectMarker(markerViewOptions!!.getMarker())
            infoWindowShown()
        }
    }

    /**
     * 初始化弹窗
     */
    @SuppressLint("NewApi")
    private fun initInfoWindow() {
        zxMap.isAllowConcurrentMultipleOpenInfoWindows = false //不允许多个弹窗
        zxMap.infoWindowAdapter = InfoWindowAdapter {
            val view = LayoutInflater.from(context).inflate(R.layout.layout_identify, null, false)
            view.setOnClickListener(null)
            val ivLocation = view.findViewById<ImageView>(R.id.iv_identify_location)
            val ivClose = view.findViewById<ImageView>(R.id.iv_identify_close)
            val tvTitle = view.findViewById<TextView>(R.id.tv_identify_title)
            val rlTitle = view.findViewById<RelativeLayout>(R.id.rl_identify_title)
            val llList = view.findViewById<LinearLayout>(R.id.ll_identify_list)
            val llCustom = view.findViewById<LinearLayout>(R.id.ll_identify_customview)
            val svQueryList = view.findViewById<ScrollView>(R.id.sv_identify_queryList)
            val llBtnFunc = view.findViewById<LinearLayout>(R.id.ll_identify_btnfunc)
            rlTitle.background.setTint(ContextCompat.getColor(context, config.infoWindowTitleColor))
            rlTitle.setOnClickListener(null)
            ivLocation.setOnClickListener {
                zxMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        latLng!!,
                        config.locationZoom
                    ), 1000
                )
            }
            ivClose.setOnClickListener { clearStatus() }
            tvTitle.text = queryTitle
            llCustom.removeAllViews()
            llList.removeAllViews()
            llBtnFunc.removeAllViews()
            //设置内容
            if (queryView == null) {
                llCustom.visibility = View.GONE
                svQueryList.visibility = View.VISIBLE
                if (queryList.size > 0) {
                    for (info in queryList) {
                        val item = LayoutInflater.from(context)
                            .inflate(R.layout.map_identify_item, null, false)
                        val tvKey = item.findViewById<TextView>(R.id.tv_identify_key)
                        val tvValue = item.findViewById<TextView>(R.id.tv_identify_value)
                        tvKey.text = info.key
                        tvValue.text = info.value
                        llList.addView(item)
                    }
                }
            } else {
                llCustom.visibility = View.VISIBLE
                svQueryList.visibility = View.GONE
                llCustom.removeAllViews()
                if (queryView?.isAttachedToWindow == false)
                    llCustom.addView(queryView)
            }
            dynamicViewList.clear()
            //设置底部按钮
            if (funcList.isNotEmpty()) {
                llBtnFunc.background.setTint(
                    ContextCompat.getColor(
                        context,
                        config.infoWindowBtnFuncColor
                    )
                )
                funcList.forEachIndexed { index, func ->
                    val item = LayoutInflater.from(context)
                        .inflate(R.layout.item_identify_func, null, false)
                    item.layoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                    val ivIcon = item.findViewById<ImageView>(R.id.iv_func_icon)
                    val tvName = item.findViewById<TextView>(R.id.tv_func_name)
                    val viewDivider = item.findViewById<View>(R.id.view_divider)
                    if (func.funcIcon == null) {
                        ivIcon.visibility = View.GONE
                    } else {
                        ivIcon.visibility = View.VISIBLE
                        ivIcon.background = func.funcIcon
                    }

                    if (index == funcList.size - 1) {
                        viewDivider.visibility = View.GONE
                    } else {
                        viewDivider.visibility = View.VISIBLE
                    }
                    tvName.text = func.funcName
                    item.setOnClickListener {
                        func.funcClick()
                        clearStatus()
                    }
                    llBtnFunc.addView(item)
                    if (func.isDynamic) {
                        item.tag = func.funcName
                        dynamicViewList.add(item)
                    }
                }
            }
            view
        }
    }

    /**
     * 获取图标
     *
     * @return
     */
    private fun getIcon(): Icon {
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        return IconFactory.getInstance(context).fromBitmap(bitmap)
    }

    /**
     * 清除查询状态
     */
    public fun clearStatus() {
        //关闭参数弹窗
        if (markerViewOptions != null && markerViewOptions!!.marker != null) {
            markerViewOptions!!.marker.hideInfoWindow()
            zxMap.removeMarker(markerViewOptions!!.marker)
            zxMap.removeAnnotations()
        }
        //移除高亮图层
        zxMap.removeLayer("test_line_layer")
        zxMap.removeLayer("high_light_fill_layer")
        zxMap.removeLayer("high_light_line_layer")
        zxMap.removeLayer("high_light_circle_layer")
        zxMap.removeSource("test_line_source")
        zxMap.removeSource("high_light_fill_source")
        zxMap.removeSource("high_light_line_source")
        zxMap.removeSource("high_light_circle_source")
        layerChange("test_line_layer", null, false)
        layerChange("high_light_fill_layer", null, false)
        layerChange("high_light_line_layer", null, false)
        layerChange("high_light_circle_layer", null, false)
        //清空参数赋值
        latLng = null
        highLightFeature = null
        markerViewOptions = null
        queryTitle = ""
        queryList.clear()
        funcList.clear()
        queryView = null
    }

    /**
     * 添加高亮图层
     */
    private fun addHighLightFeature() {
        if (highLightFeature == null) {
            return
        }
//        addNormalHight()

        getVectorSource()
    }

    /**
     * 添加普通高亮（从feature中获取图形）
     */
    private fun addNormalHight() {
        if (highLightFeature!!.geometry is Polygon || highLightFeature!!.geometry is MultiPolygon) { //面
            zxMap.addSource(GeoJsonSource("high_light_fill_source", highLightFeature))
            val fillLayer =
                LineLayer("high_light_fill_layer", "high_light_fill_source").withProperties(
                    PropertyFactory.lineColor(
                        ContextCompat.getColor(
                            context,
                            config.highLightColor
                        )
                    ),
                    PropertyFactory.lineWidth(config.lineLayerWidth),
                    PropertyFactory.lineOpacity(config.lineLayerAlpha)
                    //                PropertyFactory.fillColor(
                    //                    ContextCompat.getColor(
                    //                        context,
                    //                        config.highLightColor
                    //                    )
                    //                ), PropertyFactory.fillOpacity(config.fillLayerAlpha)
                )
            zxMap.addLayer(fillLayer)
            layerChange("high_light_fill_layer", fillLayer, true)
        } else if (highLightFeature!!.geometry is LineString || highLightFeature!!.geometry is MultiLineString) { //线
            zxMap.addSource(GeoJsonSource("high_light_line_source", highLightFeature))
            val lineLayer =
                LineLayer("high_light_line_layer", "high_light_line_source").withProperties(
                    PropertyFactory.lineColor(
                        ContextCompat.getColor(
                            context,
                            config.highLightColor
                        )
                    ),
                    PropertyFactory.lineWidth(config.lineLayerWidth),
                    PropertyFactory.lineOpacity(config!!.lineLayerAlpha)
                )
            zxMap.addLayer(lineLayer)
            layerChange("high_light_line_layer", lineLayer, true)
        } else if (highLightFeature!!.geometry is Point || highLightFeature!!.geometry is MultiPoint) { //点
            zxMap.addSource(GeoJsonSource("high_light_circle_source", highLightFeature))
            val circleLayer =
                CircleLayer("high_light_circle_layer", "high_light_circle_source").withProperties(
                    PropertyFactory.circleColor(
                        ContextCompat.getColor(
                            context,
                            config.highLightColor
                        )
                    ), PropertyFactory.circleOpacity(config.pointLayerAlpha)
                )
            zxMap.addLayer(circleLayer)
            layerChange("high_light_circle_layer", circleLayer, true)
        }
    }

    /**
     * 根据图层id寻找source
     */
    private val tempVectorSourceUrlMap = hashMapOf<String, Pair<String, String>>()
    private fun getVectorSource() {
        try {
            if (tempVectorSourceUrlMap.containsKey(highLightFeature!!.layerId)) {
                addVectorLayer(
                    tempVectorSourceUrlMap[highLightFeature!!.layerId]!!.first,
                    tempVectorSourceUrlMap[highLightFeature!!.layerId]!!.second
                )
                return
            }
            val styleUrl = getStyleUrlByLayerId(highLightFeature!!.layerId)
            if (styleUrl.isEmpty()) {
                addNormalHight()
                return
            }
            ZXHttpTool.getHttp(styleUrl, hashMapOf(), object : ZXHttpListener<String>() {
                override fun onResult(t: String?) {
                    try {
                        val jsonObject = JSONObject(t)
                        for (i in 0 until jsonObject.getJSONArray("layers").length()) {
                            val layer = jsonObject.getJSONArray("layers").getJSONObject(i)
                            if (layer.getString("id") == highLightFeature!!.layerId) {
                                val sourceUrl = layer.getString("source")
                                val sourceLayer = layer.getString("source-layer")
                                tempVectorSourceUrlMap[highLightFeature!!.layerId] =
                                    sourceUrl to sourceLayer
                                addVectorLayer(sourceUrl, sourceLayer)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        addNormalHight()
                    }
                }

                override fun onError(msg: String?) {
                    addNormalHight()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            addNormalHight()
        }
    }

    /**
     * 添加无损高亮
     */
    private fun addVectorLayer(sourceUrl: String, sourceLayer: String) {
        if (zxMap.isLayerExist("test_line_layer")) {
            zxMap.removeLayer("test_line_layer")
        }
        if (zxMap.isSourceExist("test_line_source")) {
            zxMap.removeSource("test_line_source")
        }
        val testSource = VectorSource(
            "test_line_source",
            sourceUrl
        )
        val testLayer = LineLayer(
            "test_line_layer",
            "test_line_source"
        )
        testLayer.sourceLayer = sourceLayer
        testLayer.withProperties(
            PropertyFactory.lineColor(
                ContextCompat.getColor(
                    context,
                    config.highLightColor
                )
            ),
            PropertyFactory.lineWidth(config.lineLayerWidth),
            PropertyFactory.lineOpacity(config.lineLayerAlpha)
        )
        testLayer.setFilter(
            Filter.eq(
                "mapguid",
                highLightFeature!!.getStringProperty("mapguid")
            )
        )
        zxMap.addSource(testSource)
        zxMap.addLayer(testLayer)
        layerChange("test_line_layer", testLayer, true)
    }

    fun getDynamicViewList(): ArrayList<View> {
        return dynamicViewList
    }

    /**
     * 参数配置器
     */
    class ConfigBuilder {
        @ColorRes
        var highLightColor = R.color.highLightColor //高亮颜色 = 0
        var pointLayerAlpha = 0.6f //点图层透明度
        var lineLayerAlpha = 0.6f //线图层透明度
        var fillLayerAlpha = 0.4f //面图层透明度
        var lineLayerWidth = 2.0f //线图层宽度
        var locationZoom = 16.0 //定位缩放层级
        var popupOffsetX = 0.0f//弹框偏移量
        var popupOffsetY = 0.0f//弹框偏移量

        @ColorRes
        var infoWindowTitleColor = R.color.colorPrimary

        @ColorRes
        var infoWindowBtnFuncColor = R.color.colorPrimary
    }

    /**
     * 查询的展示数据
     */
    data class QueryInfo(var key: String, var value: String)

    /**
     * 按钮栏点击事件
     */
    data class FuncInfo(
        var funcIcon: Drawable? = null,
        var funcName: String,
        var isDynamic: Boolean = false,
        var funcClick: () -> Unit
    )
}