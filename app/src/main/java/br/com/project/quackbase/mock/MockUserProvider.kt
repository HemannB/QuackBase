package br.com.project.quackbase.mock

import br.com.project.quackbase.model.User

object MockUserProvider {

    fun getUser(): User {
        return User(
            name = "Duck Dodgers",
            email = "duck@quackbase.com"
        )
    }
}
