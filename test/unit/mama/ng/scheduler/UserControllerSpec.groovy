package mama.ng.scheduler

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification

@TestMixin(DomainClassUnitTestMixin)
@TestFor(UserController)
@Mock([User])
class UserControllerSpec extends Specification {

    User user

    def setup() {
        user = new User(
            username: "vumi",
            passwordHash: "1234",
            apiKey: "123456789"
        )
        user.save(flush: true)
    }

    def cleanup() {

    }

    void "test show user"() {
        when:
            controller.show(user)

        then:
            response.status == 200
    }

    void "test show null user"() {
        when:
            controller.show(null)

        then:
            response.status == 404
    }

    void "test list user"() {
        when:
            controller.list()

        then:
            response.status == 200
    }

    void "test create user"() {
        given:
            User user2 = new User(
                username: "test2",
                passwordHash: "password"
            )
        request.method = 'POST'

        when:
            controller.create(user2)

        then:
            response.status == 200
    }

    void "test create error user"() {
        given:
            User user2 = new User(
                username: "test2",
                passwordHash: null
            )
            request.method = 'POST'

        when:
            controller.create(user2)

        then:
            response.status == 400
    }

    void "test update user"() {
        given:
            params.id = user.id
            request.method = 'PUT'

        when:
            controller.update()

        then:
            response.status == 200
    }

    void "test update null user"() {
        given:
            params.id = 10
            request.method = 'PUT'

        when:
            controller.update()

        then:
            response.status == 404
    }

    void "test delete user"() {
        given:
            request.method = 'DELETE'

        when:
            controller.delete(user)

        then:
            response.status == 200
    }


    void "test delete null user"() {
        given:
            request.method = 'DELETE'

        when:
            controller.delete(null)

        then:
            response.status == 404
    }
}
