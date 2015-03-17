package mama.ng.scheduler

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestMixin(DomainClassUnitTestMixin)
@TestFor(User)
@Mock([User])
class UserSpec extends Specification {

    User user

    def setup() {
        user = new User(
            username: "vumi",
            passwordHash: "1234",
            apiKey: "123456789"
        )
    }

    def cleanup() {
        user.delete()
    }


    void "test blank username"() {
        given:
            user.username = ""
            user.save()
        expect:
            user.hasErrors()
            user.errors["username"].toString().contains("blank")
    }

    void "test null username"() {
        given:
            user.username = null
            user.save()
        expect:
            user.hasErrors()
            user.errors["username"].toString().contains("nullable")
    }

    void "test blank passwordHash"() {
        given:
            user.passwordHash = ""
            user.save()
        expect:
            user.hasErrors()
            user.errors["passwordHash"].toString().contains("blank")
    }

    void "test null passwordHash"() {
        given:
            user.passwordHash = null
            user.save()
        expect:
            user.hasErrors()
            user.errors["passwordHash"].toString().contains("nullable")
    }

    void "test blank apiKey"() {
        given:
            user.apiKey = ""
            user.save()
        expect:
            user.hasErrors()
            user.errors["apiKey"].toString().contains("blank")
    }

    void "test null apiKey"() {
        given:
            user.apiKey = null
            user.save()
        expect:
            user.hasErrors()
            user.errors["apiKey"].toString().contains("nullable")
    }

}
