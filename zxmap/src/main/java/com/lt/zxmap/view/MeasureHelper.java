package com.lt.zxmap.view;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lt.zxmap.R;
import com.lt.zxmap.view.dragView.DragDrawView;
import com.lt.zxmap.view.dragView.DragViewManager;
import com.zx.zxutils.other.QuickAdapter.ZXBaseHolder;
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter;
import com.zx.zxutils.util.ZXSystemUtil;
import com.zx.zxutils.util.ZXToastUtil;
import com.zx.zxutils.views.BubbleView.ZXBubbleView;
import com.zxmap.zxmapsdk.camera.CameraUpdateFactory;
import com.zxmap.zxmapsdk.geojson.Feature;
import com.zxmap.zxmapsdk.geojson.LineString;
import com.zxmap.zxmapsdk.geojson.MultiLineString;
import com.zxmap.zxmapsdk.geojson.MultiPoint;
import com.zxmap.zxmapsdk.geojson.MultiPolygon;
import com.zxmap.zxmapsdk.geojson.Point;
import com.zxmap.zxmapsdk.geojson.Polygon;
import com.zxmap.zxmapsdk.geojson.core.commons.models.Position;
import com.zxmap.zxmapsdk.geometry.LatLng;
import com.zxmap.zxmapsdk.maps.MapView;
import com.zxmap.zxmapsdk.maps.ZXMap;
import com.zxmap.zxmapsdk.style.layers.Layer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by Xiangb on 2019/9/23.
 * 功能：
 */
public class MeasureHelper implements MeaListener {

    public List<List<LatLng>> meaTotalList = new ArrayList<>();//总点集
    public List<List<LatLng>> tempMeaTotalList = new ArrayList<>();//备用总点集
    public List<LatLng> latLngList = new ArrayList<>();//当前绘制的点集
    private List<LatLng> tempLatlngs = new ArrayList<>();//备用点集
    public List<LatLng> drawLatlngs = new ArrayList<>();//绘制点集

    public static final int GEO_TYPE_Polyline = 1;
    public static final int GEO_TYPE_Polygon = 2;
    public static final int SECOND_TYPE_CIRCLE = 3;
    public static final int SECOND_TYPE_RECTANGLE = 4;
    public static final int SECOND_TYPE_LOCATION = 5;
    public static final int SECOND_TYPE_HANDPOINT = 6;
    public static final int SECOND_TYPE_INPUT = 7;
    public static final int SECOND_TYPE_GPS = 8;
    public int geotype = 1;//类型

    public int editLatlngIndex = 0;//当前正在编辑的点集

    private int editPoint = -1;//现在正编辑的点的index
    private LatLng moveLatlng;//移动的初始点坐标
    private LatLng transStartLatlng;//平移的初始点坐标
    private boolean isLongClick = false;//是否为在长按
    private boolean isMapLongClick = false;//是否当前为map的长按事件（非touch）
    private boolean isMeaTranslate = false;//当前是否为平移
    private Timer timer;
    private PointF longClickPoint;//长按的起始点屏幕坐标
    private Feature clickFeature;//长按时的feature

    private MapMeasureView meaView;
    private Context context;
    private MapView mapView;
    private ZXMap zxMap;
    private MeaStatusListener statusListener;
    public LocationFuncView locationFuncView;
    public LatLng locationPoint;
    private MeaLayerManager layerManager;//图层管理器
    private MeaOptManager optManager;//操作管理器
    private ZXBubbleView bubbleView;
    private ZXQuickAdapter mTotalAdapter;

    public MeasureHelper() {
        throw new RuntimeException("Do not create MeasureHelper with constructor,Please use MeasureHelper.bind!");
    }

    private MeasureHelper(MapMeasureView meaView, MapView mapView) {
        this.meaView = meaView;
        context = meaView.getContext();
        this.zxMap = mapView.getZXMap();
        this.mapView = mapView;
        init();
    }

    public static MeasureHelper bind(MapMeasureView meaView, MapView mapView) {
        return new MeasureHelper(meaView, mapView);
    }

    private void init() {
        layerManager = new MeaLayerManager(context, zxMap, this);
        optManager = new MeaOptManager(context, zxMap, this);

        updateSource(true);
    }

    /**
     * 上一步
     *
     * @return 是否已为最后一步
     */
    public boolean postUndo() {
        optManager.closeMarker();
        return optManager.postUndo();
    }

    /**
     * 下一步
     *
     * @return 是否已为最后一步
     */
    public boolean postRedo() {
        optManager.closeMarker();
        return optManager.postRedo();
    }


    /**
     * 设置绘制类型
     *
     * @param geotype GEO_TYPE_Polyline、GEO_TYPE_Polygon
     */
    public void setGeoType(int geotype) {
        stopTranslate();
        this.geotype = geotype;
        DragViewManager.INSTANCE.resetType(geotype == GEO_TYPE_Polyline);
    }

    /**
     * 多功能工具箱
     *
     * @param view
     * @param name
     */
    public void postTool(final View view, String name) {
        removeLocationFunc();
        DragViewManager.INSTANCE.exit();
        switch (name) {
            case "记录":
               if (bubbleView==null) bubbleView = new ZXBubbleView(context);
                View content = LayoutInflater.from(context).inflate(R.layout.layout_mea_point_list, null, false);
                RecyclerView rvList = content.findViewById(R.id.rv_mea_point_list);
                rvList.setLayoutManager(new LinearLayoutManager(context));
//                rvList.setAdapter(mAdapter = new ZXQuickAdapter<List<LatLng>, ZXBaseHolder>(R.layout.item_mea_point_list, meaTotalList) {
                rvList.setAdapter(mTotalAdapter = new ZXQuickAdapter<List<LatLng>, ZXBaseHolder>(R.layout.item_mea_point_list, tempMeaTotalList) {
                    @Override
                    protected void convert(@NonNull ZXBaseHolder helper, final List<LatLng> item) {
                        helper.setText(R.id.tv_point_title, (helper.getAdapterPosition() + 1) + "");
                        helper.addOnClickListener(R.id.iv_point_list_delete);
                        LinearLayout itemView = helper.getView(R.id.ll_point_list);
                        itemView.removeAllViews();
                        for (int i = 0; i < item.size(); i++) {
                            View items = LayoutInflater.from(context).inflate(R.layout.item_mea_point_items, null, false);
                            ((TextView) items.findViewById(R.id.tv_mea_point_items_num)).setText((i + 1) + "");
                            ((TextView) items.findViewById(R.id.tv_mea_point_items_lon)).setText(String.format("%.6f", item.get(i).getLongitude()));
                            ((TextView) items.findViewById(R.id.tv_mea_point_items_lat)).setText(String.format("%.6f", item.get(i).getLatitude()));
                            items.setTag(i);
                            itemView.addView(items);
                            items.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    zxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(item.get((Integer) v.getTag()), 15.0));
                                }
                            });
                        }
                    }
                });
                mTotalAdapter.setOnItemChildClickListener(new ZXQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(ZXQuickAdapter adapter, View view, int position) {
                        if (view.getId() == R.id.iv_point_list_delete) {
                            tempMeaTotalList.remove(position);
                            if (meaTotalList.size() <= position) {
                                clear();
                                latLngList.clear();
                            } else {
                                meaTotalList.remove(position);
                            }
                            mTotalAdapter.notifyDataSetChanged();
                            layerManager.updateTotal();
                        }
                    }
                });
                bubbleView.setBubbleView(content);
                int gravity;
                if (((View) (meaView.getParent())).getHeight() - meaView.getY() < ZXSystemUtil.dp2px(240)) {
                    gravity = Gravity.TOP;
                } else {
                    gravity = Gravity.BOTTOM;
                }
                bubbleView.setFocusable(false);
                bubbleView.setOutsideTouchable(false);
                bubbleView.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);//解决不压键盘
                if (bubbleView.isShowing()) bubbleView.dismiss();
                else bubbleView.show(view, gravity);
                break;
            case "输入":
                meaView.showMeasureMsg(null, MeasureHelper.SECOND_TYPE_INPUT);
                break;
            case "手绘":
                clear();
                DragViewManager.INSTANCE.open(context, mapView, DragDrawView.DrawType.HANDPOINT, geotype == GEO_TYPE_Polyline, new Function1<List<? extends LatLng>, Unit>() {
                    @Override
                    public Unit invoke(List<? extends LatLng> latLngs) {
                        clear();
                        drawLatlngs.clear();
                        drawLatlngs.addAll((List<LatLng>) latLngs);
//                        optManager.optMultiAdd((List<LatLng>) latLngs);
                        return null;
                    }
                });
                meaView.showMeasureMsg(null, MeasureHelper.SECOND_TYPE_HANDPOINT);
                break;
            case "圆形":
                clear();
                DragViewManager.INSTANCE.open(context, mapView, DragDrawView.DrawType.CIRCLE, geotype == GEO_TYPE_Polyline, new Function1<List<? extends LatLng>, Unit>() {
                    @Override
                    public Unit invoke(List<? extends LatLng> latLngs) {
                        clear();
                        drawLatlngs.clear();
                        drawLatlngs.addAll((List<LatLng>) latLngs);
                        return null;
                    }
                });
                meaView.showMeasureMsg(null, MeasureHelper.SECOND_TYPE_CIRCLE);
                break;
            case "矩形":
                clear();
                DragViewManager.INSTANCE.open(context, mapView, DragDrawView.DrawType.RECTANGLE, geotype == GEO_TYPE_Polyline, new Function1<List<? extends LatLng>, Unit>() {
                    @Override
                    public Unit invoke(List<? extends LatLng> latLngs) {
                        clear();
                        drawLatlngs.clear();
                        drawLatlngs.addAll((List<LatLng>) latLngs);
//                        optManager.optMultiAdd((List<LatLng>) latLngs);
                        return null;
                    }
                });
                meaView.showMeasureMsg(null, MeasureHelper.SECOND_TYPE_RECTANGLE);
                break;
            case "光标":
                if (locationFuncView == null) {
                    locationFuncView = new LocationFuncView(context).setCoordinateListener(new LocationFuncView.CoordinateListener() {
                        @Override
                        public void coordinate(LatLng location) {
                            meaView.showMeasureMsg(location, MeasureHelper.SECOND_TYPE_LOCATION);
                            MeasureHelper.this.locationPoint = location;
                        }
                    }).bindMapView(mapView);
                } else {
                    meaView.showMeasureMsg(locationPoint, MeasureHelper.SECOND_TYPE_LOCATION);
                }
                break;
            case "GPS":
                meaView.showMeasureMsg(null, MeasureHelper.SECOND_TYPE_GPS);
//                if (zxMap != null) {
//                    Location location = zxMap.getMyLocation();
//                    if (location != null) {
//                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                        getOptManager().optAdd(latLng);
////                        meaView.showMeasureMsg(locationPoint, MeasureHelper.SECOND_TYPE_GPS);
//                    }
//                }
//                if (locationFuncView == null) {
//                    locationFuncView = new LocationFuncView(context).setCoordinateListener(new LocationFuncView.CoordinateListener() {
//                        @Override
//                        public void coordinate(LatLng location) {
//                            meaView.showMeasureMsg(location, MeasureHelper.SECOND_TYPE_LOCATION);
//                            MeasureHelper.this.locationPoint = location;
//                        }
//                    }).bindMapView(mapView);
//                } else {
//                    meaView.showMeasureMsg(locationPoint, MeasureHelper.SECOND_TYPE_LOCATION);
//                }
                break;
            case "下一要素":
                if ((getGeoType() == GEO_TYPE_Polyline && latLngList.size() > 1) || (getGeoType() == GEO_TYPE_Polygon && latLngList.size() > 2)) {
                    List<LatLng> mLatlngs = new ArrayList<>();
                    mLatlngs.addAll(latLngList);
                    meaTotalList.add(mLatlngs);
                    editLatlngIndex = meaTotalList.size();
                    clear();
                    updateSource(true);
                    layerManager.showPoint = true;
                } else {
                    ZXToastUtil.showToast("当前要素尚未绘制完毕");
                }
                break;
        }
    }

    /**
     * 清除
     */
    public void clear() {
        optManager.clear();
        layerManager.clear();
        stopTranslate();
        editPoint = -1;
        moveLatlng = null;
        longClickPoint = null;
        optManager.popMenuLatlng = null;
        clickFeature = null;
        optManager.closeMarker();
        removeLocationFunc();
    }

    private void removeLocationFunc() {
        if (locationFuncView != null) {
            locationFuncView.removeView();
            locationFuncView.removListener();
            locationFuncView = null;
        }
    }

    public void startMeasure() {
        layerManager.showPoint = true;
        zxMap.setOnMapClickListener(new MyMapClickListener());
        zxMap.setOnMapLongClickListener(new MyMapLongClickListener());
        mapView.setOnTouchListener(new MyTouchListener());
    }

    public void exit() {
        clear();
        DragViewManager.INSTANCE.exit();
        layerManager.removeLayer();
        optManager.closeMarker();
        mapView.setOnTouchListener(null);
        zxMap.setOnMapLongClickListener(null);
        if (bubbleView != null) bubbleView.dismiss();
        if (locationFuncView != null) locationFuncView.removeView();
    }

    public void setMeaStatusListener(MeaStatusListener statusListener) {
        this.statusListener = statusListener;
    }

    private class MyMapLongClickListener implements ZXMap.OnMapLongClickListener {

        @Override
        public void onMapLongClick(@NonNull final LatLng latLng) {
            if (!getLayerManager().showPoint) {
                ZXToastUtil.showToast("当前图形不支持编辑，请切换至下一要素");
                return;
            }
            PointF screenPoint = zxMap.getProjection().toScreenLocation(latLng);
            RectF rectF = new RectF(screenPoint.x - 15, screenPoint.y - 15, screenPoint.x + 15, screenPoint.y + 15);
            List<Feature> features;
            if (isMeaTranslate) {
                //平移时，只允许对面进行长按
                features = zxMap.queryRenderedFeatures(rectF, MeaLayerManager.Fill_Layer_Name);
            } else {
                features = zxMap.queryRenderedFeatures(rectF, MeaLayerManager.Line_Layer_Name, MeaLayerManager.Fill_Layer_Name);
            }
            if (features.size() > 0) {
                isMapLongClick = true;
                clickFeature = features.get(features.size() - 1);
                if (clickFeature.getGeometry() instanceof LineString || clickFeature.getGeometry() instanceof MultiLineString) {
                    if (clickFeature.getGeometry() instanceof LineString) {
                        List<Position> positions = ((LineString) clickFeature.getGeometry()).getCoordinates();
                        optManager.popMenuLatlng = MeaUtil.getProjectivePoint(new LatLng(positions.get(0).getLatitude(), positions.get(0).getLongitude()), new LatLng(positions.get(1).getLatitude(), positions.get(1).getLongitude()), latLng);
                        editPoint = clickFeature.getNumberProperty("lineIndex").intValue() + 1;
                        optManager.funcIndex = editPoint;
                    }
                } else if (clickFeature.getGeometry() instanceof Polygon || clickFeature.getGeometry() instanceof MultiPolygon) {
                    optManager.popMenuLatlng = latLng;
                }
                handler.sendEmptyMessage(0);
            }
        }
    }

    private class MyMapClickListener implements ZXMap.OnMapClickListener {

        @Override
        public void onMapClick(@NonNull LatLng latLng) {
            if (!getLayerManager().showPoint) {
                ZXToastUtil.showToast("当前图形不支持编辑，请切换至下一要素");
                return;
            }
            //如果已开启弹窗，只关闭弹窗，不添加点
            if (optManager.closeMarker()) {
                return;
            }
            //平移不允许添加点
            if (isMeaTranslate) {
                return;
            }
            //添加上一步操作序列
            optManager.optAdd(latLng);

            onMapReAdd();
        }
    }

    private class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isLongClick = false;
                    PointF screenPoint = new PointF(event.getX(), event.getY());
                    RectF rectF = new RectF(screenPoint.x - 15, screenPoint.y - 15, screenPoint.x + 15, screenPoint.y + 15);
                    List<Feature> features;
                    if (isMeaTranslate) {
                        //平移式不允许对点进行操作,只允许操作面图层
                        features = zxMap.queryRenderedFeatures(rectF, MeaLayerManager.Fill_Layer_Name);
                    } else {
                        features = zxMap.queryRenderedFeatures(rectF, MeaLayerManager.Point_Layer_Name);
                    }
                    if (features.size() > 0) {
                        longClickPoint = new PointF(event.getX(), event.getY());
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(0);
                                isLongClick = true;
                            }
                        }, 700);
                        clickFeature = features.get(features.size() - 1);
                        if (clickFeature.getGeometry() instanceof Point || clickFeature.getGeometry() instanceof MultiPoint) {
                            editPoint = clickFeature.getNumberProperty("pointIndex").intValue();
                            optManager.funcIndex = editPoint;
                            moveLatlng = latLngList.get(editPoint);
                            optManager.popMenuLatlng = moveLatlng;
                        } else if (clickFeature.getGeometry() instanceof Polygon || clickFeature.getGeometry() instanceof MultiPolygon) {
                            transStartLatlng = zxMap.getProjection().fromScreenLocation(screenPoint);
                            optManager.popMenuLatlng = zxMap.getProjection().fromScreenLocation(screenPoint);
                            for (int i = 0; i < latLngList.size(); i++) {
                                tempLatlngs.add(new LatLng(latLngList.get(i)));
                            }
                        }
                        return true;
                    } else {
                        editPoint = -1;
                        optManager.popMenuLatlng = null;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    //计算移动距离
                    if (timer != null && longClickPoint != null) {
                        double movex = Math.sqrt(Math.pow(Math.abs(event.getX() - longClickPoint.x), 2) + Math.pow(Math.abs(event.getY() - longClickPoint.y), 2));
                        if (movex > 20) {
                            isLongClick = false;
                            timer.cancel();
                            timer = null;
                        }
                    }
                    //地图平移事件
                    if (isMeaTranslate) {
                        if (isLongClick) {
                            return true;
                        }
                        if (transStartLatlng != null) {
                            if (tempLatlngs.isEmpty()) {
                                return true;
                            }
                            LatLng resultLatlng = zxMap.getProjection().fromScreenLocation(new PointF(event.getX(), event.getY()));
                            double transLon = resultLatlng.getLongitude() - transStartLatlng.getLongitude();
                            double transLat = resultLatlng.getLatitude() - transStartLatlng.getLatitude();
                            for (int i = 0; i < latLngList.size(); i++) {
                                latLngList.get(i).setLongitude(tempLatlngs.get(i).getLongitude() + transLon);
                                latLngList.get(i).setLatitude(tempLatlngs.get(i).getLatitude() + transLat);
                            }
                            updateSource(false);
                            return true;
                        }
                        return false;
                    }
                    //地图长按事件
                    if (isMapLongClick) {
                        return false;
                    }
                    //如果为长按，将不再执行移动的操作
                    if (isLongClick) {
                        return true;
                    }
                    if (editPoint != -1 && moveLatlng != null) {
                        LatLng touchPoint = zxMap.getProjection().fromScreenLocation(new PointF(event.getX(), event.getY()));
                        latLngList.set(editPoint, touchPoint);
                        updateSource(false);
                        optManager.closeMarker();
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    //平移事件
                    if (isMeaTranslate) {
                        if (transStartLatlng != null && !tempLatlngs.isEmpty()) {
                            optManager.optTrans(transStartLatlng, zxMap.getProjection().fromScreenLocation(new PointF(event.getX(), event.getY())));
                            tempLatlngs.clear();
                            transStartLatlng = null;
                            return true;
                        } else {
                            transStartLatlng = null;
                            return false;
                        }
                    }
                    //长按事件
                    isLongClick = false;
                    if (isMapLongClick) {
                        isMapLongClick = false;
                        //非touch移动
                        return false;
                    }
                    if (editPoint != -1) {
                        optManager.optMove(editPoint, moveLatlng, latLngList.get(editPoint));
                        editPoint = -1;
                        onMapReAdd();
                    }
                    moveLatlng = null;
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//showMenu
                tempLatlngs.clear();
                ((Vibrator) meaView.getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(200);
                //执行长按事件-弹出操作菜单
                optManager.showMeaMenu(clickFeature);
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        }
    };

    @Override
    public void updateSource(boolean updateTotal) {
        if (updateTotal) layerManager.updateTotal();
        layerManager.updateSource();
    }

    @Override
    public void onMapReAdd() {
        if (statusListener != null) {
            statusListener.onMapReAdd();
        }
    }

    @Override
    public MeaOptManager getOptManager() {
        return optManager;
    }

    @Override
    public MeaLayerManager getLayerManager() {
        return layerManager;
    }

    @Override
    public void onLayerAdd(String name, Layer layer) {
        if (statusListener != null) {
            statusListener.onLayerAdd(name, layer);
        }
    }

    @Override
    public void onLayerRemove(String name) {
        if (statusListener != null) {
            statusListener.onLayerRemove(name);
        }
    }

    @Override
    public void onPointChange() {
        if (statusListener != null) {
            statusListener.onPointChange();
//            if (tempMeaTotalList.size() > 0) {
//                tempMeaTotalList.remove(tempMeaTotalList.size() - 1);
//            }
            tempMeaTotalList.clear();
            tempMeaTotalList.addAll(meaTotalList);
            if (latLngList.size() > 0) tempMeaTotalList.add(latLngList);
            if (mTotalAdapter != null) mTotalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getGeoType() {
        return geotype;
    }

    @Override
    public List<LatLng> getLatlngs() {
        return latLngList;
    }

    @Override
    public List<List<LatLng>> getTotalList() {
        return meaTotalList;
    }

    @Override
    public int getEditLatlngIndex() {
        return editLatlngIndex;
    }

    @Override
    public void startTranslate() {
        isMeaTranslate = true;
        layerManager.startTranslate();
    }

    @Override
    public void stopTranslate() {
        if (isMeaTranslate) {
            isMeaTranslate = false;
            layerManager.stopTranslate();
        }
    }

    @Override
    public boolean isTranslate() {
        return isMeaTranslate;
    }
}
