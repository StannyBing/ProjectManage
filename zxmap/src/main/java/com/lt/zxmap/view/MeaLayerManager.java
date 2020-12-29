package com.lt.zxmap.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.lt.zxmap.R;
import com.zxmap.zxmapsdk.geojson.Feature;
import com.zxmap.zxmapsdk.geojson.FeatureCollection;
import com.zxmap.zxmapsdk.geojson.LineString;
import com.zxmap.zxmapsdk.geojson.MultiPolygon;
import com.zxmap.zxmapsdk.geojson.Point;
import com.zxmap.zxmapsdk.geojson.Polygon;
import com.zxmap.zxmapsdk.geojson.core.commons.models.Position;
import com.zxmap.zxmapsdk.geometry.LatLng;
import com.zxmap.zxmapsdk.maps.ZXMap;
import com.zxmap.zxmapsdk.style.layers.CircleLayer;
import com.zxmap.zxmapsdk.style.layers.FillLayer;
import com.zxmap.zxmapsdk.style.layers.LineLayer;
import com.zxmap.zxmapsdk.style.layers.PropertyFactory;
import com.zxmap.zxmapsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static com.lt.zxmap.view.MeasureHelper.GEO_TYPE_Polygon;
import static com.lt.zxmap.view.MeasureHelper.GEO_TYPE_Polyline;

/**
 * Created by Xiangb on 2019/9/27.
 * 功能：
 */
public class MeaLayerManager {

    private Context context;
    private ZXMap zxMap;

    //全图形绘制
    public static final String Total_Line_Layer_Name = "total_measure_line_layer";
    private static final String Total_Line_Source_Name = "total_measure_line_source";
    public static final String Total_Fill_Layer_Name = "total_measure_fill_layer";
    private static final String Total_Fill_Source_Name = "total_measure_fill_source";
    private LineLayer totalLineLayer;
    private FillLayer totalFillLayer;
    private GeoJsonSource totalLineSource, totalFillSource;

    //单图形绘制
    public static final String Point_Layer_Name = "measure_point_layer";
    private static final String Point_Source_Name = "measure_point_source";
    public static final String Line_Layer_Name = "measure_line_layer";
    private static final String Line_Source_Name = "measure_line_source";
    public static final String Fill_Layer_Name = "measure_fill_layer";
    private static final String Fill_Source_Name = "measure_fill_source";
    private CircleLayer pointLayer;
    private LineLayer lineLayer;
    private FillLayer fillLayer;
    private GeoJsonSource pointSource, lineSource, fillSource;

    private MeaListener meaListener;

    public boolean showPoint = true;//是否显示点（手绘时不显示）

    MeaLayerManager(Context context, ZXMap zxMap, MeaListener meaListener) {
        this.context = context;
        this.zxMap = zxMap;
        this.meaListener = meaListener;

        pointSource = null;
        lineSource = null;
        fillSource = null;
        lineLayer = null;
        fillLayer = null;
        pointLayer = null;

        totalLineSource = null;
        totalFillSource = null;
        totalLineLayer = null;
        totalFillLayer = null;
    }

    /**
     * 添加图层
     */
    public void addLayer() {
        zxMap.addSource(pointSource);
        zxMap.addSource(lineSource);
        zxMap.addSource(fillSource);
        pointLayer = new CircleLayer(Point_Layer_Name, Point_Source_Name)
                .withProperties(
                        PropertyFactory.circleColor(ContextCompat.getColor(context, R.color.red)));

        lineLayer = new LineLayer(Line_Layer_Name, Line_Source_Name)
                .withProperties(
                        PropertyFactory.lineColor(ContextCompat.getColor(context, R.color.red)),
                        PropertyFactory.lineWidth(1.0f)
                );

        fillLayer = new FillLayer(Fill_Layer_Name, Fill_Source_Name)
                .withProperties(
                        PropertyFactory.fillOpacity(0.5f),
                        PropertyFactory.fillColor(ContextCompat.getColor(context, R.color.measureBlue))
                );

        zxMap.addLayer(fillLayer);
        zxMap.addLayer(lineLayer);
        zxMap.addLayer(pointLayer);
        meaListener.onLayerAdd(Fill_Layer_Name, fillLayer);
        meaListener.onLayerAdd(Line_Layer_Name, lineLayer);
        meaListener.onLayerAdd(Point_Layer_Name, pointLayer);

        zxMap.addSource(totalLineSource);
        zxMap.addSource(totalFillSource);
        totalLineLayer = new LineLayer(Total_Line_Layer_Name, Total_Line_Source_Name)
                .withProperties(
                        PropertyFactory.lineColor(ContextCompat.getColor(context, R.color.measureBlue)),
                        PropertyFactory.lineWidth(1.0f)
                );

        totalFillLayer = new FillLayer(Total_Fill_Layer_Name, Total_Fill_Source_Name)
                .withProperties(
                        PropertyFactory.fillOpacity(0.5f),
                        PropertyFactory.fillColor(ContextCompat.getColor(context, R.color.measureBlue))
                );
        zxMap.addLayer(totalFillLayer);
        zxMap.addLayer(totalLineLayer);
        meaListener.onLayerAdd(Total_Fill_Layer_Name, totalFillLayer);
        meaListener.onLayerAdd(Total_Line_Layer_Name, totalLineLayer);
    }

    /**
     * 清空图层
     */
    public void removeLayer() {
        if (pointLayer != null && zxMap.isLayerExist(pointLayer.getId())) {
            zxMap.removeLayer(pointLayer);
            meaListener.onLayerRemove(Point_Layer_Name);
        }
        if (lineLayer != null && zxMap.isLayerExist(lineLayer.getId())) {
            zxMap.removeLayer(lineLayer);
            meaListener.onLayerRemove(Line_Layer_Name);
        }
        if (fillLayer != null && zxMap.isLayerExist(fillLayer.getId())) {
            zxMap.removeLayer(fillLayer);
            meaListener.onLayerRemove(Fill_Layer_Name);
        }
        if (pointSource != null && zxMap.isSourceExist(pointSource.getId())) {
            zxMap.removeSource(pointSource);
        }
        if (lineSource != null && zxMap.isSourceExist(lineSource.getId())) {
            zxMap.removeSource(lineSource);
        }
        if (fillSource != null && zxMap.isSourceExist(fillSource.getId())) {
            zxMap.removeSource(fillSource);
        }
        pointLayer = null;
        lineLayer = null;
        fillSource = null;
        pointSource = null;
        fillLayer = null;
        lineSource = null;

        if (totalLineLayer != null && zxMap.isLayerExist(totalLineLayer.getId())) {
            zxMap.removeLayer(totalLineLayer);
            meaListener.onLayerRemove(Total_Line_Layer_Name);
        }
        if (totalFillLayer != null && zxMap.isLayerExist(totalFillLayer.getId())) {
            zxMap.removeLayer(totalFillLayer);
            meaListener.onLayerRemove(Total_Fill_Layer_Name);
        }
        if (totalLineSource != null && zxMap.isSourceExist(totalLineSource.getId())) {
            zxMap.removeSource(totalLineSource);
        }
        if (totalFillSource != null && zxMap.isSourceExist(totalFillSource.getId())) {
            zxMap.removeSource(totalFillSource);
        }
        totalLineLayer = null;
        totalFillSource = null;
        totalFillLayer = null;
        totalLineSource = null;
    }

    /**
     * 开启平移
     */
    public void startTranslate() {
        if (lineLayer != null) {
            lineLayer.withProperties(PropertyFactory.lineColor(ContextCompat.getColor(context, R.color.measureBlue)));
            pointLayer.withProperties(PropertyFactory.circleColor(ContextCompat.getColor(context, R.color.measureBlue)));
        }
    }

    /**
     * 关闭平移
     */
    public void stopTranslate() {
        if (lineLayer != null) {
            lineLayer.withProperties(PropertyFactory.lineColor(Color.RED));
            pointLayer.withProperties(PropertyFactory.circleColor(Color.RED));
        }
    }

    /**
     * 清除内容
     */
    public void clear() {
        if (meaListener.getLatlngs().size() > 0) {
            meaListener.getLatlngs().clear();
            updateTotal();
            updateSource();
        }
    }

    /**
     * 绘制总面
     */
    public void updateTotal() {
        if (totalLineSource == null) {
            totalLineSource = new GeoJsonSource(Total_Line_Source_Name);
        }
        if (totalFillSource == null) {
            totalFillSource = new GeoJsonSource(Total_Fill_Source_Name);
        }
        List<Feature> lineFeature = new ArrayList<>();
        List<List<Position>> polygon = new ArrayList<>();
        if (meaListener.getTotalList().size() > 0) {
            for (int i = 0; i < meaListener.getTotalList().size(); i++) {
                if (i != meaListener.getEditLatlngIndex()) {
                    List<Position> positions = new ArrayList<>();
                    for (LatLng latLng : meaListener.getTotalList().get(i)) {
                        positions.add(Position.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
                    }
                    if (meaListener.getGeoType() == GEO_TYPE_Polygon) {
                        positions.add(Position.fromLngLat(meaListener.getTotalList().get(i).get(0).getLongitude(), meaListener.getTotalList().get(i).get(0).getLatitude()));
                    }
                    lineFeature.add(Feature.fromGeometry(
                            LineString.fromCoordinates(
                                    positions
                            )
                    ));
                    polygon.add(positions);
                }
            }
        }
        totalLineSource.setGeoJson(FeatureCollection.fromFeatures(lineFeature));
        if (meaListener.getGeoType() == GEO_TYPE_Polygon) {
            totalFillSource.setGeoJson(Feature.fromGeometry(Polygon.fromCoordinates(
                    polygon
            )));
        }
    }

    /**
     * 更新信息
     */
    public void updateSource() {
        if (totalLineSource == null) {
            updateTotal();
        }
        if (pointSource == null) {
            pointSource = new GeoJsonSource(Point_Source_Name);
        }
        if (lineSource == null) {
            lineSource = new GeoJsonSource(Line_Source_Name);
        }
        if (fillSource == null) {
            fillSource = new GeoJsonSource(Fill_Source_Name);
        }
        if (pointLayer == null) {
            addLayer();
        }
        pointLayer.setVisible(showPoint);
        List<Feature> pointFeature = new ArrayList<>();
        List<Feature> lineFeature = new ArrayList<>();
        //添加点feature
        for (int i = 0; i < meaListener.getLatlngs().size(); i++) {
            Feature feature = Feature.fromGeometry(
                    Point.fromCoordinates(
                            new double[]{meaListener.getLatlngs().get(i).getLongitude(), meaListener.getLatlngs().get(i).getLatitude()}
                    )
            );
            feature.addNumberProperty("pointIndex", i);
            pointFeature.add(feature);
        }
        pointSource.setGeoJson(FeatureCollection.fromFeatures(pointFeature));
        //添加线feature
        if (meaListener.getLatlngs().size() > 1) {
            for (int i = 0; i < meaListener.getLatlngs().size(); i++) {
                if ((meaListener.getGeoType() == GEO_TYPE_Polygon) && i == meaListener.getLatlngs().size() - 1 && meaListener.getLatlngs().size() > 2) {//第三条线
                    Feature feature = Feature.fromGeometry(
                            LineString.fromCoordinates(
                                    new double[][]{{meaListener.getLatlngs().get(i).getLongitude(), meaListener.getLatlngs().get(i).getLatitude()}, {meaListener.getLatlngs().get(0).getLongitude(), meaListener.getLatlngs().get(0).getLatitude()}}
                            )
                    );
                    feature.addNumberProperty("lineIndex", i);
                    lineFeature.add(feature);
                } else if (i < meaListener.getLatlngs().size() - 1) {
                    Feature feature = Feature.fromGeometry(
                            LineString.fromCoordinates(
                                    new double[][]{{meaListener.getLatlngs().get(i).getLongitude(), meaListener.getLatlngs().get(i).getLatitude()}, {meaListener.getLatlngs().get(i + 1).getLongitude(), meaListener.getLatlngs().get(i + 1).getLatitude()}}
                            )
                    );
                    feature.addNumberProperty("lineIndex", i);
                    lineFeature.add(feature);
                }
            }
        }
        lineSource.setGeoJson(FeatureCollection.fromFeatures(lineFeature));

        //添加面feature
        if (meaListener.getLatlngs().size() > 2 && (meaListener.getGeoType() == GEO_TYPE_Polygon)) {
            List<List<Position>> polygon = new ArrayList<>();
            List<Position> position = new ArrayList<>();
            for (int i = 0; i < meaListener.getLatlngs().size(); i++) {
                position.add(Position.fromLngLat(meaListener.getLatlngs().get(i).getLongitude(), meaListener.getLatlngs().get(i).getLatitude()));
            }
            position.add(Position.fromLngLat(meaListener.getLatlngs().get(0).getLongitude(), meaListener.getLatlngs().get(0).getLatitude()));
            polygon.add(position);
            Feature feature = Feature.fromGeometry(
                    Polygon.fromCoordinates(
                            polygon
                    )
            );
            fillSource.setGeoJson(feature);
        }
        if (fillLayer != null) {
            if (meaListener.getLatlngs().size() < 3 || (meaListener.getGeoType() == GEO_TYPE_Polyline)) {
                fillLayer.setVisible(false);
            } else {
                fillLayer.setVisible(true);
            }
        }

        meaListener.onPointChange();
    }

}
