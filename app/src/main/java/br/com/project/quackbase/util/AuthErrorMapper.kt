package br.com.project.quackbase.util

import androidx.annotation.StringRes
import br.com.project.quackbase.R
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

object AuthErrorMapper {

    @StringRes
    fun messageFor(exception: Throwable): Int {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException,
            is FirebaseAuthInvalidUserException -> R.string.auth_error_invalid_credentials

            is FirebaseAuthUserCollisionException -> R.string.auth_error_email_in_use
            is FirebaseAuthWeakPasswordException -> R.string.auth_error_weak_password
            is FirebaseNetworkException -> R.string.auth_error_network
            is FirebaseTooManyRequestsException -> R.string.auth_error_too_many_requests
            else -> R.string.auth_error_generic
        }
    }
}
