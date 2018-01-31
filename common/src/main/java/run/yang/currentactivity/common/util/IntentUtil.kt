package run.yang.currentactivity.common.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent

/**
 * 创建时间: 2018/01/31 14:38 <br>
 * 作者: Yang Tianmei <br>
 * 描述:
 */
object IntentUtil {

    inline fun launchExternalActivity(context: Context, intent: Intent,
                                      notFoundHandler: (e: ActivityNotFoundException) -> Unit) {
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            notFoundHandler(e)
        }
    }
}