package run.yang.currentactivity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import run.yang.currentactivity.common.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val goToAccessibilitySettingsButton = findViewById<Button>(R.id.go_to_accessibility_settings_btn)
        goToAccessibilitySettingsButton.setOnClickListener {
            try {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}
