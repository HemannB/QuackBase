package br.com.project.quackbase.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.project.quackbase.R
import br.com.project.quackbase.data.device.DeviceInfoProvider
import br.com.project.quackbase.mock.MockAuthProvider
import br.com.project.quackbase.model.User
import br.com.project.quackbase.ui.login.LoginActivity
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
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = MockAuthProvider.getCurrentUser()
        if (user == null) {
            openLogin()
            return
        }

        setContentView(R.layout.activity_dashboard)

        initializeComponents()
        initializeListeners()

        loadUser(user)
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
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun initializeListeners() {
        btnLogout.setOnClickListener {
            MockAuthProvider.signOut()
            openLogin()
        }
    }

    private fun loadUser(user: User) {
        txtUserName.text = user.name
        txtUserEmail.text = user.email
    }

    private fun loadDeviceInfo() {
        val provider = DeviceInfoProvider(this)

        val deviceInfo = provider.getDeviceInfo()

        txtManufacturer.text = deviceInfo.manufacturer
        txtModel.text = deviceInfo.model

        txtAndroidVersion.text = deviceInfo.androidVersion
        txtSdkVersion.text = String.format(
            Locale.getDefault(),
            "%d",
            deviceInfo.sdkVersion
        )

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

    private fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
