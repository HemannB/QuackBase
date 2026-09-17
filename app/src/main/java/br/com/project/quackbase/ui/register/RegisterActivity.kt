package br.com.project.quackbase.ui.register

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
import br.com.project.quackbase.util.AuthErrorMapper
import br.com.project.quackbase.util.Validators

class RegisterActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPassword: EditText

    private lateinit var btnRegister: Button
    private lateinit var txtLogin: TextView
    private lateinit var txtRegisterError: TextView
    private lateinit var progressRegister: ProgressBar

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
        txtRegisterError = findViewById(R.id.txtRegisterError)
        progressRegister = findViewById(R.id.progressRegister)
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
        txtRegisterError.visibility = View.GONE

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

        setLoading(true)

        authRepository.signUp(email, password) { result ->
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
        inputConfirmPassword.isEnabled = !loading
        btnRegister.isEnabled = !loading
        txtLogin.isEnabled = !loading
        progressRegister.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showAuthError(exception: Throwable) {
        txtRegisterError.setText(AuthErrorMapper.messageFor(exception))
        txtRegisterError.visibility = View.VISIBLE
    }

    private fun openDashboard() {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
