package br.com.project.quackbase.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import br.com.project.quackbase.R
import br.com.project.quackbase.data.auth.AuthRepository
import br.com.project.quackbase.ui.dashboard.DashboardActivity
import br.com.project.quackbase.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (authRepository.getCurrentUser() != null) {
            openDashboard()
            return
        }

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnGetStarted).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun openDashboard() {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
