package br.com.project.quackbase.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.project.quackbase.R
import br.com.project.quackbase.data.auth.AuthRepository
import br.com.project.quackbase.ui.dashboard.DashboardActivity
import br.com.project.quackbase.ui.register.RegisterActivity
import br.com.project.quackbase.util.AuthErrorMapper
import br.com.project.quackbase.util.Validators
import kotlin.fold

class LoginActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText

    private lateinit var btnLogin: Button
    private lateinit var txtRegister: TextView
    private lateinit var txtLoginError: TextView
    private lateinit var progressLogin: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (authRepository.getCurrentUser() != null) {
            openDashboard()
            return
        }

        setContentView(R.layout.activity_login)

        initializeComponents()
        initializeListeners()
    }

    private fun initializeComponents() {
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)

        btnLogin = findViewById(R.id.btnLogin)
        txtRegister = findViewById(R.id.txtRegister)
        txtLoginError = findViewById(R.id.txtLoginError)
        progressLogin = findViewById(R.id.progressLogin)
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

        inputEmail.error = null
        inputPassword.error = null
        txtLoginError.visibility = View.GONE

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

        setLoading(true)

        authRepository.signIn(email, password) { result ->
            if (!isFinishing && !isDestroyed) {
                setLoading(false)
                result.fold(
                    onSuccess = { openDashboard() },
                    onFailure = { exception -> showAuthError(exception) }
                )
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        inputEmail.isEnabled = !loading
        inputPassword.isEnabled = !loading
        btnLogin.isEnabled = !loading
        txtRegister.isEnabled = !loading
        progressLogin.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showAuthError(exception: Throwable) {
        txtLoginError.setText(AuthErrorMapper.messageFor(exception))
        txtLoginError.visibility = View.VISIBLE
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
