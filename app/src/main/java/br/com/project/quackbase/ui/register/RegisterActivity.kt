package br.com.project.quackbase.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.project.quackbase.R
import br.com.project.quackbase.mock.MockAuthProvider
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

        inputEmail.error = null
        inputPassword.error = null
        inputConfirmPassword.error = null

        if (!Validators.isEmailValid(email)) {
            inputEmail.error = getString(R.string.error_invalid_email)
            inputEmail.requestFocus()
            return
        }

        if (!Validators.isPasswordValid(password)) {
            inputPassword.error = getString(R.string.error_password_length)
            inputPassword.requestFocus()
            return
        }

        if (!Validators.passwordsMatch(password, confirmPassword)) {
            inputConfirmPassword.error = getString(R.string.error_passwords_do_not_match)
            inputConfirmPassword.requestFocus()
            return
        }

        MockAuthProvider.register(email, password)
        openDashboard()
    }

    private fun openDashboard() {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
