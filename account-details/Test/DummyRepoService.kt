import exceptions.NotFoundException
import models.User
import repository.UserRepository

class DummyRepoService : UserRepository {
    override fun findByEmail(email: String?): User {
        if (email == "abc@email.com"){
            throw NotFoundException()
        }
        return User("1234567890", email!!, "abc")
    }

}
