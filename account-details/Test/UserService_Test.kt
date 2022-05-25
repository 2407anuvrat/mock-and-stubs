import exceptions.NotFoundException
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import models.EmailBody
import models.User
import repository.UserRepository
import service.EmailService
import service.UserService

class UserServiceTest : StringSpec() {
    init {
        "should welcome be send" {
            val dummyUserService = DummyUserService()
            val userService = UserService(null, dummyUserService)
            userService.sendWelcomeEmail("test@email.com")
            val expectedEmailBody = EmailBody("Welcome", "Welcome to the portal", "test@email.com" )
            val actualEmailBody = dummyUserService.getLastEmail()
            actualEmailBody shouldBe expectedEmailBody
        }

        "should send account details if user is present"{
            val dummyUserService = DummyUserService()
            val dummyRepoService = DummyRepoService()
            UserService(dummyRepoService, dummyUserService).sendRegisteredPhoneNumber("test@email.com")
            val expectedEmailBody = EmailBody("Account Details","Here is your Registered Phone Number: 1234567890", "test@email.com" )
            val actualEmailBody = dummyUserService.getLastEmail()
            actualEmailBody shouldBe expectedEmailBody
        }

        "should send account not available email if user is not found"{
            val dummyUserService = DummyUserService()
            val dummyRepoService = DummyRepoService()
            UserService(dummyRepoService, dummyUserService).sendRegisteredPhoneNumber("abc@email.com")
            val expectedEmailBody = EmailBody("Account Not Found", "We do not have a registered account matching your email address", "abc@email.com")
            val actualEmailBody = dummyUserService.getLastEmail()
            actualEmailBody shouldBe expectedEmailBody
        }

        "should welcome be send in mock" {
            val mockEmailService = io.mockk.mockk<service.EmailService>(relaxed = true)
            val userService = UserService(null, mockEmailService)
            val expectedEmailBody = EmailBody("Welcome", "Welcome to the portal", "test@email.com" )
            userService.sendWelcomeEmail("test@email.com")
            verify(exactly = 1) { mockEmailService.send(expectedEmailBody)}
        }

        "should send account details if user is present with mockk"{
            val mockEmailService = io.mockk.mockk<service.EmailService>(relaxed = true)
            val mockUserService = io.mockk.mockk<UserRepository>(relaxed = true)
            every { mockUserService.findByEmail("test@email.com") } returns User("1234567890", "test@email.com", "abc")
            UserService(mockUserService, mockEmailService).sendRegisteredPhoneNumber("test@email.com")
            val expectedEmailBody = EmailBody("Account Details","Here is your Registered Phone Number: 1234567890", "test@email.com" )
            verify(exactly = 1) { mockEmailService.send(expectedEmailBody) }
        }

        "should send account not available if user is not present with mockk"{
            val mockEmailService = io.mockk.mockk<service.EmailService>(relaxed = true)
            val mockUserService = io.mockk.mockk<UserRepository>()
            every { mockUserService.findByEmail("abc@email.com") } throws NotFoundException()
            UserService(mockUserService, mockEmailService).sendRegisteredPhoneNumber("abc@email.com")
            val expectedEmailBody = EmailBody("Account Not Found", "We do not have a registered account matching your email address", "abc@email.com")
            verify(exactly = 1) { mockEmailService.send(expectedEmailBody) }
        }
    }
}
