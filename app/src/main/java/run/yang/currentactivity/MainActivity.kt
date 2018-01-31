package run.yang.currentactivity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import run.yang.currentactivity.common.base.BaseActivity
import run.yang.currentactivity.common.constant.NotiChannelId
import run.yang.currentactivity.common.util.IntentUtil

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val goToAccessibilitySettingsButton = findViewById<Button>(R.id.go_to_accessibility_settings_btn)
        goToAccessibilitySettingsButton.setOnClickListener {
            IntentUtil.launchExternalActivity(this, Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), {
                Toast.makeText(this, R.string.app_can_not_open_accessibility_settings, Toast.LENGTH_SHORT).show()
            })
        }

        val manageNotiChannelBtn = findViewById<Button>(R.id.manage_noti_channels_btn)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manageNotiChannelBtn.setOnClickListener {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotiChannelId.CURRENT_ACTIVITY)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                IntentUtil.launchExternalActivity(this, intent, {
                    Toast.makeText(this, "Fail to go to notification settings", Toast.LENGTH_SHORT).show()
                })
            }
        } else {
            manageNotiChannelBtn.visibility = View.GONE
        }
    }
}
