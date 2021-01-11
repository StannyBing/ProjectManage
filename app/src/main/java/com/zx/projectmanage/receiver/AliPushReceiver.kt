package com.zx.projectmanage.receiver

import android.content.Context
import android.util.Log
import com.alibaba.sdk.android.push.MessageReceiver
import com.alibaba.sdk.android.push.notification.CPushMessage

/**
 * Date:2021/1/11
 * Time:3:51 PM
 * author:qingsong
 */
class AliPushReceiver : MessageReceiver() {
    public override fun onNotification(context: Context, title: String, summary: String, extraMap: Map<String, String>) {
        // TODO 处理推送通知
        Log.e("MyMessageReceiver", "Receive notification, title: $title, summary: $summary, extraMap: $extraMap")
    }

    public override fun onMessage(context: Context, cPushMessage: CPushMessage) {
        Log.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.messageId + ", title: " + cPushMessage.title + ", content:" + cPushMessage.content)
    }

    public override fun onNotificationOpened(context: Context, title: String, summary: String, extraMap: String) {
        Log.e("MyMessageReceiver", "onNotificationOpened, title: $title, summary: $summary, extraMap:$extraMap")
    }

    override fun onNotificationClickedWithNoAction(context: Context, title: String, summary: String, extraMap: String) {
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: $title, summary: $summary, extraMap:$extraMap")
    }

    override fun onNotificationReceivedInApp(
        context: Context,
        title: String,
        summary: String,
        extraMap: Map<String, String>,
        openType: Int,
        openActivity: String,
        openUrl: String
    ) {
        Log.e(
            "MyMessageReceiver",
            "onNotificationReceivedInApp, title: $title, summary: $summary, extraMap:$extraMap, openType:$openType, openActivity:$openActivity, openUrl:$openUrl"
        )
    }

    override fun onNotificationRemoved(context: Context, messageId: String) {
        Log.e("MyMessageReceiver", "onNotificationRemoved")
    }

    companion object {
        // 消息接收部分的LOG_TAG
        const val REC_TAG = "receiver"
    }
}