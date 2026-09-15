package br.com.project.quackbase.util

import android.util.Patterns

object Validators {

    fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun passwordsMatch(
        password: String,
        confirmation: String
    ): Boolean {
        return password == confirmation
    }
}