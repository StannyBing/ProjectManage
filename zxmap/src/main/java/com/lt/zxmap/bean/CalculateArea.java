package com.lt.zxmap.bean;


import com.zxmap.zxmapsdk.geometry.LatLng;

import java.util.List;

/**
 * Created by Xiangb on 2017/4/24.
 * 功能：
 */

public class CalculateArea {

    private static double x1, x2, dx, dy;
    private static double Qbar1, Qbar2;
    private static double mSemiMajor = 6378140.0;
    private static double mSemiMinor = 6356755.0;
    private static double m_AE;
    private static double m_QA, m_QB, m_QC;
    private static double m_QbarA, m_QbarB, m_QbarC, m_QbarD;
    private static double m_Qp;
    private static double m_E;

    public static double getArea(List<LatLng> latLngList) {
        init();
        if (latLngList.size() < 3) {
            return 0;
        } else {
            double[] padX = new double[latLngList.size()];
            double[] padY = new double[latLngList.size()];
            for (int i = 0; i < latLngList.size(); i++) {
                padX[i] = latLngList.get(i).getLongitude();
                padY[i] = latLngList.get(i).getLatitude();
            }
            return ComputePolygonArea(padX, padY, latLngList.size());
        }
    }

    private static double ComputePolygonArea(double[] padX, double[] padY, int nCount) {
        double x1, y1, dx, dy;
        double Qbar1, Qbar2;

        if (null == padX || null == padY) {
            return 0;
        }

        double x2 = DEG2RAD(padX[nCount - 1]);
        double y2 = DEG2RAD(padY[nCount - 1]);
        Qbar2 = GetQbar(y2);

        double area = 0.0;

        for (int i = 0; i < nCount; i++) {
            x1 = x2;
            y1 = y2;
            Qbar1 = Qbar2;

            x2 = DEG2RAD(padX[i]);
            y2 = DEG2RAD(padY[i]);
            Qbar2 = GetQbar(y2);

            if (x1 > x2)
                while (x1 - x2 > Math.PI)
                    x2 += Math.PI * 2;
            else if (x2 > x1)
                while (x2 - x1 > Math.PI)
                    x1 += Math.PI * 2;

            dx = x2 - x1;
            area += dx * (m_Qp - GetQ(y2));

            if ((dy = y2 - y1) != 0.0)
                area += dx * GetQ(y2) - (dx / dy) * (Qbar2 - Qbar1);
        }
        if ((area *= m_AE) < 0.0)
            area = -area;

        if (area > m_E)
            area = m_E;
        if (area > m_E / 2)
            area = m_E - area;

        return area;
    }

    private static void init() {
        double a2 = mSemiMajor * mSemiMinor;
        double e2 = 1 - (a2 / (mSemiMinor * mSemiMinor));
        double e4, e6;
        e4 = e2 * e2;
        e6 = e4 * e2;

        m_AE = a2 * (1 - e2);
        m_QA = (2.0 / 3.0) * e2;
        m_QB = (3.0 / 5.0) * e4;
        m_QC = (4.0 / 7.0) * e6;

        m_QbarA = -1.0 - (2.0 / 3.0) * e2 - (3.0 / 5.0) * e4 - (4.0 / 7.0) * e6;
        m_QbarB = (2.0 / 9.0) * e2 + (2.0 / 5.0) * e4 + (4.0 / 7.0) * e6;
        m_QbarC = -(3.0 / 25.0) * e4 - (12.0 / 35.0) * e6;
        m_QbarD = (4.0 / 49.0) * e6;

        m_Qp = GetQ(Math.PI / 2);
        m_E = 4 * Math.PI * m_Qp * m_AE;
        if (m_E < 0.0)
            m_E = -m_E;
    }

    private static double GetQbar(double x) {
        double cosx, cosx2;
        cosx = Math.cos(x);
        cosx2 = cosx * cosx;

        return cosx * (m_QbarA + cosx2 * (m_QbarB + cosx2 * (m_QbarC + cosx2 * m_QbarD)));
    }

    private static double GetQ(double x) {
        double sinx, sinx2;

        sinx = Math.sin(x);
        sinx2 = sinx * sinx;

        return sinx * (1 + sinx2 * (m_QA + sinx2 * (m_QB + sinx2 * m_QC)));
    }

    private static double DEG2RAD(double x) {
        return x * Math.PI / 180;
    }

}
