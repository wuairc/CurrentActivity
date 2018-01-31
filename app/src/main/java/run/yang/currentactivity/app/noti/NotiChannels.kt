package run.yang.currentactivity.app.noti

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import run.yang.currentactivity.app.CurrentActivityNotification

/**
 * 创建时间: 2018/01/31 12:16 <br>
 * 作者: Yang Tianmei <br>
 * 描述: Notification Channel Manager
 */
object NotiChannels {

    fun createAppNotiChannels(context: Context) {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            CurrentActivityNotification.createCurrentActivityNotificationChannel(context)
        }
    }
}