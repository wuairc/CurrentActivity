package run.yang.currentactivity.app

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import run.yang.currentactivity.R

class CurrentActivityAccessibilityService : AccessibilityService() {

    private var mNotification: CurrentActivityNotification? = null

    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) {
            return
        }

        if (mNotification == null) {
            mNotification = CurrentActivityNotification(
                    this, R.drawable.ic_launcher_foreground, 1)
        }
        mNotification!!.show(event.packageName, event.className)
    }

}
