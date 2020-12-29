package com.lt.zxmap.view;

import android.content.Context;
import android.util.TypedValue;

import com.zxmap.zxmapsdk.geojson.core.commons.models.Position;
import com.zxmap.zxmapsdk.geometry.LatLng;

import java.util.List;

/**
 * Created by Xiangb on 2019/9/25.
 * 功能：
 */
public class MeaUtil {

    public static void removeLast(List<LatLng> latLngs, LatLng latLng) {
        for (int i = latLngs.size() - 1; i >= 0; i--) {
            if (latLngs.get(i) == latLng) {
                latLngs.remove(i);
                return;
            }
        }
    }

    public static LatLng getLineProjection(List<Position> positions, LatLng outPoint) {
        LatLng lineProjection = null;
        double shortLength = 0;
        for (int i = 0; i < positions.size() - 1; i++) {
            LatLng pProject = getProjectivePoint(new LatLng(positions.get(i).getLatitude(), positions.get(i).getLongitude()),
                    new LatLng(positions.get(i).getLatitude(), positions.get(i).getLongitude()),
                    outPoint);
            double length = Math.sqrt(Math.pow(Math.abs(pProject.getLongitude() - outPoint.getLongitude()), 2) + Math.pow(Math.abs(pProject.getLatitude() - outPoint.getLatitude()), 2));
            if (shortLength == 0 || length < shortLength) {
                shortLength = length;
                lineProjection = pProject;
                lineProjection.setAltitude(i + 1);
            }
        }
        return lineProjection;
    }

    /**
     * 求pOut在pLine以及pLine2所连直线上的投影点
     *
     * @param pLine
     * @param pLine2
     * @param pOut
     */
    public static LatLng getProjectivePoint(LatLng pLine, LatLng pLine2, LatLng pOut) {
        double k = 0;
        try {
            k = getSlope(pLine.getLongitude(), pLine.getLatitude(), pLine2.getLongitude(), pLine2.getLatitude());
        } catch (Exception e) {
            k = 0;
        }
        LatLng pProject = new LatLng();
        if (k == 0) {//垂线斜率不存在情况
            pProject.setLongitude(pOut.getLongitude());
            pProject.setLatitude(pLine.getLatitude());
        } else {
            pProject.setLongitude((float) ((k * pLine.getLongitude() + pOut.getLongitude() / k + pOut.getLatitude() - pLine.getLatitude()) / (1 / k + k)));
            pProject.setLatitude((float) (-1 / k * (pProject.getLongitude() - pOut.getLongitude()) + pOut.getLatitude()));
        }
        return pProject;
    }

    /**
     * 通过两个点坐标计算斜率
     * 已知A(x1,y1),B(x2,y2)
     * 1、若x1=x2,则斜率不存在；
     * 2、若x1≠x2,则斜率k=[y2－y1]/[x2－x1]
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @throws Exception 如果x1==x2,则抛出该异常
     */
    private static double getSlope(double x1, double y1, double x2, double y2) throws Exception {
        if (x1 == x2) {
            throw new Exception("error");
        }
        return (y2 - y1) / (x2 - x1);
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}
