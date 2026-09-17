package br.com.project.quackbase.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

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
        name: String,
        email: String,
        password: String,
        onResult: (Result<FirebaseUser>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val user = if (task.isSuccessful) task.result?.user else null

                if (!task.isSuccessful || user == null) {
                    completeWithUser(false, null, task.exception, onResult)
                    return@addOnCompleteListener
                }

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                user.updateProfile(profileUpdates)
                    .addOnCompleteListener { profileTask ->
                        completeWithUser(
                            profileTask.isSuccessful,
                            user,
                            profileTask.exception,
                            onResult
                        )
                    }
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
