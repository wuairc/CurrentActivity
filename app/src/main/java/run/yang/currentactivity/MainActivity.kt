package run.yang.currentactivity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import run.yang.currentactivity.common.base.BaseActivity
import run.yang.currentactivity.common.constant.NotiChannelId
import run.yang.currentactivity.common.util.startExternalActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        go_to_accessibility_settings_btn.setOnClickListener {
            Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).startExternalActivity(this) {
                Toast.makeText(this, R.string.app_fail_to_go_to_accessibility_settings, Toast.LENGTH_SHORT).show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manage_noti_channels_btn.setOnClickListener {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, NotiChannelId.CURRENT_ACTIVITY)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                intent.startExternalActivity(this) {
                    Toast.makeText(this, R.string.app_fail_to_go_to_notification_settings, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            manage_noti_channels_btn.visibility = View.GONE
        }

        go_to_system_settings.setOnClickListener {
            Intent(Settings.ACTION_SETTINGS).startExternalActivity(this) {
                Toast.makeText(this, R.string.app_fail_to_go_to_system_settings, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
