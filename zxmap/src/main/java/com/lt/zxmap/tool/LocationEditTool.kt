package com.lt.zxmap.tool

import com.zx.zxutils.util.ZXToastUtil
import com.zxmap.zxmapsdk.geometry.LatLng

object LocationEditTool {

    fun checkLocation(longitude: String, latitude: String): LatLng? {
        try {
            if (longitude.isEmpty()) {
                ZXToastUtil.showToast("请输入经度")
            } else if (latitude.isEmpty()) {
                ZXToastUtil.showToast("请输入纬度")
            } else if (!verificationNumber(longitude)) {
                ZXToastUtil.showToast("经度不符合规范")
            } else if (!verificationNumber(latitude)) {
                ZXToastUtil.showToast("纬度不符合规范")
            } else {
                val typeLon = getType(longitude, true)
                val typeLat = getType(latitude, false)
                if (typeLon != typeLat) {
                    ZXToastUtil.showToast("经纬度坐标不一致")
                } else if (typeLon == CoordinateSystem.GCS_0) {
                    return LatLng(latitude.toDouble(), longitude.toDouble())
                } else if (typeLon == CoordinateSystem.GCS_1) {
                    val x_du = longitude.substring(0, 3).toDouble()
                    val x_fen = longitude.substring(3, 5).toDouble()
                    val x_miao = longitude.substring(5, longitude.length).toDouble()
                    val x_cs_0 = x_du + (x_fen + (x_miao / 60)) / 60

                    val y_du = latitude.substring(0, 2).toDouble()
                    val y_fen = latitude.substring(2, 4).toDouble()
                    val y_miao = latitude.substring(4, latitude.length).toDouble()
                    val y_cs_0 = y_du + (y_fen + (y_miao / 60)) / 60

                    return LatLng(String.format("%.5f", y_cs_0).toDouble(), String.format("%.5f", x_cs_0).toDouble())
                } else if (typeLon == CoordinateSystem.RC) {
                    val latLngStr = CoordTransTool.GaussToBL2(longitude.substring(2).toDouble(), latitude.toDouble())
                    return LatLng(latLngStr[1], latLngStr[0])
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        ZXToastUtil.showToast("坐标输入不符合规范")
        return null
    }


    /**
     * 检验 是否是数字
     */
    private fun verificationNumber(value: String): Boolean {
        var regex = Regex("^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$")
        return regex.matches(value)
    }

    enum class CoordinateSystem {
        NULL,
        GCS_0,//地理坐标 模式一 如105.55，29.55
        GCS_1,//地理坐标 模式二 如1053030.55，293030.55
        RC//平面坐标 如36345000，3301000
    }

    private fun getType(value: String, isX: Boolean): CoordinateSystem {
        if (value.contains(".")) {
            val splitX = value.split(".").toTypedArray()

            if (isX) {
                if (splitX[0].length == 3) {//地理坐标 模式一 如105.55，29.55
                    return CoordinateSystem.GCS_0
                }
            } else {
                if (splitX[0].length == 2) {//地理坐标 模式一 如105.55，29.55
                    return CoordinateSystem.GCS_0
                }
            }

            if (isX) {
                if (splitX[0].length == 7) {//地理坐标 模式一 如105.55，29.55
                    return CoordinateSystem.GCS_1
                }
            } else {
                if (splitX[0].length == 6) {//地理坐标 模式一 如105.55，29.55
                    return CoordinateSystem.GCS_1
                }
            }

            if (isX) {
                if (splitX[0].length == 8) {//地理坐标 模式一 如105.55，29.55
                    return CoordinateSystem.RC
                }
            } else {
                if (splitX[0].length == 7) {//地理坐标 模式一 如105.55，29.55
                    return CoordinateSystem.RC
                }
            }

            return CoordinateSystem.NULL

        } else {

            if (isX) {
                if (value.length == 3) {//地理坐标 模式一 如105.55，29.55
                    return CoordinateSystem.GCS_0
                }
            } else {
                if (value.length == 2) {//地理坐标 模式一 如105.55，29.55
                    return CoordinateSystem.GCS_0
                }
            }

            if (isX) {
                if (value.length == 7) {//地理坐标 模式二 如1053030.55，293030.55
                    return CoordinateSystem.GCS_1
                }
            } else {
                if (value.length == 6) {//地理坐标 模式二 如1053030.55，293030.55
                    return CoordinateSystem.GCS_1
                }
            }

            if (isX) {
                if (value.length == 8) {//平面坐标 如36345000，3301000
                    return CoordinateSystem.RC
                }
            } else {
                if (value.length == 7) {//平面坐标 如36345000，3301000
                    return CoordinateSystem.RC
                }
            }
            return CoordinateSystem.NULL
        }
    }
}