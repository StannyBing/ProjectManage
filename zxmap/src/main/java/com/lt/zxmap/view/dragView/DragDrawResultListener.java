package com.lt.zxmap.view.dragView;

import android.graphics.PointF;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * create by 96212 on 2020/6/16.
 * Email 962123525@qq.com
 * desc 拖动绘制回调
 */
public interface DragDrawResultListener {
    void drawCircleResult(int radius, PointF pointF);
    void drawRectangleResult(Rect rect);
    void drawHandPointResult(ArrayList<PointF> pointFs);
}
