package run.yang.currentactivity.app.app

import android.app.Application
import run.yang.currentactivity.app.noti.NotiChannels

/**
 * 创建时间: 2018/01/31 12:15 <br>
 * 作者: Yang Tianmei <br>
 * 描述:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NotiChannels.createAppNotiChannels(this)
    }
}