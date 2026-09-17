package br.com.project.quackbase.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun signIn(
        email: String,
        password: String,
        onResult: (Result<FirebaseUser>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val user = if (task.isSuccessful) task.result?.user else null
                completeWithUser(task.isSuccessful, user, task.exception, onResult)
            }
    }

    fun signUp(
        email: String,
        password: String,
        onResult: (Result<FirebaseUser>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val user = if (task.isSuccessful) task.result?.user else null
                completeWithUser(task.isSuccessful, user, task.exception, onResult)
            }
    }

    fun signOut() {
        auth.signOut()
    }

    private fun completeWithUser(
        successful: Boolean,
        user: FirebaseUser?,
        exception: Exception?,
        onResult: (Result<FirebaseUser>) -> Unit
    ) {
        if (successful && user != null) {
            onResult(Result.success(user))
            return
        }

        onResult(
            Result.failure(
                exception ?: IllegalStateException("Firebase Authentication returned no user")
            )
        )
    }
}
