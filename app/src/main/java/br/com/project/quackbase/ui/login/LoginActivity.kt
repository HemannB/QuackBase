package br.com.project.quackbase.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.project.quackbase.R
import br.com.project.quackbase.ui.dashboard.DashboardActivity
import br.com.project.quackbase.ui.register.RegisterActivity
import br.com.project.quackbase.util.Validators

class LoginActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText

    private lateinit var btnLogin: Button
    private lateinit var txtRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        initializeComponents()
        initializeListeners()
    }

    private fun initializeComponents() {
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)

        btnLogin = findViewById(R.id.btnLogin)
        txtRegister = findViewById(R.id.txtRegister)
    }

    private fun initializeListeners() {

        btnLogin.setOnClickListener {
            login()
        }

        txtRegister.setOnClickListener {
            openRegister()
        }
    }

    private fun login() {

        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString()

        if (!Validators.isEmailValid(email)) {
            inputEmail.error = "Invalid email"
            inputEmail.requestFocus()
            return
        }

        if (!Validators.isPasswordValid(password)) {
            inputPassword.error = "Password must contain at least 6 characters"
            inputPassword.requestFocus()
            return
        }

        openDashboard()
    }

    private fun openRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun openDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}