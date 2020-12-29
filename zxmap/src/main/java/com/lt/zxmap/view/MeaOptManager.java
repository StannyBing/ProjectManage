package com.lt.zxmap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lt.zxmap.R;
import com.zxmap.zxmapsdk.annotations.IconFactory;
import com.zxmap.zxmapsdk.annotations.Marker;
import com.zxmap.zxmapsdk.annotations.MarkerViewOptions;
import com.zxmap.zxmapsdk.geojson.Feature;
import com.zxmap.zxmapsdk.geojson.LineString;
import com.zxmap.zxmapsdk.geojson.MultiLineString;
import com.zxmap.zxmapsdk.geojson.MultiPoint;
import com.zxmap.zxmapsdk.geojson.MultiPolygon;
import com.zxmap.zxmapsdk.geojson.Point;
import com.zxmap.zxmapsdk.geojson.Polygon;
import com.zxmap.zxmapsdk.geometry.LatLng;
import com.zxmap.zxmapsdk.maps.ZXMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiangb on 2019/9/27.
 * 功能：
 */
public class MeaOptManager {

    private Context context;
    private ZXMap zxMap;

    private List<OptBean> optUndoList = new ArrayList<>();//上一步的操作集合
    private List<OptBean> optRedoList = new ArrayList<>();//下一步的操作集合

    private MarkerViewOptions markerOptions;//标记点
    public LatLng popMenuLatlng;//弹出窗口时的定位点
    public int funcIndex = -1;//操作点

    private MeaListener meaListener;

    MeaOptManager(Context context, ZXMap zxMap, MeaListener meaListener) {
        this.context = context;
        this.zxMap = zxMap;
        this.meaListener = meaListener;
    }

    /**
     * 操作实体类
     */
    class OptBean {
        private static final int ADD = 1;
        private static final int MOVE = 2;
        private static final int INSERT = 3;
        private static final int DELETE = 4;
        private static final int TRANS = 5;
        private static final int MultiAdd = 6;

        private int optType;

        private LatLng pointAdd;
        private LatLng pointMoveFrom;
        private LatLng pointMoveTo;
        private LatLng pointInsert;
        private LatLng pointDelete;
        private LatLng pointTransStart;
        private LatLng pointTransEnd;
        private List<LatLng> pointsMulti = new ArrayList<>();

        private int moveIndex;
        private int insertIndex;
        private int deleteIndex;
    }

    //添加操作
    public void optAdd(LatLng latlng) {
        OptBean bean = new OptBean();
        bean.optType = OptBean.ADD;
        bean.pointAdd = latlng;
        optUndoList.add(bean);
        doOpt(true, bean, false);
        //清空下一步操作序列
        optRedoList.clear();
        meaListener.onMapReAdd();
    }

    //移动操作
    public void optMove(int moveIndex, LatLng latlngFrom, LatLng latlngTo) {
        OptBean bean = new OptBean();
        bean.optType = OptBean.MOVE;
        bean.pointMoveFrom = latlngFrom;
        bean.pointMoveTo = latlngTo;
        bean.moveIndex = moveIndex;
        optUndoList.add(bean);
        //清空下一步操作序列
        optRedoList.clear();
        meaListener.onMapReAdd();
    }

    //插入操作
    public void optInsert(LatLng latlng, int index) {
        OptBean bean = new OptBean();
        bean.optType = OptBean.INSERT;
        bean.pointInsert = latlng;
        bean.insertIndex = index;
        optUndoList.add(bean);
        doOpt(true, bean, false);
        //清空下一步操作序列
        optRedoList.clear();
        meaListener.onMapReAdd();
    }

    //删除操作
    public void optDelete(LatLng latlng, int index) {
        OptBean bean = new OptBean();
        bean.optType = OptBean.DELETE;
        bean.pointDelete = latlng;
        bean.deleteIndex = index;
        optUndoList.add(bean);
        doOpt(true, bean, false);
        //清空下一步操作序列
        optRedoList.clear();
        meaListener.onMapReAdd();
    }

    //平移操作
    public void optTrans(LatLng transStart, LatLng transEnd) {
        OptBean bean = new OptBean();
        bean.optType = OptBean.TRANS;
        bean.pointTransStart = transStart;
        bean.pointTransEnd = transEnd;
        optUndoList.add(bean);
        //清空下一步操作序列
        optRedoList.clear();
        meaListener.onMapReAdd();
    }

    //多点添加
    public void optMultiAdd(List<LatLng> latLngs) {
        OptBean bean = new OptBean();
        bean.optType = OptBean.MultiAdd;
        bean.pointsMulti.addAll(latLngs);
        optUndoList.add(bean);
        //清空下一步操作序列
        doOpt(true, bean, false);
        optRedoList.clear();
        meaListener.onMapReAdd();
    }

    /**
     * 上一步
     *
     * @return 是否已为最后一步
     */
    public boolean postUndo() {
        meaListener.stopTranslate();
        if (optUndoList.size() > 0) {
            doOpt(false, optUndoList.get(optUndoList.size() - 1));
            return optUndoList.size() == 0;
        }
        return false;
    }

    /**
     * 下一步
     *
     * @return 是否已为最后一步
     */
    public boolean postRedo() {
        meaListener.stopTranslate();
        if (optRedoList.size() > 0) {
            doOpt(true, optRedoList.get(optRedoList.size() - 1));
            return optRedoList.size() == 0;
        }
        return false;
    }

    /**
     * 清除
     */
    public void clear() {
        optUndoList.clear();
        optRedoList.clear();
    }

    private void doOpt(boolean isRedo, OptBean bean) {
        doOpt(isRedo, bean, true);
    }

    /**
     * 执行操作
     *
     * @param isRedo 是否为下一步操作，是则正向执行，否则反向执行
     * @param bean
     * @param isPost 是否是通过postRedo或者postUndo执行的
     */
    private void doOpt(boolean isRedo, OptBean bean, boolean isPost) {
        switch (bean.optType) {
            case OptBean.ADD:
                if (isRedo) {
                    meaListener.getLatlngs().add(bean.pointAdd);
                } else {
                    MeaUtil.removeLast(meaListener.getLatlngs(), bean.pointAdd);
//                    meaListener.getLatlngs().remove(bean.pointAdd);
                }
                break;
            case OptBean.MOVE:
                if (isRedo) {
                    meaListener.getLatlngs().set(bean.moveIndex, bean.pointMoveTo);
                } else {
                    meaListener.getLatlngs().set(bean.moveIndex, bean.pointMoveFrom);
                }
                break;
            case OptBean.INSERT:
                if (isRedo) {
                    meaListener.getLatlngs().add(bean.insertIndex, bean.pointInsert);
                } else {
                    meaListener.getLatlngs().remove(bean.insertIndex);
                }
                break;
            case OptBean.DELETE:
                if (isRedo) {
                    meaListener.getLatlngs().remove(bean.deleteIndex);
                } else {
                    meaListener.getLatlngs().add(bean.deleteIndex, bean.pointDelete);
                }
                break;
            case OptBean.TRANS:
                double transLon = bean.pointTransEnd.getLongitude() - bean.pointTransStart.getLongitude();
                double transLat = bean.pointTransEnd.getLatitude() - bean.pointTransStart.getLatitude();
                if (isRedo) {
                    for (int i = 0; i < meaListener.getLatlngs().size(); i++) {
                        meaListener.getLatlngs().get(i).setLongitude(meaListener.getLatlngs().get(i).getLongitude() + transLon);
                        meaListener.getLatlngs().get(i).setLatitude(meaListener.getLatlngs().get(i).getLatitude() + transLat);
                    }
                } else {
                    for (int i = 0; i < meaListener.getLatlngs().size(); i++) {
                        meaListener.getLatlngs().get(i).setLongitude(meaListener.getLatlngs().get(i).getLongitude() - transLon);
                        meaListener.getLatlngs().get(i).setLatitude(meaListener.getLatlngs().get(i).getLatitude() - transLat);
                    }
                }
                break;
            case OptBean.MultiAdd:
                if (isRedo) {
                    meaListener.getLatlngs().addAll(bean.pointsMulti);
                } else {
                    meaListener.getLatlngs().clear();
                }
                break;
            default:
                break;
        }
        meaListener.updateSource(false);
        if (isPost) {
            if (isRedo) {
                optRedoList.remove(bean);
                optUndoList.add(bean);
            } else {
                optRedoList.add(bean);
                optUndoList.remove(bean);
            }
        }
    }


    /**
     * 执行长按事件-弹出操作菜单
     */
    public void showMeaMenu(final Feature clickFeature) {
        closeMarker();
        zxMap.setAllowConcurrentMultipleOpenInfoWindows(false);
        zxMap.setInfoWindowAdapter(new ZXMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                View view = LayoutInflater.from(context).inflate(R.layout.map_meamenu_layout, null, false);
                LinearLayout llMenu = view.findViewById(R.id.ll_mea_menu);
                List<String> funcString = new ArrayList<>();
                if (clickFeature.getGeometry() instanceof Point || clickFeature.getGeometry() instanceof MultiPoint) {
                    funcString.add("删除");
                } else if (clickFeature.getGeometry() instanceof LineString || clickFeature.getGeometry() instanceof MultiLineString) {
                    funcString.add("插入");
//                    funcString.add("裁切");
                } else if (clickFeature.getGeometry() instanceof Polygon || clickFeature.getGeometry() instanceof MultiPolygon) {
                    if (!meaListener.isTranslate()) {
                        funcString.add("平移");
//                    funcString.add("旋转");
//                    funcString.add("裁切");
                    } else {
                        funcString.add("结束平移");
                    }
                }
                if (funcString.size() > 0) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, MeaUtil.dp2px(context, 30));
                    params.gravity = Gravity.CENTER;
                    for (int i = 0; i < funcString.size(); i++) {
                        TextView tv = new TextView(context);
                        tv.setText(funcString.get(i));
                        tv.setTextColor(Color.WHITE);
                        tv.setTextSize(12);
                        tv.setGravity(Gravity.CENTER);
                        tv.setMinWidth(MeaUtil.dp2px(context, 30));
                        tv.setLayoutParams(params);
                        llMenu.addView(tv);
                        tv.setTag(funcString.get(i));
                        tv.setOnClickListener(new MenuClickListener());
                        if (funcString.size() > 1 && i < funcString.size() - 1) {
                            View divider = new View(context);
                            divider.setLayoutParams(new LinearLayout.LayoutParams(MeaUtil.dp2px(context, 1), MeaUtil.dp2px(context, 20)));
                            divider.setBackgroundColor(Color.WHITE);
                            llMenu.addView(divider);
                        }
                    }
                }

                return view;
            }
        });
        if (popMenuLatlng != null) {
            markerOptions = new MarkerViewOptions()
                    .position(popMenuLatlng)
                    .icon(IconFactory.getInstance(context)
                            .fromBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)))
                    .alpha(0.0f);
            zxMap.addMarker(markerOptions);
            zxMap.selectMarker(markerOptions.getMarker());
        }
    }

    //关闭弹框
    public boolean closeMarker() {
        if (markerOptions != null && markerOptions.getMarker() != null) {
            markerOptions.getMarker().hideInfoWindow();
            markerOptions.getMarker().remove();
            markerOptions = null;
            zxMap.removeAnnotations();
            return true;
        } else {
            return false;
        }
    }

    //菜单点击事件
    private class MenuClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getTag().toString()) {
                case "删除":
                    if (funcIndex != -1) {
                        optDelete(popMenuLatlng, funcIndex);
                    }
                    break;
                case "插入":
                    if (funcIndex != -1) {
                        optInsert(popMenuLatlng, funcIndex);
                    }
                    break;
                case "裁切":

                    break;
                case "平移":
                    meaListener.startTranslate();
                    break;
                case "结束平移":
                    meaListener.stopTranslate();
                    break;
                case "旋转":

                    break;
            }
            closeMarker();
        }
    }

}
