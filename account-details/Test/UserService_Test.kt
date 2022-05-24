import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import models.EmailBody
import service.UserService

class UserServiceTest : StringSpec() {
    init {
        "should welcome be send" {
            val dummyUserService = DummyUserSevice()
            val userService = UserService(null, dummyUserService)
            userService.sendWelcomeEmail("test@email.com")
            val expectedEmailBody = EmailBody("Welcome", "Welcome to the portal", "test@email.com" )
            val actualEmailBody = dummyUserService.getLastEmail()
            actualEmailBody shouldBe expectedEmailBody
        }

    }

}
