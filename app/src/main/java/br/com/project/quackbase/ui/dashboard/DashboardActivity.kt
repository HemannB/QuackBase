package br.com.project.quackbase.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.project.quackbase.R
import br.com.project.quackbase.data.auth.AuthRepository
import br.com.project.quackbase.data.device.DeviceInfoProvider
import br.com.project.quackbase.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseUser
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

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

        val user = authRepository.getCurrentUser()
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
            authRepository.signOut()
            openLogin()
        }
    }

    private fun loadUser(user: FirebaseUser) {
        val email = user.email.orEmpty()
        val displayName = user.displayName
            ?.takeIf { it.isNotBlank() }
            ?: displayNameFrom(email)

        txtUserName.text = displayName
        txtUserEmail.text = email.ifBlank { getString(R.string.value_unavailable) }
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

    private fun displayNameFrom(email: String): String {
        val name = email
            .substringBefore('@')
            .replace('.', ' ')
            .replace('_', ' ')
            .replace('-', ' ')
            .split(' ')
            .filter { it.isNotBlank() }
            .joinToString(" ") { part ->
                part.replaceFirstChar { firstCharacter ->
                    firstCharacter.uppercaseChar()
                }
            }

        return name.ifBlank { getString(R.string.default_user_name) }
    }

    private fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
