package br.com.project.quackbase.ui.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.project.quackbase.R
import br.com.project.quackbase.data.device.DeviceInfoProvider
import br.com.project.quackbase.mock.MockUserProvider
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var txtUserName: TextView
    private lateinit var txtUserEmail: TextView

    private lateinit var txtManufacturer: TextView
    private lateinit var txtModel: TextView
    private lateinit var txtAndroidVersion: TextView
    private lateinit var txtSdkVersion: TextView

    private lateinit var txtTotalMemory: TextView
    private lateinit var txtAvailableMemory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        initializeComponents()

        loadUser()
        loadDeviceInfo()
    }

    private fun initializeComponents() {
        txtUserName = findViewById(R.id.txtUserName)
        txtUserEmail = findViewById(R.id.txtUserEmail)

        txtManufacturer = findViewById(R.id.txtManufacturer)
        txtModel = findViewById(R.id.txtModel)
        txtAndroidVersion = findViewById(R.id.txtAndroidVersion)
        txtSdkVersion = findViewById(R.id.txtSdkVersion)

        txtTotalMemory = findViewById(R.id.txtTotalMemory)
        txtAvailableMemory = findViewById(R.id.txtAvailableMemory)
    }

    private fun loadUser() {
        val user = MockUserProvider.getUser()

        txtUserName.text = user.name
        txtUserEmail.text = user.email
    }

    private fun loadDeviceInfo() {
        val provider = DeviceInfoProvider(this)

        val deviceInfo = provider.getDeviceInfo()

        txtManufacturer.text = deviceInfo.manufacturer
        txtModel.text = deviceInfo.model

        txtAndroidVersion.text = deviceInfo.androidVersion
        txtSdkVersion.text = deviceInfo.sdkVersion.toString()

        txtTotalMemory.text =
            formatMemory(deviceInfo.totalMemory)

        txtAvailableMemory.text =
            formatMemory(deviceInfo.availableMemory)
    }

    private fun formatMemory(bytes: Long): String {

        val gigabytes =
            bytes.toDouble() / (1024 * 1024 * 1024)

        return String.format(
            Locale.getDefault(),
            "%.1f GB",
            gigabytes
        )
    }
}