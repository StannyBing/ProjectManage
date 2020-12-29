package com.lt.zxmap.tool;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author ondear
 * time  : 16/11/13
 * desc  : 定位相关工具类
 */
public class CoordTransTool {

    public static double pi = 3.1415926535897932384626;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    /**
     * 判断Gps是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断定位是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 打开Gps设置界面
     */
    public static void openGpsSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 根据经纬度获取地理位置
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return {@link Address}
     */
    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据经纬度获取所在国家
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在国家
     */
    public static String getCountryName(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getCountryName();
    }

    /**
     * 根据经纬度获取所在地
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在地
     */
    public static String getLocality(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getLocality();
    }

    /**
     * 根据经纬度获取所在街道
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在街道
     */
    public static String getStreet(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

    //------------------------------------------坐标转换工具start--------------------------------------

    /**
     * GPS坐标 转换成 角度
     * 例如 113.202222 转换成 113°12′8″
     *
     * @param location
     * @return
     */
    public static String gpsToDegree(double location) {
        double degree = Math.floor(location);
        double minute_temp = (location - degree) * 60;
        double minute = Math.floor(minute_temp);
        //        double second = Math.floor((minute_temp - minute)*60);
        String second = new DecimalFormat("#.##").format((minute_temp - minute) * 60);
        return (int) degree + "°" + (int) minute + "′" + second + "″";
    }

    /**
     * 国际 GPS84 坐标系
     * 转换成
     * [国测局坐标系] 火星坐标系 (GCJ-02)
     * <p>
     * World Geodetic System ==> Mars Geodetic System
     *
     * @param lon 经度
     * @param lat 纬度
     * @return GPS实体类
     */
    public static Gps GPS84ToGCJ02(double lon, double lat) {
        if (outOfChina(lon, lat)) {
            return null;
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new Gps(mgLon, mgLat);
    }

    /**
     * [国测局坐标系] 火星坐标系 (GCJ-02)
     * 转换成
     * 国际 GPS84 坐标系
     *
     * @param lon 火星经度
     * @param lat 火星纬度
     */
    public static Gps GCJ02ToGPS84(double lon, double lat) {
        Gps gps = transform(lon, lat);
        double lontitude = lon * 2 - gps.getLongitude();
        double latitude = lat * 2 - gps.getLatitude();
        return new Gps(lontitude, latitude);
    }

    /**
     * 火星坐标系 (GCJ-02)
     * 转换成
     * 百度坐标系 (BD-09)
     *
     * @param gg_lon 经度
     * @param gg_lat 纬度
     */
    public static Gps GCJ02ToBD09(double gg_lon, double gg_lat) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new Gps(bd_lon, bd_lat);
    }

    /**
     * 百度坐标系 (BD-09)
     * 转换成
     * 火星坐标系 (GCJ-02)
     *
     * @param bd_lon 百度*经度
     * @param bd_lat 百度*纬度
     * @return GPS实体类
     */
    public static Gps BD09ToGCJ02(double bd_lon, double bd_lat) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new Gps(gg_lon, gg_lat);
    }

    /**
     * 百度坐标系 (BD-09)
     * 转换成
     * 国际 GPS84 坐标系
     *
     * @param bd_lon 百度*经度
     * @param bd_lat 百度*纬度
     * @return GPS实体类
     */
    public static Gps BD09ToGPS84(double bd_lon, double bd_lat) {
        Gps gcj02 = BD09ToGCJ02(bd_lon, bd_lat);
        Gps map84 = GCJ02ToGPS84(gcj02.getLongitude(), gcj02.getLatitude());
        return map84;

    }

    /**
     * 不在中国范围内
     *
     * @param lon 经度
     * @param lat 纬度
     * @return boolean值
     */
    public static boolean outOfChina(double lon, double lat) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        return lat < 0.8293 || lat > 55.8271;
    }

    /**
     * 转化算法
     *
     * @param lon
     * @param lat
     * @return
     */
    public static Gps transform(double lon, double lat) {
        if (outOfChina(lon, lat)) {
            return new Gps(lon, lat);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new Gps(mgLon, mgLat);
    }

    /**
     * 纬度转化算法
     *
     * @param x
     * @param y
     * @return
     */
    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经度转化算法
     *
     * @param x
     * @param y
     * @return
     */
    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }

    //  由高斯投影坐标反算成经纬度
    //, double *longitude, double *latitude)
    public static String GaussToBL(double X, double Y) {
        int ProjNo;
        int ZoneWide; ////带宽
        String output = "";
        double longitude1, latitude1, longitude0, X0, Y0, xval, yval;//latitude0,
        double e1, e2, f, a, ee, NN, T, C, M, D, R, u, fai, iPI;
        iPI = Math.PI / 180.0; ////3.1415926535898/180.0;
        //a = 6378245.0; f = 1.0/298.3; //54年北京坐标系参数
        a = 6378140.0;
        f = 1 / 298.257; //80年西安坐标系参数
        ZoneWide = 3; ////6度带宽
        ProjNo = (int) (X / 1000000L); //查找带号
//        ProjNo = 36; //查找带号
        //        longitude0 = (ProjNo - 1) * ZoneWide + ZoneWide / 2;
        longitude0 = 105;
        //TODO 长寿使用
        longitude0 = longitude0 * iPI; //中央经线


        X0 = ProjNo * 1000000L + 500000L;
        Y0 = 0;
        xval = X - X0;
        yval = Y - Y0; //带内大地坐标
        e2 = 2 * f - f * f;
        e1 = (1.0 - Math.sqrt(1 - e2)) / (1.0 + Math.sqrt(1 - e2));
        ee = e2 / (1 - e2);
        M = yval;
        u = M / (a * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));
        fai = u + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * u) + (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * Math.sin(
                4 * u)
                + (151 * e1 * e1 * e1 / 96) * Math.sin(6 * u) + (1097 * e1 * e1 * e1 * e1 / 512) * Math.sin(8 * u);
        C = ee * Math.cos(fai) * Math.cos(fai);
        T = Math.tan(fai) * Math.tan(fai);
        NN = a / Math.sqrt(1.0 - e2 * Math.sin(fai) * Math.sin(fai));
        R = a * (1 - e2) / Math.sqrt((1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin
                (fai) * Math.sin(fai)));
        D = xval / NN;
        //计算经度(Longitude) 纬度(Latitude)
        longitude1 = longitude0 + (D - (1 + 2 * T + C) * D * D * D / 6 + (5 - 2 * C + 28 * T - 3 * C * C + 8 * ee + 24 * T * T) * D
                * D * D * D * D / 120) / Math.cos(fai);
        latitude1 = fai - (NN * Math.tan(fai) / R) * (D * D / 2 - (5 + 3 * T + 10 * C - 4 * C * C - 9 * ee) * D * D * D * D / 24
                + (61 + 90 * T + 298 * C + 45 * T * T - 256 * ee - 3 * C * C) * D * D * D * D * D * D / 720);
        //转换为度 DD
        //        output[0] = longitude1 / iPI;
        //        output[1] = latitude1 / iPI;
        return longitude1 / iPI + "," + latitude1 / iPI;
        //*longitude = longitude1 / iPI;
        //*latitude = latitude1 / iPI;
    }

    public static double[] GaussToBL2(double X, double Y) {

        int ProjNo;
        // 带宽
        int ZoneWide;
        double[] output = new double[2];
        // latitude0,
        double longitude1, latitude1, longitude0, X0, Y0, xval, yval;
        double e1, e2, f, a, ee, NN, T, C, M, D, R, u, fai, iPI;
        //3.1415926535898/180.0;
        iPI = 0.0174532925199433;
        a = 6378137;
        f = 1 / 298.2572236;
        // 3度带宽
        ZoneWide = 3;
        // 查找带号
        ProjNo = 0;
        // 中央经线
        longitude0 = 105 * Math.PI / 180;

        X0 = ProjNo * 1000000L + 500000L;
        Y0 = 0;
        xval = X - X0;
        // 带内大地坐标
        yval = Y - Y0;
        e2 = 2 * f - f * f;
        e1 = (1.0 - Math.sqrt(1 - e2)) / (1.0 + Math.sqrt(1 - e2));
        ee = e2 / (1 - e2);
        M = yval;
        u = M / (a * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));
        fai = u + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * u) + (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * Math.sin(4 * u) + (151 * e1 * e1 * e1 / 96) * Math.sin(6 * u) + (1097 * e1 * e1 * e1 * e1 / 512) * Math.sin(8 * u);
        C = ee * Math.cos(fai) * Math.cos(fai);
        T = Math.tan(fai) * Math.tan(fai);
        NN = a / Math.sqrt(1.0 - e2 * Math.sin(fai) * Math.sin(fai));
        R = a * (1 - e2) / Math.sqrt((1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)));
        D = xval / NN;
        // 计算经度(Longitude) 纬度(Latitude)
        longitude1 = longitude0 + (D - (1 + 2 * T + C) * D * D * D / 6 + (5 - 2 * C + 28 * T - 3 * C * C + 8 * ee + 24 * T * T) * D * D * D * D * D / 120) / Math.cos(fai);
        latitude1 = fai - (NN * Math.tan(fai) / R) * (D * D / 2 - (5 + 3 * T + 10 * C - 4 * C * C - 9 * ee) * D * D * D * D / 24 + (61 + 90 * T + 298 * C + 45 * T * T - 256 * ee - 3 * C * C) * D * D * D * D * D * D / 720);
        // 转换为度 DD
        output[0] = longitude1 / iPI;
        output[1] = latitude1 / iPI;
        return output;
    }


    //===========================================坐标转换工具end====================================

    ////  由经纬度反算成高斯投影坐标
    public void GaussToBLToGauss(double longitude, double latitude) {
        int ProjNo = 0;
        int ZoneWide; ////带宽
        double longitude1, latitude1, longitude0, latitude0, X0, Y0, xval, yval;
        double a, f, e2, ee, NN, T, C, A, M, iPI;
        iPI = 0.0174532925199433; ////3.1415926535898/180.0;
        ZoneWide = 6; ////6度带宽
        a = 6378245.0;
        f = 1.0 / 298.3; //54年北京坐标系参数
        ////a=6378140.0; f=1/298.257; //80年西安坐标系参数
        ProjNo = (int) (longitude / ZoneWide);
        longitude0 = ProjNo * ZoneWide + ZoneWide / 2;
        longitude0 = longitude0 * iPI;
        latitude0 = 0;
        System.out.println(latitude0);
        longitude1 = longitude * iPI; //经度转换为弧度
        latitude1 = latitude * iPI; //纬度转换为弧度
        e2 = 2 * f - f * f;
        ee = e2 * (1.0 - e2);
        NN = a / Math.sqrt(1.0 - e2 * Math.sin(latitude1) * Math.sin(latitude1));
        T = Math.tan(latitude1) * Math.tan(latitude1);
        C = ee * Math.cos(latitude1) * Math.cos(latitude1);
        A = (longitude1 - longitude0) * Math.cos(latitude1);
        M = a * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256) * latitude1 - (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2
                * e2 / 1024) * Math.sin(2 * latitude1)
                + (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024) * Math.sin(4 * latitude1) - (35 * e2 * e2 * e2 / 3072) * Math.sin(6 * latitude1));
        xval = NN * (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 72 * C - 58 * ee) * A * A * A * A * A / 120);
        yval = M + NN * Math.tan(latitude1) * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24
                + (61 - 58 * T + T * T + 600 * C - 330 * ee) * A * A * A * A * A * A / 720);
        X0 = 1000000L * (ProjNo + 1) + 500000L;
        Y0 = 0;
        xval = xval + X0;
        yval = yval + Y0;
        //*X = xval;
        //*Y = yval;
        System.out.println("x：" + xval);
        System.out.println("y：" + yval);

    }


    public interface OnLocationChangeListener {

        /**
         * 获取最后一次保留的坐标
         *
         * @param location 坐标
         */
        void getLastKnownLocation(Location location);

        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        void onLocationChanged(Location location);

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        void onStatusChanged(String provider, int status, Bundle extras);//位置状态发生改变
    }

    public static class Gps {

        private double mLatitude;
        private double mLongitude;

        public Gps(double longitude, double mLatitude) {
            setLatitude(mLatitude);
            setLongitude(longitude);
        }

        public double getLatitude() {
            return mLatitude;
        }

        public void setLatitude(double latitude) {
            this.mLatitude = latitude;
        }

        public double getLongitude() {
            return mLongitude;
        }

        public void setLongitude(double longitude) {
            this.mLongitude = longitude;
        }

        @Override
        public String toString() {
            return mLongitude + "," + mLatitude;
        }
    }
}