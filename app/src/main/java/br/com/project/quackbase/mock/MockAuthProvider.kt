package br.com.project.quackbase.mock

import br.com.project.quackbase.model.User

object MockAuthProvider {

    const val TEST_PASSWORD = "quack123"

    private var registeredUser: User? = null
    private var registeredPassword: String? = null
    private var currentUser: User? = null

    fun signIn(email: String, password: String): User? {
        val customUser = registeredUser
        val defaultUser = MockUserProvider.getUser()

        currentUser = when {
            customUser != null &&
                email.equals(customUser.email, ignoreCase = true) &&
                password == registeredPassword -> customUser

            email.equals(defaultUser.email, ignoreCase = true) &&
                password == TEST_PASSWORD -> defaultUser

            else -> null
        }

        return currentUser
    }

    fun register(email: String, password: String): User {
        val user = User(
            name = displayNameFrom(email),
            email = email
        )

        registeredUser = user
        registeredPassword = password
        currentUser = user

        return user
    }

    fun getCurrentUser(): User? = currentUser

    fun signOut() {
        currentUser = null
    }

    private fun displayNameFrom(email: String): String {
        val name = email
            .substringBefore('@')
            .replace('.', ' ')
            .replace('_', ' ')
            .replace('-', ' ')
            .split(' ')
            .filter { it.isNotBlank() }
            .joinToString(" ") { part ->
                part.replaceFirstChar { firstCharacter ->
                    firstCharacter.uppercaseChar()
                }
            }

        return name.ifBlank { "New Duck" }
    }
}
