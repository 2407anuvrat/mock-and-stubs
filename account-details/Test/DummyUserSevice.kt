import models.EmailBody
import service.EmailService

class DummyUserSevice : EmailService {

    var lastEmailBody: EmailBody? = null
    override fun send(emailBody: EmailBody?) {
        lastEmailBody = emailBody
    }

    fun getLastEmail(): EmailBody?{
        return this.lastEmailBody
    }

}
