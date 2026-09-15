package br.com.project.quackbase.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.project.quackbase.R
import br.com.project.quackbase.ui.dashboard.DashboardActivity
import br.com.project.quackbase.util.Validators

class RegisterActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPassword: EditText

    private lateinit var btnRegister: Button
    private lateinit var txtLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        initializeComponents()
        initializeListeners()
    }

    private fun initializeComponents() {
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword)

        btnRegister = findViewById(R.id.btnRegister)
        txtLogin = findViewById(R.id.txtLogin)
    }

    private fun initializeListeners() {

        btnRegister.setOnClickListener {
            register()
        }

        txtLogin.setOnClickListener {
            finish()
        }
    }

    private fun register() {

        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString()
        val confirmPassword = inputConfirmPassword.text.toString()

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

        if (!Validators.passwordsMatch(password, confirmPassword)) {
            inputConfirmPassword.error = "Passwords do not match"
            inputConfirmPassword.requestFocus()
            return
        }
        openDashboard()
    }

    private fun openDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}