package br.com.project.quackbase.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.project.quackbase.R
import br.com.project.quackbase.mock.MockAuthProvider
import br.com.project.quackbase.mock.MockUserProvider
import br.com.project.quackbase.ui.dashboard.DashboardActivity
import br.com.project.quackbase.ui.register.RegisterActivity
import br.com.project.quackbase.util.Validators

class LoginActivity : AppCompatActivity() {

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText

    private lateinit var btnLogin: Button
    private lateinit var txtRegister: TextView
    private lateinit var txtMockCredentials: TextView

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
        txtMockCredentials = findViewById(R.id.txtMockCredentials)

        txtMockCredentials.text = getString(
            R.string.mock_credentials,
            MockUserProvider.getUser().email,
            MockAuthProvider.TEST_PASSWORD
        )

        fillMockCredentials()
    }

    private fun initializeListeners() {

        btnLogin.setOnClickListener {
            login()
        }

        txtRegister.setOnClickListener {
            openRegister()
        }

        txtMockCredentials.setOnClickListener {
            fillMockCredentials()
        }
    }

    private fun login() {

        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString()

        inputEmail.error = null
        inputPassword.error = null

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

        if (MockAuthProvider.signIn(email, password) == null) {
            inputPassword.error = getString(R.string.error_invalid_mock_credentials)
            inputPassword.requestFocus()
            return
        }

        openDashboard()
    }

    private fun fillMockCredentials() {
        inputEmail.setText(MockUserProvider.getUser().email)
        inputPassword.setText(MockAuthProvider.TEST_PASSWORD)
        inputPassword.setSelection(inputPassword.text.length)
    }

    private fun openRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun openDashboard() {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
