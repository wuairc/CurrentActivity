package run.yang.currentactivity.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Typeface
import android.os.Build.VERSION_CODES
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.util.CircularArray
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StyleSpan
import android.util.Log
import run.yang.currentactivity.R
import run.yang.currentactivity.R.string
import run.yang.currentactivity.app.util.TextTool
import run.yang.currentactivity.common.constant.NotiChannelId
import java.util.*
import java.util.concurrent.TimeUnit

class CurrentActivityNotification(
        context: Context, @DrawableRes notificationIconRes: Int,
        private val mNotificationId: Int
) {
    private val mNotificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, NotiChannelId.CURRENT_ACTIVITY)
    private val mBigTextStyle: NotificationCompat.BigTextStyle

    private val mNotificationHistory = CircularArray<NotificationItemInfo>(MAX_NOTIFICATION_ITEM)

    init {
        mBuilder.setSmallIcon(notificationIconRes)
        mBuilder.setCategory(NotificationCompat.CATEGORY_STATUS)
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        mBuilder.setAutoCancel(false)
        mBuilder.priority = NotificationCompat.PRIORITY_DEFAULT

        mBigTextStyle = NotificationCompat.BigTextStyle(mBuilder)
    }

    fun show(packageName: CharSequence?, fullClassName: CharSequence?) {
        Log.d(TAG, "show " + fullClassName)

        if (TextUtils.isEmpty(packageName) || TextUtils.isEmpty(fullClassName)) {
            Log.e(TAG, "package name and fullClassName are null")
            return
        }

        if (fullClassName!!.startsWith("android.")) {
            return
        }

        addToHistory(packageName!!, fullClassName)

        val activityName = TextTool.lastPartExclude(fullClassName, '.')
        val packageBaseName = TextTool.removeLastPartExclude(fullClassName, '.')
        mBuilder.setContentTitle(activityName)
        mBuilder.setContentText(packageBaseName)
        mBuilder.setTicker(activityName)

        showBigTextStyle()

        mNotificationManager.notify(mNotificationId, mBuilder.build())
    }

    private fun sameAsLastClass(packageName: CharSequence, fullClassName: CharSequence): Boolean {
        if (!mNotificationHistory.isEmpty) {
            val first = mNotificationHistory.first
            return TextUtils.equals(
                    first.fullClassName,
                    fullClassName
            ) && TextUtils.equals(first.packageName, packageName)
        }

        return false
    }

    private fun showBigTextStyle() {
        val first = mNotificationHistory.first
        val size = mNotificationHistory.size()
        val builder = SpannableStringBuilder()
        var start: Int
        var previousTime = System.currentTimeMillis()
        for (i in 0 until size) {
            val info = mNotificationHistory.get(i)

            builder.append(TextTool.NON_BREAKING_SPACE)
            if (info.fullClassName!!.startsWith(info.packageName!!)) {
                start = builder.length
                builder.append(info.fullClassName)
                val end = start + info.packageName!!.length
                builder.setSpan(
                        StyleSpan(Typeface.BOLD),
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                builder.append(info.fullClassName).append('/').append(info.packageName)
            }

            val diffTime = previousTime - info.addTime
            val text = formatNotificationElapsedTime(diffTime)
            start = builder.length
            builder.append(TextTool.combineToSingleLine(String.format(Locale.US, " %-6s", text)))
            builder.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    builder.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.append('\n')

            previousTime = info.addTime
        }
        mBigTextStyle.bigText(builder)
        mBuilder.setContentTitle(TextTool.lastPartExclude(first.fullClassName, '.'))
        mBuilder.setContentText(TextTool.removeLastPartExclude(first.fullClassName, '.'))
    }

    private fun addToHistory(packageName: CharSequence, fullClassName: CharSequence) {
        if (sameAsLastClass(packageName, fullClassName)) {
            // update first item add time
            mNotificationHistory.first.addTime = System.currentTimeMillis()
            return
        }

        val info: NotificationItemInfo
        if (mNotificationHistory.size() < MAX_NOTIFICATION_ITEM) {
            info = NotificationItemInfo()
        } else {
            info = mNotificationHistory.popLast()
        }
        info.addTime = System.currentTimeMillis()
        info.packageName = packageName
        info.fullClassName = fullClassName
        mNotificationHistory.addFirst(info)
    }

    private class NotificationItemInfo {
        var packageName: CharSequence? = null
        var fullClassName: CharSequence? = null
        var addTime: Long = 0
    }

    companion object {

        private val TAG = CurrentActivityNotification::class.java.simpleName

        private const val MAX_NOTIFICATION_ITEM = 5

        @RequiresApi(VERSION_CODES.O)
        fun createCurrentActivityNotificationChannel(context: Context) {
            val nm =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val id = NotiChannelId.CURRENT_ACTIVITY
            val name = context.getString(R.string.noti_channel_current_activity_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)
            channel.description =
                    context.getString(string.noti_channel_current_activity_description)
            nm.createNotificationChannel(channel)
        }

        private fun formatNotificationElapsedTime(diffTimeMillis: Long): String {
            val builder = StringBuilder()
            when {
                diffTimeMillis < 1000 -> {
                    builder.append(diffTimeMillis).append("ms")
                }
                diffTimeMillis < TimeUnit.SECONDS.toMillis(10) -> {
                    builder.append(String.format(Locale.US, "%.1f", diffTimeMillis / 1000f))
                            .append("s")
                }
                diffTimeMillis < TimeUnit.MINUTES.toMillis(1) -> {
                    builder.append(TimeUnit.MILLISECONDS.toSeconds(diffTimeMillis)).append("s")
                }
                diffTimeMillis < TimeUnit.MINUTES.toMillis(10) -> {
                    val minute = TimeUnit.MILLISECONDS.toMinutes(diffTimeMillis)
                    val seconds =
                            TimeUnit.MILLISECONDS.toSeconds(diffTimeMillis) - TimeUnit.MINUTES.toSeconds(
                                    minute
                            )
                    builder.append(minute).append("m").append(seconds).append("s")
                }
                diffTimeMillis < TimeUnit.HOURS.toMillis(1) -> {
                    builder.append(TimeUnit.MILLISECONDS.toMinutes(diffTimeMillis)).append("m")
                }
                else -> {
                    builder.append(">1h")
                }
            }

            return builder.toString()
        }
    }
}
