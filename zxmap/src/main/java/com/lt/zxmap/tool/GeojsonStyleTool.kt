package com.lt.zxmap.tool

import com.zxmap.zxmapsdk.camera.CameraUpdateFactory
import com.zxmap.zxmapsdk.geojson.Feature
import com.zxmap.zxmapsdk.geojson.FeatureCollection
import com.zxmap.zxmapsdk.geojson.LineString
import com.zxmap.zxmapsdk.geojson.MultiLineString
import com.zxmap.zxmapsdk.geojson.MultiPoint
import com.zxmap.zxmapsdk.geojson.MultiPolygon
import com.zxmap.zxmapsdk.geojson.Point
import com.zxmap.zxmapsdk.geojson.Polygon
import com.zxmap.zxmapsdk.geojson.core.commons.models.Position
import com.zxmap.zxmapsdk.geometry.LatLng
import com.zxmap.zxmapsdk.geometry.LatLngBounds
import com.zxmap.zxmapsdk.maps.ZXMap
import com.zxmap.zxmapsdk.style.layers.FillLayer
import com.zxmap.zxmapsdk.style.layers.LineLayer
import com.zxmap.zxmapsdk.style.layers.PropertyFactory
import com.zxmap.zxmapsdk.style.layers.SymbolLayer
import com.zxmap.zxmapsdk.style.sources.GeoJsonSource

/**
 * create by 96212 on 2020/6/16.
 * Email 962123525@qq.com
 * desc
 */
class GeojsonStyleTool private constructor() {
    private var geoJsonSource: GeoJsonSource? = null

    private object SingletonHolder {
        val instance = GeojsonStyleTool()
    }

    companion object {
        val instance: GeojsonStyleTool
            get() = SingletonHolder.instance
    }

    //绘制元素layer id
    var layerMap = mutableMapOf<String, String>()
    //图层是否显示
    var layerVisibility = mutableMapOf<String,Boolean>()
    /**
     * 绘制
     *
     * @param geojson
     * @return
     */
    fun drawStyle(geojson: String) {
        geoJsonSource?.setGeoJson(geojson)
    }

    /**
     * 多面坐标集合
     */
    private fun multiPolygonLatLngs(coordinates: List<List<List<Position>>>): ArrayList<LatLng> {
        val latlngs = arrayListOf<LatLng>()
        val positionsListList = coordinates
        if (positionsListList.isNotEmpty()) {
            positionsListList.forEach {
                if (it.isNotEmpty()) {
                    it.forEach {
                        if (it.isNotEmpty()) {
                            it.forEach {
                                latlngs.add(LatLng(it.latitude, it.longitude))
                            }
                        }
                    }
                }
            }
        }
        return latlngs
    }

    /**
     * 多点/线坐标集合
     */
    private fun mulitPointOrLineStringLatLngs(coordinates: List<Position>): ArrayList<LatLng> {
        val latlngs = arrayListOf<LatLng>()
        val positions = coordinates
        if (positions.isNotEmpty()) {
            positions.forEach {
                latlngs.add(LatLng(it.latitude, it.longitude))
            }
        }
        return latlngs
    }

    /**
     * 多线点/面坐标集合
     */
     fun multiLineStringOrPolygonLatLngs(coordinates: List<List<Position>>): ArrayList<LatLng> {
        val latlngs = arrayListOf<LatLng>()
        if (coordinates.isNotEmpty()) {
            coordinates.forEach {
                if (it.isNotEmpty()) {
                    it.forEach {
                        latlngs.add(LatLng(it.latitude, it.longitude))
                    }
                }
            }
        }
        return latlngs
    }

    /**
     * 构建feature
     */
    fun constructFeature(geojson: String): Feature {
        var feature = when {
            geojson.contains("MultiPolygon") -> {
                Feature.fromGeometry(MultiPolygon.fromJson(geojson))
            }
            geojson.contains("Polygon") -> {
                Feature.fromGeometry(Polygon.fromJson(geojson))
            }
            geojson.contains("MultiLineString") -> {
                Feature.fromGeometry(MultiLineString.fromJson(geojson))
            }
            geojson.contains("LineString") -> {
                Feature.fromGeometry(LineString.fromJson(geojson))
            }
            geojson.contains("MultiPoint") -> {
                Feature.fromGeometry(MultiPoint.fromJson(geojson))
            }
            geojson.contains("Point") -> {
                Feature.fromGeometry(Point.fromJson(geojson))
            }
            else -> Feature.fromJson(geojson)
        }
        return feature
    }

    /**
     * feature坐标集合
     */
    private fun featureLatLngs(feature: Feature, map: ZXMap): ArrayList<LatLng> {
        var latLngs = arrayListOf<LatLng>()
        if (feature.geometry is Point) {
            val position = feature.geometry.coordinates as Position
            latLngs.add(LatLng(position.latitude, position.longitude))
            return latLngs
        } else if (feature.geometry is MultiPoint) {
            latLngs = mulitPointOrLineStringLatLngs(feature.geometry.coordinates as List<Position>)
        } else if (feature.geometry is LineString) {
            latLngs = mulitPointOrLineStringLatLngs(feature.geometry.coordinates as List<Position>)
        } else if (feature.geometry is MultiLineString) {
            latLngs =
                multiLineStringOrPolygonLatLngs(feature.geometry.coordinates as List<List<Position>>)
        } else if (feature.geometry is Polygon) {
            latLngs =
                multiLineStringOrPolygonLatLngs(feature.geometry.coordinates as List<List<Position>>)
        } else if (feature.geometry is MultiPolygon) {
            latLngs =
                multiPolygonLatLngs(feature.geometry.coordinates as List<List<List<Position>>>)
        }
        return latLngs
    }

    /**
     * 移动
     */
    fun animateToBoundsByGeojson(geojson: String, map: ZXMap) {
        var latlngs = arrayListOf<LatLng>()
        when {
            geojson.contains("FeatureCollection") -> {
                val collection = FeatureCollection.fromJson(geojson)
                collection.features?.forEach {
                    latlngs.addAll(featureLatLngs(it, map))
                }
            }
            geojson.contains("Feature") -> {
                latlngs = featureLatLngs(Feature.fromJson(geojson), map)
            }
            geojson.contains("MultiPolygon") -> {
                latlngs =
                    multiPolygonLatLngs(MultiPolygon.fromJson(geojson).coordinates as List<List<List<Position>>>)
            }
            geojson.contains("Polygon") -> {
                latlngs = multiLineStringOrPolygonLatLngs(Polygon.fromJson(geojson).coordinates)
            }
            geojson.contains("MultiLineString") -> {
                latlngs =
                    multiLineStringOrPolygonLatLngs(MultiLineString.fromJson(geojson).coordinates)
            }
            geojson.contains("LineString") -> {
                latlngs = mulitPointOrLineStringLatLngs(LineString.fromJson(geojson).coordinates)
            }
            geojson.contains("MultiPoint") -> {
                latlngs = mulitPointOrLineStringLatLngs(MultiPoint.fromJson(geojson).coordinates)
            }
            geojson.contains("Point") -> {
                val position = Point.fromJson(geojson).coordinates
                map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            position.latitude,
                            position.longitude
                        ), 14.0
                    )
                )
            }
        }

        if (latlngs.isNotEmpty()) {
            if (latlngs.size > 1) {
                val latLngBounds = LatLngBounds.Builder().includes(latlngs).build()
                map?.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 300), 1000)
            } else {
                map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        latlngs[0], 14.0
                    )
                )
            }
        }
    }

    /**
     * 初始化线Layer
     */
    fun initLineLayer(lineLayerId: String, sourceId: String, map: ZXMap, lineLayerColor: Int) {
        if (!map.isLayerExist(lineLayerId)) {
            val lineLayer = LineLayer(lineLayerId, sourceId)
            layerMap[lineLayerId] = sourceId
            lineLayer.setProperties(
                PropertyFactory.lineWidth(3.0f),
                PropertyFactory.lineColor(lineLayerColor)
            )
            map.addLayer(lineLayer)
        }
    }

    /**
     * 初始化面Layer
     */
    fun initFillLayer(layerId: String, sourceId: String, map: ZXMap, layerColor: Int) {
        if (!map.isLayerExist(layerId)) {
            val layer = FillLayer(layerId, sourceId)
            geoJsonSource = GeoJsonSource(sourceId)
            layerMap[layerId] = sourceId
            layer.setProperties(
                PropertyFactory.fillOpacity(0.5f),
                PropertyFactory.fillColor(layerColor)
            )
            map.addSource(geoJsonSource!!)
            map.addLayer(layer)
        }
    }

    /**
     * 初始化点Layer
     */
    fun initSymbolLayer(symbolId: String, sourceId: String, map: ZXMap) {
        if (!map.isLayerExist(symbolId)) {
            val symbolLayer = SymbolLayer(symbolId, sourceId)
            geoJsonSource = GeoJsonSource(sourceId)
            layerMap[symbolId] = sourceId
            symbolLayer.setProperties(
                PropertyFactory.iconImage("symbol_icon"),
                PropertyFactory.iconSize(0.6f),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.textAllowOverlap(true)
            )
            map.addSource(geoJsonSource!!)
            map.addLayer(symbolLayer)
        }
    }

    fun initSymbolLayer(symbolId: String, sourceId: String, text: String = "", map: ZXMap) {
        if (!map.isLayerExist(symbolId)) {
            val symbolLayer = SymbolLayer(symbolId, sourceId)
            geoJsonSource = GeoJsonSource(sourceId)
            layerMap[symbolId] = sourceId
            symbolLayer.setProperties(
                PropertyFactory.iconImage("polymeric_icon"),
                PropertyFactory.iconSize(2.5f),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.textAllowOverlap(true),
                PropertyFactory.textColor("#FFFFFF"),
                PropertyFactory.textField("{areaName}\n{areaSize}"),
                PropertyFactory.textSize(10.0f)
            )
            map.addSource(geoJsonSource!!)
            map.addLayer(symbolLayer)
        }
    }

    /**
     * 切换底图时重新加载
     */
    fun reloadLayer(map: ZXMap, lineColorId: Int, fillColorId: Int, text: String = "") {
        if (layerMap.isNotEmpty()) {
            layerMap.mapKeys {
                val value = map?.getLayer(it.key)?.visibility?.value
                layerVisibility[it.key] = value=="visible"
                map?.removeLayer(it.key)
            }
            layerMap.mapKeys {
                if (it.key.contains("Fill")) {
                    val layer = FillLayer(it.key, it.value)
                    layer.setProperties(
                        PropertyFactory.fillOpacity(0.5f),
                        PropertyFactory.fillColor(fillColorId)
                    )
                    map?.addLayer(layer)
                    layer.setVisible(layerVisibility[it.key]?:false)
                } else if (it.key.contains("Line")) {
                    val lineLayer = LineLayer(it.key, it.value)
                    lineLayer.setProperties(
                        PropertyFactory.lineWidth(3.0f),
                        PropertyFactory.lineColor(lineColorId)
                    )
                    map?.addLayer(lineLayer)
                    lineLayer.setVisible(layerVisibility[it.key]?:false)
                } else if (it.key.contains("counterSymbol")) {
                    val symbolLayer = SymbolLayer(it.key, it.value)
                    symbolLayer.setProperties(
                        PropertyFactory.iconImage("symbol_icon"),
                        PropertyFactory.iconSize(0.6f),
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.textAllowOverlap(true)
                    )
                    map.addLayer(symbolLayer)
                    symbolLayer.setVisible(layerVisibility[it.key]?:false)
                } else if (it.key.contains("polymericSymbol")) {
                    val symbolLayer = SymbolLayer(it.key, it.value)
                    symbolLayer.setProperties(
                        PropertyFactory.iconImage("polymeric_icon"),
                        PropertyFactory.iconSize(2.5f),
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.textAllowOverlap(true),
                        PropertyFactory.textColor("#FFFFFF"),
                        PropertyFactory.textField("{areaName}\n{areaSize}"),
                        PropertyFactory.textSize(10.0f)
                    )
                    map.addLayer(symbolLayer)
                    symbolLayer.setVisible(layerVisibility[it.key]?:false)
                }
            }
        }
    }

}
