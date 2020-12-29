package com.lt.zxmap.tool;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lt.zxmap.R;
import com.lt.zxmap.identify.ZxMapIdentify;
import com.zx.zxutils.util.ZXToastUtil;
import com.zxmap.zxmapsdk.camera.CameraPosition;
import com.zxmap.zxmapsdk.camera.CameraUpdate;
import com.zxmap.zxmapsdk.camera.CameraUpdateFactory;
import com.zxmap.zxmapsdk.geojson.MultiLineString;
import com.zxmap.zxmapsdk.geojson.MultiPoint;
import com.zxmap.zxmapsdk.geojson.MultiPolygon;
import com.zxmap.zxmapsdk.geojson.Point;
import com.zxmap.zxmapsdk.geojson.Polygon;
import com.zxmap.zxmapsdk.geojson.core.commons.models.Position;
import com.zxmap.zxmapsdk.geometry.LatLng;
import com.zxmap.zxmapsdk.geometry.LatLngBounds;
import com.zxmap.zxmapsdk.location.LocationSource;
import com.zxmap.zxmapsdk.maps.ZXMap;
import com.zxmap.zxmapsdk.maps.ZXMapOptions;
import com.zxmap.zxmapsdk.style.layers.BackgroundLayer;
import com.zxmap.zxmapsdk.style.layers.CircleLayer;
import com.zxmap.zxmapsdk.style.layers.FillExtrusionLayer;
import com.zxmap.zxmapsdk.style.layers.FillLayer;
import com.zxmap.zxmapsdk.style.layers.Filter;
import com.zxmap.zxmapsdk.style.layers.Layer;
import com.zxmap.zxmapsdk.style.layers.LineLayer;
import com.zxmap.zxmapsdk.style.layers.PropertyFactory;
import com.zxmap.zxmapsdk.style.layers.RasterLayer;
import com.zxmap.zxmapsdk.style.layers.SymbolLayer;
import com.zxmap.zxmapsdk.style.sources.GeoJsonSource;
import com.zxmap.zxmapsdk.style.sources.RasterSource;
import com.zxmap.zxmapsdk.style.sources.TileSet;
import com.zxmap.zxmapsdk.telemetry.location.LocationEngine;
import com.zxmap.zxmapsdk.telemetry.location.LocationEngineListener;
import com.zxmap.zxmapsdk.telemetry.permissions.PermissionsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiangb on 2017/12/5.
 * 功能：地图工具类
 */

public class MapTool {

    private Activity activity;
    private ZXMap zxMap;
    private static final int PERMISSIONS_LOCATION = 0;
    private boolean enableLocation = false;
    private LocationEngineListener locationListener;
    private LocationEngine locationEngine;
    private Location myLocation;


    public MapTool(Activity activity, ZXMap zxMap) {
        this.activity = activity;
        this.zxMap = zxMap;
        this.locationEngine = LocationSource.getLocationEngine(activity);
    }

    //添加RasterLayer
    public RasterLayer addWMSRasterLayer(String url, String souceId, String layerId) {
        RasterSource imageSource = new RasterSource(
                souceId, url, 256);
        zxMap.addSource(imageSource);
        RasterLayer rasterLayer = new RasterLayer(layerId, souceId);
        zxMap.addLayer(rasterLayer);
        return rasterLayer;
    }

    //添加RasterLayer-wms
    public RasterLayer addWMSRasterLayer(String url, String souceId, String layerId, int index) {
        TileSet tileSet = new TileSet("2.1.0", url);
        tileSet.setScheme("wms");
        RasterSource rasterSource = new RasterSource(souceId, tileSet, 256);
        //        RasterSource imageSource = new RasterSource(
        //                souceId, url, 256);
        zxMap.addSource(rasterSource);
        RasterLayer rasterLayer = new RasterLayer(layerId, souceId);
        zxMap.addLayerAt(rasterLayer, index);
        return rasterLayer;
    }

    //添加RasterLayer-index
    public RasterLayer addRasterLayer(String url, String souceId, String layerId, int index) {
        RasterSource imageSource = new RasterSource(
                souceId, url, 256);
        zxMap.addSource(imageSource);
        RasterLayer rasterLayer = new RasterLayer(layerId, souceId);
        zxMap.addLayerAt(rasterLayer, index);
        return rasterLayer;
    }

    //设置style透明度
    public void setStyleAlpha(String styleId, float alpha) {

        if (alpha > 1f) {
            alpha = alpha / 100;
        }

        if (!zxMap.isStyleExist(styleId)) {
            Log.e("setStyleAlpha", "该style不存在!");
            return;
        }
        if (alpha > 1.0f) {
            Log.e("setStyleAlpha", "透明度alpha应该为0.0f-1.0f");
            return;
        }
        try {
            List<String> layers = zxMap.getLayerIdsById(styleId);
            for (int i = 0; i < layers.size(); i++) {
                if (layers.get(i) != null) {
                    setLayerAlpah(zxMap.getLayer(layers.get(i)), alpha);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置style可见性
    public void setStyleVisible(String styleId, boolean visible) {
        if (!zxMap.isStyleExist(styleId)) {
            Log.e("setStyleVisible", "该style不存在!");
            return;
        }
        setStyleAlpha(styleId, visible ? 1.0f : 0.0f);
        //        try {
        //            List<String> layerids = zxMap.getLayerIdsById(styleId);
        //            for (int i = 0; i < layerids.size(); i++) {
        //                if (layerids.get(i) != null) {
        //                    zxMap.getLayer(layerids.get(i)).setVisible(visible);
        //                }
        //            }
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
    }

    //视角移动-坐标
    public void cameraMap(LatLng latLng) {
        zxMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    //视角移动-坐标、缩放比例
    public void cameraMap(LatLng latLng, int zoom) {
        zxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    //视角移动-倾斜角
    public void cameraMapT(double tilt) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .tilt(tilt)
                .build();
        zxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //视角移动-旋转角
    public void cameraMapB(double bearing) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .bearing(bearing)
                .build();
        zxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //权限回调
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_LOCATION) {
            if (!isRuntimePermissionsRequired() || isPermissionAccepted(grantResults)) {
                enableLocation(true);
            }
        }
    }

    private boolean isPermissionAccepted(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isRuntimePermissionsRequired() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void doLocation(boolean autoEnable) {
        if (!autoEnable) {
            enableLocation = false;
        }
        doLocation();
    }

    //定位
    public void doLocation() {
        if (!enableLocation) {
            if (!PermissionsManager.areLocationPermissionsGranted(activity)) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
            } else {
                enableLocation(true);
            }
        } else {
            enableLocation(false);
        }
    }

    //返回当前定位坐标
    public Location getLocation() {
        if (myLocation == null) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            myLocation = locationEngine.getLastLocation();
        }
        return myLocation;
    }

    private void enableLocation(boolean enabled) {
        if (enabled) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location lastLocation = locationEngine.getLastLocation();
            myLocation = lastLocation;
            if (lastLocation != null) {
                if (zxMap.getCameraPosition().zoom > 15.99) {
                    zxMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lastLocation)), 2000);
                } else {
                    zxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 14), 2000);
                }
            } else {
                if (locationListener != null) {
                    locationEngine.removeLocationEngineListener(locationListener);
                }
                locationListener = new LocationEngineListener() {
                    @Override
                    public void onConnected() {
                        // Nothing
                    }


                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            myLocation = location;
                            zxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 14), 2000);
                            locationEngine.removeLocationEngineListener(this);
                        }
                    }
                };
                locationEngine.addLocationEngineListener(locationListener);
                locationEngine.activate();
            }
        }
        zxMap.setMyLocationEnabled(enabled);
        if (enabled) {
            zxMap.setMyLocationEnabled(true);
            if (zxMap.getMyLocation() != null) {
                zxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(zxMap.getMyLocation().getLatitude(),
                                zxMap.getMyLocation().getLongitude()), 14), 2000);
            }
        } else {
            zxMap.setMyLocationEnabled(false);
        }
        enableLocation = !enableLocation;
    }

    public void setLayerAlpah(String id, float alpha) {
        try {
            Layer layer = zxMap.getLayer(id);
            setLayerAlpah(layer, alpha);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置图层透明度
    public void setLayerAlpah(Layer layer, float alpha) {
        try {
            if (layer instanceof LineLayer) {
                ((LineLayer) layer).withProperties(
                        PropertyFactory.lineOpacity(alpha)
                );
            } else if (layer instanceof BackgroundLayer) {
                ((BackgroundLayer) layer).withProperties(
                        PropertyFactory.backgroundOpacity(alpha)
                );
            } else if (layer instanceof FillLayer) {
                ((FillLayer) layer).withProperties(
                        PropertyFactory.fillOpacity(alpha)
                );
            } else if (layer instanceof FillExtrusionLayer) {
                ((FillExtrusionLayer) layer).withProperties(
                        PropertyFactory.fillExtrusionOpacity(alpha)
                );
            } else if (layer instanceof CircleLayer) {
                ((CircleLayer) layer).withProperties(
                        PropertyFactory.circleOpacity(alpha),
                        PropertyFactory.circleStrokeOpacity(alpha)
                );
            } else if (layer instanceof SymbolLayer) {
                ((SymbolLayer) layer).withProperties(
                        PropertyFactory.textOpacity(alpha),
                        PropertyFactory.iconOpacity(alpha)
                );
            } else if (layer instanceof RasterLayer) {
                ((RasterLayer) layer).withProperties(
                        PropertyFactory.rasterOpacity(alpha)
                );
            } else {
                layer.setProperties(
                        PropertyFactory.textOpacity(alpha),
                        PropertyFactory.iconOpacity(alpha),
                        PropertyFactory.rasterOpacity(alpha),
                        PropertyFactory.circleOpacity(alpha),
                        PropertyFactory.fillExtrusionOpacity(alpha),
                        PropertyFactory.fillOpacity(alpha),
                        PropertyFactory.lineOpacity(alpha),
                        PropertyFactory.circleStrokeOpacity(alpha),
                        PropertyFactory.backgroundOpacity(alpha)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置图层透明度
    public float getLayerAlpha(Layer layer) {
        try {
            if (layer instanceof LineLayer) {
                return ((LineLayer) layer).getLineOpacity().value;
            } else if (layer instanceof BackgroundLayer) {
                return ((BackgroundLayer) layer).getBackgroundOpacity().value;
            } else if (layer instanceof FillLayer) {
                return ((FillLayer) layer).getFillOpacity().value;
            } else if (layer instanceof FillExtrusionLayer) {
                return ((FillExtrusionLayer) layer).getFillExtrusionOpacity().value;
            } else if (layer instanceof CircleLayer) {
                return ((CircleLayer) layer).getCircleOpacity().value;
            } else if (layer instanceof SymbolLayer) {
                return ((SymbolLayer) layer).getTextOpacity().value;
            } else if (layer instanceof RasterLayer) {
                return ((RasterLayer) layer).getRasterOpacity().value;
            } else {
                return 1.0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1.0f;
        }
    }

    private GeoJsonSource source;
    private LineLayer lineLayer;

    /**
     * 绘制区县边界线
     *
     * @param areacode
     * @param geojson
     * @param geojsonlist
     */
    public void drawArea(String areacode, String geojson, List<List<Double>> geojsonlist) {


        //        Feature feature = Feature.fromJson(geojson);
        if (!zxMap.isSourceExist("area_source" + areacode)) {
            source = new GeoJsonSource("area_source" + areacode, geojson);
            zxMap.addSource(source);
        } else {
            source.setGeoJson(geojson);
        }

        if (!zxMap.isLayerExist("area_layer" + areacode)) {
            lineLayer = new LineLayer("area_layer" + areacode, "area_source" + areacode);
            lineLayer.setProperties(
                    PropertyFactory.lineOpacity(1f),
                    PropertyFactory.lineWidth(2.0f),
                    PropertyFactory.lineColor(ContextCompat.getColor(activity, R.color.areaColor)),
                    PropertyFactory.lineMiterLimit(3f)
            );
            zxMap.addLayer(lineLayer);
        }
        List<LatLng> latLngs = new ArrayList<>();
        try {
            MultiPolygon multiPolygon = MultiPolygon.fromJson(geojson);
            for (List<List<Position>> positonListList : multiPolygon.getCoordinates()) {
                for (List<Position> positionList : positonListList) {
                    for (Position position : positionList) {
                        latLngs.add(new LatLng(position.getLatitude(), position.getLongitude()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            latLngs.clear();
            for (int i = 0; i < geojsonlist.size(); i += (geojsonlist.size() > 100 ? geojsonlist.size() / 100 : 1)) {
                latLngs.add(new LatLng(geojsonlist.get(i).get(1), geojsonlist.get(i).get(0)));
            }
        }
        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(latLngs).build();
        CameraUpdate update =
                CameraUpdateFactory.newLatLngBounds(latLngBounds,
                        dp2px(420),
                        dp2px(80),
                        dp2px(80),
                        dp2px(80));
        zxMap.animateCamera(update, 1000);
    }


    /**
     * @param areacode
     */
    public Filter.Statement filter(String areacode) {
        if ("500002".equals(areacode)) {

            Filter.Statement mFilter0 = Filter.all(
                    Filter.gte("xzq_bm", 500103 + ""),
                    Filter.lte("xzq_bm", 500103 + "z")

            );

            Filter.Statement mFilter1 = Filter.all(
                    Filter.gte("xzq_bm", 500104 + ""),
                    Filter.lte("xzq_bm", 500104 + "z")

            );
            Filter.Statement mFilter2 = Filter.all(
                    Filter.gte("xzq_bm", 500105 + ""),
                    Filter.lte("xzq_bm", 500113 + "z")

            );
            Filter.Statement mFilter3 = Filter.all(
                    Filter.gte("xzq_bm", 500106 + ""),
                    Filter.lte("xzq_bm", 500106 + "z")

            );
            Filter.Statement mFilter4 = Filter.all(
                    Filter.gte("xzq_bm", 500107 + ""),
                    Filter.lte("xzq_bm", 500107 + "z")

            );
            Filter.Statement mFilter5 = Filter.all(
                    Filter.gte("xzq_bm", 500108 + ""),
                    Filter.lte("xzq_bm", 500108 + "z")

            );
            Filter.Statement mFilter6 = Filter.all(
                    Filter.gte("xzq_bm", 500109 + ""),
                    Filter.lte("xzq_bm", 500109 + "z")

            );
            Filter.Statement mFilter7 = Filter.all(
                    Filter.gte("xzq_bm", 500112 + ""),
                    Filter.lte("xzq_bm", 500112 + "z")
            );
            Filter.Statement mFilter8 = Filter.all(
                    Filter.gte("xzq_bm", 500113 + ""),
                    Filter.lte("xzq_bm", 500113 + "z")
            );

            Filter.Statement statement = Filter.any(mFilter0, mFilter1, mFilter2, mFilter3, mFilter4, mFilter5, mFilter6, mFilter7, mFilter8);

            Filter.Statement mFilter9 = Filter.all(
                    Filter.gte("xzq_bm", 500110 + ""),
                    Filter.lte("xzq_bm", 500110 + "z")
            );

            Filter.Statement mFilter10 = Filter.all(
                    Filter.gte("xzq_bm", 500111 + ""),
                    Filter.lte("xzq_bm", 500111 + "z")
            );


            Filter.Statement statement1 = Filter.none(mFilter9, mFilter10);


            return Filter.all(statement, statement1);

        } else {
            return Filter.all(Filter.gte("xzq_bm", areacode), Filter.lte("xzq_bm", areacode + "z"));
        }
    }

    /**
     * 获取geojson的中心点
     *
     * @param geojson
     * @return
     */
    public LatLng getCenterPointFromGeoJson(String geojson) {
        try {
            if (geojson.contains("MultiPolygon")) {
                return calculatePolygonGravity(MultiPolygon.fromJson(geojson).getCoordinates().get(0));
            } else if (geojson.contains("Polygon")) {
                return calculatePolygonGravity(Polygon.fromJson(geojson).getCoordinates());
            } else if (geojson.contains("MultiPoint")) {
                MultiPoint multiPoin = MultiPoint.fromJson(geojson);
                return new LatLng(multiPoin.getCoordinates().get(0).getLatitude(), multiPoin.getCoordinates().get(0).getLongitude());
            } else if (geojson.contains("MultiLineString")) {
                MultiLineString multiLineString = MultiLineString.fromJson(geojson);
                return new LatLng(multiLineString.getCoordinates().get(0).get(0).getLatitude(), multiLineString.getCoordinates().get(0).get(0).getLongitude());
            } else if (geojson.contains("Point")) {
                Point point = Point.fromJson(geojson);
                return new LatLng(point.getCoordinates().getLatitude(), point.getCoordinates().getLongitude());
            } else {
                String point = geojson.substring(geojson.indexOf("[[[[") + 4, geojson.indexOf("]"));
                return new LatLng(Double.parseDouble(point.split(",")[1]), Double.parseDouble(point.split(",")[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            String point = geojson.substring(geojson.indexOf("[[[[") + 4, geojson.indexOf("]"));
            return new LatLng(Double.parseDouble(point.split(",")[1]), Double.parseDouble(point.split(",")[0]));
        }
    }

    /**
     * 获取geojson的范围
     *
     * @param geojson
     * @return
     */
    public LatLngBounds getBoundsFromGeoJson(String geojson) {
        try {
            List<LatLng> bounds = new ArrayList<>();
            if (geojson.contains("MultiPolygon")) {
                List<Position> positions = (MultiPolygon.fromJson(geojson).getCoordinates().get(0).get(0));
                for (Position position : positions) {
                    bounds.add(new LatLng(position.getLatitude(), position.getLongitude()));
                }
            } else if (geojson.contains("Polygon")) {
                List<Position> positions = (Polygon.fromJson(geojson).getCoordinates().get(0));
                for (Position position : positions) {
                    bounds.add(new LatLng(position.getLatitude(), position.getLongitude()));
                }
            } else if (geojson.contains("MultiPoint")) {
                List<Position> positions = (MultiPoint.fromJson(geojson).getCoordinates());
                if (positions.size() < 2) {
                    return null;
                }
                for (Position position : positions) {
                    bounds.add(new LatLng(position.getLatitude(), position.getLongitude()));
                }
            } else if (geojson.contains("MultiLineString")) {
                List<Position> positions = (MultiLineString.fromJson(geojson).getCoordinates().get(0));
                if (positions.size() < 2) {
                    return null;
                }
                for (Position position : positions) {
                    bounds.add(new LatLng(position.getLatitude(), position.getLongitude()));
                }
            } else if (geojson.contains("Point")) {
                return null;
            } else {
                return null;
            }
            return new LatLngBounds.Builder().includes(bounds).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //计算几何中心
    private LatLng calculatePolygon(List<List<Position>> positonListList) {
        double sumx = 0, sumy = 0;
        int count = 0;
        for (List<Position> positionList : positonListList) {
            for (Position position : positionList) {
                sumx += position.getLatitude();
                sumy += position.getLongitude();
                count++;
            }
        }

        return new LatLng(sumx / count, sumy / count);
    }

    //计算集合重心
    private LatLng calculatePolygonGravity(List<List<Position>> positonListList) {
        double area = 0.0;//多边形面积
        double Gx = 0.0, Gy = 0.0;// 重心的x、y
        for (List<Position> positionList : positonListList) {
            for (int i = 1; i <= positionList.size(); i++) {
                double iLat = positionList.get(i % positionList.size()).getLatitude();
                double iLng = positionList.get(i % positionList.size()).getLongitude();
                double nextLat = positionList.get(i - 1).getLatitude();
                double nextLng = positionList.get(i - 1).getLongitude();
                double temp = (iLat * nextLng - iLng * nextLat) / 2.0;
                area += temp;
                Gx += temp * (iLat + nextLat) / 3.0;
                Gy += temp * (iLng + nextLng) / 3.0;
            }
        }
        Gx = Gx / area;
        Gy = Gy / area;
        return new LatLng(Gx, Gy);
    }

    public int dp2px(final float dpValue) {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void onPause() {
        if (locationEngine != null)
            locationEngine.removeLocationUpdates();
    }

    public void onResume() {
        if (locationEngine != null) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationEngine.requestLocationUpdates();
                return;
            }

        }
    }
}
