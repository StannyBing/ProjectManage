package com.zx.projectmanage.app


/**
 * Created by Xiangb on 2019/2/26.
 * 功能：
 */
object ConstStrings {
    var INI_PATH = ""
    var LOCAL_PATH: String? = null
    val APPNAME = "ProjectManage"

    fun getDatabasePath(): String {
        return "$INI_PATH/$APPNAME/DATABASE/"
    }

    fun getCachePath(): String {
        return "$LOCAL_PATH/$APPNAME/Cache/"
    }

    fun getZipPath(): String {
        return "$INI_PATH/$APPNAME/.zip/"
    }

    fun getOnlinePath(): String {
        return "$LOCAL_PATH$APPNAME/ONLINE/"
    }

    fun getCrashPath(): String {
        return "$LOCAL_PATH$APPNAME/CRASH/"
    }

    fun getApkPath(): String {
        return "$LOCAL_PATH$APPNAME/APK/"
    }

    fun getMainPath(): String {
        return "$INI_PATH/$APPNAME/"
    }

    fun getLocalPath(): String {
        return "$LOCAL_PATH$APPNAME/"
    }

}