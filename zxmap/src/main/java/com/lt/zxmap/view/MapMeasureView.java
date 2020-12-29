package com.lt.zxmap.view;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lt.zxmap.tool.LocationEditTool;
import com.lt.zxmap.view.BubbleView.MBubbleView;
import com.lt.zxmap.R;
import com.lt.zxmap.bean.CalculateArea;
import com.lt.zxmap.view.dragView.DragViewManager;
import com.zx.zxutils.other.QuickAdapter.ZXQuickAdapter;
import com.zx.zxutils.util.ZXLocationUtil;
import com.zx.zxutils.util.ZXPermissionUtil;
import com.zx.zxutils.util.ZXSystemUtil;
import com.zx.zxutils.util.ZXToastUtil;
import com.zxmap.zxmapsdk.geometry.LatLng;
import com.zxmap.zxmapsdk.maps.MapView;
import com.zxmap.zxmapsdk.maps.ZXMap;
import com.zxmap.zxmapsdk.style.layers.Layer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.lt.zxmap.view.MeasureHelper.GEO_TYPE_Polygon;
import static com.lt.zxmap.view.MeasureHelper.GEO_TYPE_Polyline;

/**
 * Created by Xiangb on 2017/4/21.
 * 功能：地图测量工具
 */

public class MapMeasureView extends LinearLayout implements View.OnClickListener {

    public Context context;
    public CardView meaLayout;//整体界面
    public RadioGroup rgMeasure;
    public RadioButton rbLine;//线
    public RadioButton rbPoly;//面
    public LinearLayout llResult;//结果
    public ImageView ivUndo;// 返回上一步按钮
    public ImageView ivRedo;// 退回下一步按钮
    public ImageView ivDelete;// 删除按钮
    public ImageView ivShape;// 形状按钮
    public ImageView ivTools;//工具箱开关
    private LinearLayout measureSecondFuncLl;//二级信息
    private FrameLayout flToolContent;//工具箱
    private TextView tvToolTips, tvToolLocationInfo, tvToolAdd, tvToolAddPoint;
    private ImageView ivToolBack;
    private EditText etToolLon, etToolLat;
    private LinearLayout locationLl;
    public TextView tvCount;
    public TextView tvUnit;
    public TextView tvExit;
    public View view1, view2, view3, view4, view5, view6;
    public RecyclerView rvTools;
    public TextView tvSubmit;//完成按钮
    public boolean isOpen = false;
    public MeasureType measureType;

    public boolean isCanNext = true;

    public List<MeaToolBean> toolList = new ArrayList<>();
    public MeaToolAdapter toolAdapter = new MeaToolAdapter(toolList);

    public ZXMap.OnMapClickListener defaultListener;//地图默认点击事件
    public MapView mapView;//地图默认点击事件
    public double size;//大小
    public ZXMap zxMap;

    private MBubbleView shapeBubble;

    private Timer timer = new Timer();

    public OnStatisticsCallBack statisticsCallBack;
    public OnMeasureCloseListener closeListener;
    public OnLayerChangeListener layerChangeListener;
    public MeasureHelper helper;

    public enum MeasureType {
        Measure,
        Statistics,
        Module
    }

    public MapMeasureView(Context context) {
        this(context, null);
        this.context = context;
    }

    public MapMeasureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;

    }

    public MapMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void init(ZXMap.OnMapClickListener defaultListener, MapView mapView) {
        this.mapView = mapView;
        //在构造函数中将xml中定义的布局解析出来
        LayoutInflater.from(context).inflate(R.layout.map_measure_layout, this, true);
        meaLayout = findViewById(R.id.mea_layout);
        measureSecondFuncLl = findViewById(R.id.measureSecondFuncLl);
        flToolContent = findViewById(R.id.fl_tool_content);
        rgMeasure = findViewById(R.id.mea_rg);
        rbLine = findViewById(R.id.mea_line);
        rbPoly = findViewById(R.id.mea_poly);
        ivUndo = findViewById(R.id.mea_undo);
        ivRedo = findViewById(R.id.mea_redo);
        ivDelete = findViewById(R.id.mea_delete);
        tvCount = findViewById(R.id.mea_count);
        tvSubmit = findViewById(R.id.mea_submit);
        llResult = findViewById(R.id.mea_result);
        ivShape = findViewById(R.id.mea_shape);
        ivTools = findViewById(R.id.mea_tools);
        tvUnit = findViewById(R.id.mea_unit);
        tvExit = findViewById(R.id.mea_exit);
        view1 = findViewById(R.id.mea_view1);
        view2 = findViewById(R.id.mea_view2);
        view3 = findViewById(R.id.mea_view3);
        view4 = findViewById(R.id.mea_view4);
        view5 = findViewById(R.id.mea_view5);
        view6 = findViewById(R.id.mea_view6);
        rvTools = findViewById(R.id.rv_mea_tools);
        tvExit.setOnClickListener(this);
        ivRedo.setOnClickListener(this);
        ivUndo.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        ivShape.setOnClickListener(this);
        ivTools.setOnClickListener(this);
        toolList.add(new MeaToolBean("记录", R.drawable.icon_mea_list));
        toolList.add(new MeaToolBean("输入", R.drawable.icon_mea_input));
        toolList.add(new MeaToolBean("手绘", R.drawable.icon_mea_draw));
        toolList.add(new MeaToolBean("圆形", R.drawable.icon_mea_circle));
        toolList.add(new MeaToolBean("矩形", R.drawable.icon_mea_rectangle));
        toolList.add(new MeaToolBean("光标", R.drawable.icon_mea_center));
        toolList.add(new MeaToolBean("GPS", R.drawable.icon_mea_gps));
        toolList.add(new MeaToolBean("下一要素", R.drawable.icon_mea_next));
        rvTools.setLayoutManager(new GridLayoutManager(context, toolList.size()));
        rvTools.setAdapter(toolAdapter);
        toolAdapter.setOnItemClickListener(new ZXQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ZXQuickAdapter adapter, View view, int position) {
                if (!isCanNext && toolList.get(position).getName().equals("下一要素")) {
                    ZXToastUtil.showToast("当前模式不支持");
                } else {
                    helper.postTool(view, toolList.get(position).getName());
                }
            }
        });
        rgMeasure.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                if (id == R.id.mea_line) {
                    helper.setGeoType(GEO_TYPE_Polyline);
                    delete(true);
                    tvCount.setText("0");
                    tvUnit.setText("米");
                    rbLine.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
                    rbPoly.setTextColor(ContextCompat.getColor(context, R.color.white));
//                    ivShape.setVisibility(View.GONE);
                } else if (id == R.id.mea_poly) {
                    helper.setGeoType(GEO_TYPE_Polygon);
                    delete(true);
                    tvCount.setText("0");
                    tvUnit.setText("平方米");
                    rbLine.setTextColor(ContextCompat.getColor(context, R.color.white));
                    rbPoly.setTextColor(ContextCompat.getColor(context, R.color.mainColor));
//                    ivShape.setVisibility(View.VISIBLE);
                }
            }
        });
        rgMeasure.check(R.id.mea_line);

        ivDelete.setImageAlpha(100);

        this.defaultListener = defaultListener;
        this.zxMap = mapView.getZXMap();

        initMeasure();

        setVisibility(View.GONE);
    }

    public void setCloseListener(OnMeasureCloseListener closeListener) {
        this.closeListener = closeListener;
    }

    /**
     * 获取总点集
     *
     * @return
     */
    public List<List<LatLng>> getTotalList() {
        return helper.meaTotalList;
    }

    public List<List<LatLng>> getTotalList(boolean endDraw) {
        if (endDraw) {
            if ((helper.getGeoType() == GEO_TYPE_Polyline && helper.latLngList.size() > 1) || (helper.getGeoType() == GEO_TYPE_Polygon && helper.latLngList.size() > 2)) {
                helper.postTool(null, "下一要素");
            }
        }
        return getTotalList();
    }


    /**
     * 打开测量
     */
    public void showMeasure() {
        setVisibility(View.VISIBLE);
        view3.setVisibility(GONE);
        view4.setVisibility(GONE);
        ivRedo.setVisibility(VISIBLE);
        ivUndo.setVisibility(VISIBLE);

        view1.setVisibility(VISIBLE);
        view2.setVisibility(VISIBLE);
        llResult.setVisibility(VISIBLE);
        rbLine.setVisibility(VISIBLE);
        rbPoly.setVisibility(VISIBLE);

        measureType = MeasureType.Measure;

        setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
        isOpen = true;
        isCanNext = true;

        helper.startMeasure();

        ivTools.setSelected(false);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flToolContent.getLayoutParams();
        params.topMargin = -ZXSystemUtil.dp2px(40);
        flToolContent.setLayoutParams(params);
        secondHandler.sendEmptyMessage(4);
    }

    /**
     * 打开统计
     */
    public void showStatistics(OnStatisticsCallBack statisticsCallBack) {
        setVisibility(View.VISIBLE);
        view3.setVisibility(VISIBLE);
        view4.setVisibility(VISIBLE);
        ivRedo.setVisibility(VISIBLE);
        ivUndo.setVisibility(VISIBLE);

        view1.setVisibility(GONE);
        view2.setVisibility(GONE);
        llResult.setVisibility(GONE);
        rbLine.setVisibility(GONE);
        rbPoly.setVisibility(GONE);

        measureType = MeasureType.Statistics;
        this.statisticsCallBack = statisticsCallBack;

        isOpen = true;
        initMeasure();

        setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));

        helper.startMeasure();
    }

    /**
     * 初始化
     */
    private void initMeasure() {
        ivRedo.setBackgroundResource(R.mipmap.recover_off);
        ivUndo.setBackgroundResource(R.mipmap.goback_off);
        setVisibility(View.VISIBLE);
//        isOpen = true;
        if (helper != null) {
            helper.clear();
        }

        helper = MeasureHelper.bind(this, mapView);
        helper.setMeaStatusListener(new MeaStatusListener() {
            @Override
            public void onMapReAdd() {
                ivUndo.setBackgroundResource(R.mipmap.goback_on);
                ivRedo.setBackgroundResource(R.mipmap.recover_off);
                ivDelete.setImageAlpha(255);
            }

            @Override
            public void onPointChange() {
                //计算
                size = 0;
                if (helper.geotype == GEO_TYPE_Polyline) {//线
                    for (int i = 0; i < helper.latLngList.size() - 1; i++) {
                        size += helper.latLngList.get(i).distanceTo(helper.latLngList.get(i + 1));
                    }
                    if (size < 1000) {
                        tvCount.setText(formatDouble(size));
                        tvUnit.setText("米");
                    } else {
                        tvCount.setText(formatDouble(size / 1000));
                        tvUnit.setText("公里");
                    }
                } else {//面
                    size = CalculateArea.getArea(helper.latLngList);
                    if (size < 1000000) {
                        tvCount.setText(formatDouble(size));
                        tvUnit.setText("平方米");
                    } else {
                        tvCount.setText(formatDouble(size / 1000000));
                        tvUnit.setText("平方公里");
                    }
                }
//                helper.startTranslate();
            }

            @Override
            public void onLayerAdd(String id, Layer layer) {
                if (layerChangeListener != null) {
                    layerChangeListener.onLayerAdd(id, layer);
                }
            }

            @Override
            public void onLayerRemove(String id) {
                if (layerChangeListener != null) {
                    layerChangeListener.onLayerRemove(id);
                }
            }
        });
    }


    public void showMeasureMsg(LatLng coordinate, int type) {
//        measureSecondFuncLl.setVisibility(View.VISIBLE);
        final int mType = type;
        if (measureSecondFuncLl.getVisibility() == View.GONE) {
            measureSecondFuncLl.removeAllViews();
            View view = LayoutInflater.from(context).inflate(R.layout.layout_measure_tool_msg, null);
            ivToolBack = view.findViewById(R.id.iv_tool_back);
            tvToolTips = view.findViewById(R.id.tv_tool_tips);
            tvToolLocationInfo = view.findViewById(R.id.tv_tool_locationinfo);
            tvToolAdd = view.findViewById(R.id.tv_tool_add);
            tvToolAddPoint = view.findViewById(R.id.tv_tool_add_point);
            etToolLon = view.findViewById(R.id.et_tool_longitude);
            etToolLat = view.findViewById(R.id.et_tool_latitude);
            locationLl = view.findViewById(R.id.locationLl);
            measureSecondFuncLl.addView(view);
            measureSecondFuncLl.setAlpha(0);
            secondHandler.sendEmptyMessage(3);
            ivToolBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    marginHandler.sendEmptyMessage(2);
                    helper.getLayerManager().showPoint = true;
                    secondHandler.sendEmptyMessage(4);
                    DragViewManager.INSTANCE.exit();
                    helper.drawLatlngs.clear();
                    if (helper.locationFuncView != null) {
                        helper.locationFuncView.removeView();
                        helper.locationFuncView = null;
                    }
                }
            });
            tvToolAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    DragViewManager.INSTANCE.exit();
                    if (mType == MeasureHelper.SECOND_TYPE_INPUT) {
                        LatLng latLng = LocationEditTool.INSTANCE.checkLocation(etToolLon.getText().toString(), etToolLat.getText().toString());
                        if (latLng != null) {
                            helper.getOptManager().optAdd(latLng);
                        }
                    } else if (mType == MeasureHelper.SECOND_TYPE_CIRCLE) {
                        helper.getLayerManager().showPoint = false;
                        helper.getOptManager().optMultiAdd(helper.drawLatlngs);
                        secondHandler.sendEmptyMessage(4);
                        DragViewManager.INSTANCE.exit();
                    } else if (mType == MeasureHelper.SECOND_TYPE_HANDPOINT) {
                        helper.getLayerManager().showPoint = false;
                        helper.getOptManager().optMultiAdd(helper.drawLatlngs);
                        secondHandler.sendEmptyMessage(4);
                        DragViewManager.INSTANCE.exit();
                    } else if (mType == MeasureHelper.SECOND_TYPE_RECTANGLE) {
                        helper.getLayerManager().showPoint = true;
                        helper.getOptManager().optMultiAdd(helper.drawLatlngs);
                        secondHandler.sendEmptyMessage(4);
                        DragViewManager.INSTANCE.exit();
                    } else if (mType == MeasureHelper.SECOND_TYPE_LOCATION) {
                        helper.postTool(null, "下一要素");
                        secondHandler.sendEmptyMessage(4);
                        DragViewManager.INSTANCE.exit();
                    } else if (mType == MeasureHelper.SECOND_TYPE_GPS) {
                        secondHandler.sendEmptyMessage(4);
                        DragViewManager.INSTANCE.exit();
                    }
                }
            });
            //打点
            tvToolAddPoint.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mType == MeasureHelper.SECOND_TYPE_LOCATION) {
                        helper.getOptManager().optAdd(helper.locationPoint);
                    } else if (mType == MeasureHelper.SECOND_TYPE_GPS) {
                        if (!ZXPermissionUtil.checkLocationPermissions()) {
                            ZXPermissionUtil.requestLocationPermissions((Activity) context);
                        } else {
                            Location location = ZXLocationUtil.getLocation((Activity) context);
                            if (location != null) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                helper.getOptManager().optAdd(latLng);
                            }
                        }
                    }

                }
            });
        }

        tvToolLocationInfo.setVisibility(View.GONE);
        tvToolAdd.setVisibility(View.VISIBLE);
        etToolLon.setVisibility(View.GONE);
        etToolLat.setVisibility(View.GONE);
        locationLl.setVisibility(View.GONE);
        tvToolAddPoint.setVisibility(View.GONE);
        if (type == MeasureHelper.SECOND_TYPE_LOCATION) {
            tvToolLocationInfo.setVisibility(View.VISIBLE);
            tvToolAddPoint.setVisibility(View.VISIBLE);
            tvToolTips.setText("光标位置：");
            if (coordinate != null) {
                tvToolLocationInfo.setText(String.format("(%.6f,%.6f)", coordinate.getLatitude(), coordinate.getLongitude()));
            }
        } else if (type == MeasureHelper.SECOND_TYPE_GPS) {
            tvToolAddPoint.setVisibility(View.VISIBLE);
            tvToolTips.setText("点击完成录入当前位置");
        } else if (type == MeasureHelper.SECOND_TYPE_RECTANGLE) {
            tvToolTips.setText("请在地图上绘制矩形");
        } else if (type == MeasureHelper.SECOND_TYPE_CIRCLE) {
            tvToolTips.setText("请在地图上绘制圆");
        } else if (type == MeasureHelper.SECOND_TYPE_INPUT) {
            etToolLon.setVisibility(View.VISIBLE);
            etToolLat.setVisibility(View.VISIBLE);
            locationLl.setVisibility(VISIBLE);
        } else if (type == MeasureHelper.SECOND_TYPE_HANDPOINT) {
            tvToolTips.setText("请在地图上进行手绘");
        } else {
            tvToolTips.setText("");
        }
    }

    /**
     * 退出测量
     */
    public void close() {
        if (getVisibility() == View.VISIBLE) {
            setVisibility(View.GONE);
            setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out));
            isOpen = false;
            zxMap.setOnMapClickListener(defaultListener);
            delete(true);
            helper.exit();
        }
    }

    /**
     * 是否开启了测量
     *
     * @return
     */
    public boolean isOpen() {
        return isOpen;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.mea_delete) {//删除
            delete(false);
        } else if (id == R.id.mea_redo) {//下一步
            if (helper.postRedo()) {
                ivRedo.setBackgroundResource(R.mipmap.recover_off);
            }
            if (helper.getLatlngs().size() > 0) {
                ivUndo.setBackgroundResource(R.mipmap.goback_on);
            }
        } else if (id == R.id.mea_undo) {//上一步
            if (helper.postUndo()) {
                ivUndo.setBackgroundResource(R.mipmap.goback_off);
            }
            ivRedo.setBackgroundResource(R.mipmap.recover_on);
        } else if (id == R.id.mea_exit) {//退出
            delete(true);
            close();
            if (measureType == MeasureType.Statistics) {
                statisticsCallBack.onStatisticsClose();
            } else {
                if (closeListener != null) {
                    closeListener.onMeasureClose();
                }
            }
        } else if (id == R.id.mea_shape) {//多边形
            if (shapeBubble == null) {
                shapeBubble = new MBubbleView(context);
                View shapeView = LayoutInflater.from(context).inflate(R.layout.layout_mea_shape, null);
                shapeBubble.setBubbleView(context, shapeView, R.color.colorPrimary);
            }
            shapeBubble.show(view, Gravity.TOP);
        } else if (id == R.id.mea_tools) {//工具栏
            timer.cancel();
            marginHandler.sendEmptyMessage(2);
            secondHandler.sendEmptyMessage(4);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    marginHandler.sendEmptyMessage(ivTools.isSelected() ? 1 : 0);
                }
            }, 0, 30);
            ivTools.setSelected(!ivTools.isSelected());
        }
//        else if (id == R.id.mea_statis) {//统计
//            if (latLngList.size() > 2) {
//                StringBuffer buffer = new StringBuffer();
//                buffer.append("[[");
//                DecimalFormat df = new DecimalFormat("######0.000000");
//                List<LatLng> tempList = new ArrayList<>();
//                tempList.addAll(latLngList);
//                tempList.add(latLngList.get(0));
//                for (int i = 0; i < tempList.size(); i++) {
//                    buffer.append("[\"");
//                    buffer.append(df.format(tempList.get(i).getLongitude()) + "");
//                    buffer.append("\",\"");
//                    buffer.append(df.format(tempList.get(i).getLatitude()) + "");
//                    buffer.append("\"]");
//                    if (i != tempList.size() - 1) {
//                        buffer.append(",");
//                    }
//                }
//                buffer.append("]]");
//                String latlngString = buffer.toString();
//                statisticsCallBack.onStatisticsCallBack(latlngString);
//            }
//        }
    }


    /**
     * 删除
     */
    public void delete(boolean deleteTotal) {
        ivDelete.setImageAlpha(100);
        ivUndo.setBackgroundResource(R.mipmap.goback_off);
        ivRedo.setBackgroundResource(R.mipmap.recover_off);
        size = 0;
        tvCount.setText(0.00 + "");
        if (deleteTotal) helper.meaTotalList.clear();
        helper.clear();
        helper.updateSource(true);
    }

    private Handler marginHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
            final int what = msg.what;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flToolContent.getLayoutParams();
            rvTools.setVisibility(View.VISIBLE);
            if (what == 0) {
                int marignTop = params.topMargin - 10;
                if (marignTop <= -ZXSystemUtil.dp2px(40)) {
                    marignTop = -ZXSystemUtil.dp2px(40);
                    timer.cancel();
                }
                params.topMargin = marignTop;
            } else if (what == 1) {
                int marignTop = params.topMargin + 10;
                if (marignTop >= 0) {
                    marignTop = 0;
                    timer.cancel();
                }
                params.topMargin = marignTop;
            } else if (what == 2) {
                if (ivTools.isSelected()) {
                    params.topMargin = -ZXSystemUtil.dp2px(40);
                } else {
                    params.topMargin = 0;
                }
            }
            flToolContent.setLayoutParams(params);
        }
    };

    private Handler secondHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull final Message msg) {
//            super.handleMessage(msg);
            final int what = msg.what;
            measureSecondFuncLl.animate().alpha(what == 3 ? 1f : 0f).setDuration(300).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (what == 3) {
                        rvTools.setVisibility(View.GONE);
                        measureSecondFuncLl.setVisibility(View.VISIBLE);
                    } else if (what == 4) {

                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (what == 3) {

                    } else if (what == 4) {
                        measureSecondFuncLl.setVisibility(View.GONE);
                        rvTools.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    };

    public void setOnLayerChangedListener(OnLayerChangeListener layerChangedListener) {
        this.layerChangeListener = layerChangedListener;
    }

    //规范化
    private String formatDouble(double value) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(value);
    }

    public interface OnStatisticsCallBack {
        void onStatisticsCallBack(String latlngString);

        void onStatisticsClose();
    }

    public interface OnMeasureCloseListener {
        void onMeasureClose();
    }

    public interface OnLayerChangeListener {
        void onLayerAdd(String id, Layer layer);

        void onLayerRemove(String id);
    }
}
