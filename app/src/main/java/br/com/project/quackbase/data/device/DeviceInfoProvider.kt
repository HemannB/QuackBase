package br.com.project.quackbase.data.device

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import br.com.project.quackbase.model.DeviceInfo

class DeviceInfoProvider(
    private val context: Context
) {

    fun getDeviceInfo(): DeviceInfo {

        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val memoryInfo = ActivityManager.MemoryInfo()

        activityManager.getMemoryInfo(memoryInfo)

        return DeviceInfo(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE,
            sdkVersion = Build.VERSION.SDK_INT,
            totalMemory = memoryInfo.totalMem,
            availableMemory = memoryInfo.availMem
        )
    }
}