package mama.ng.scheduler

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(SecurityService)
@Mock([User])
class SecurityServiceSpec extends Specification {

    def password = "password".bytes.encodeBase64().toString()
    def userAuth = "Basic dGVzdDpwYXNzd29yZA==" // test:password
    def badAuth = "Basic dGVzdHBhc3N3b3Jk" // testpassword

    def setup() {
    }

    def cleanup() {
    }

    void "test user authentication"() {
        given:
        service.grailsApplication = grailsApplication
        service.init()

        User user = new User(
            username: "test",
            passwordHash: password)
            .save(failOnError: true)

        AuthToken authToken = service.authenticate(
            userAuth,
            "127.0.0.1",
            "Chrome/39.0.2171.65",
            false);

        expect:
        authToken.user.username == user.username &&
            authToken.user.passwordHash == user.passwordHash
    }

    void "test unknown authentication"() {
        given:
        service.grailsApplication = grailsApplication
        service.init()
        AuthToken authToken = service.authenticate(
            userAuth,
            "127.0.0.1",
            "Chrome/39.0.2171.65",
            false);

        expect:
        authToken == null
    }

    void "test null authentication"() {
        given:
        service.grailsApplication = grailsApplication
        service.init()
        AuthToken authToken = service.authenticate(
            null,
            "127.0.0.1",
            "Chrome/39.0.2171.65",
            false);

        expect:
        authToken == null
    }

    void "test bad format authentication"() {
        given:
        service.grailsApplication = grailsApplication
        service.init()
        AuthToken authToken = service.authenticate(
            "abcd",
            "127.0.0.1",
            "Chrome/39.0.2171.65",
            false);

        expect:
        authToken == null
    }

    void "test no colon authentication"() {
        given:
        service.grailsApplication = grailsApplication
        service.init()
        AuthToken authToken = service.authenticate(
            badAuth,
            "127.0.0.1",
            "Chrome/39.0.2171.65",
            false);

        expect:
        authToken == null
    }

    void "test cached authentication"() {
        given:
        service.grailsApplication = grailsApplication
        service.init()

        User user = new User(
            username: "test",
            passwordHash: password)
            .save(failOnError: true)

        service.authenticate(
            userAuth,
            "127.0.0.1",
            "Chrome/39.0.2171.65",
            false);
        AuthToken authToken = service.authenticate(
            userAuth,
            "127.0.0.1",
            "Chrome/39.0.2171.65",
            false);

        expect:
        authToken.user.username == user.username &&
            authToken.user.passwordHash == user.passwordHash
            !service.authCache.isEmpty()
    }

    void "test wrong password authentication"() {
        given:
        service.grailsApplication = grailsApplication
        service.init()

        User user = new User(
            username: "test",
            passwordHash: "notpassword")
            .save(failOnError: true)

        AuthToken authToken = service.authenticate(
            userAuth,
            "127.0.0.1",
            "Chrome/39.0.2171.65",
            false);

        expect:
        authToken == null
    }
}
