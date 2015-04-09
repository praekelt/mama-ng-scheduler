package mama.ng.scheduler

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import groovy.time.TimeCategory
import spock.lang.Specification

@TestMixin(DomainClassUnitTestMixin)
@TestFor(MessageController)
@Mock([Message, Schedule])
class MessageControllerSpec extends Specification {

    Message message
    Schedule schedule

    def setup() {
        use (TimeCategory) {
            mockForConstraintsTests(Message)
            schedule = new Schedule(
                subscriptionId: "user-id",
                frequency: 5,
                cronDefinition: "0 9 * * 3",
                nextSend: new Date() + 2.days,
                endpoint: "http://test.endpoint.com"
            ).save(failOnError: true)
            message = new Message(
                schedule: schedule,
                nextSend: new Date() + 2.days
            )
        }
    }

    def cleanup() {
        message.delete()
        schedule.delete()
    }

    void "test show message"() {
        when:
            controller.show(message)

        then:
            response.status == 200
    }

    void "test show null message"() {
        when:
            controller.show(null)

        then:
            response.status == 404
    }

    void "test list message"() {
        when:
            controller.list()

        then:
            response.status == 200
    }

    void "test create message"() {
        use (TimeCategory) {
            given:
                Message message2 = new Message(
                    schedule: schedule,
                    nextSend: new Date() + 2.days
                )
                request.method = 'POST'

            when:
                controller.create(message2)

            then:
                response.status == 200
        }
    }

    void "test create error message"() {
        use (TimeCategory) {
            given:
                Message message2 = new Message(
                    schedule: schedule,
                    nextSend: null
                )
                request.method = 'POST'

            when:
                controller.create(message2)

            then:
                response.status == 400
        }
    }

    void "test update message"() {
        given:
            request.method = 'PUT'

        when:
            controller.update(message)

        then:
            response.status == 200
    }

    void "test update null message"() {
        given:
            request.method = 'PUT'

        when:
            controller.update(null)

        then:
            response.status == 404
    }

    void "test delete message"() {
        given:
            request.method = 'DELETE'

        when:
            controller.delete(message)

        then:
            response.status == 200
    }


    void "test delete null message"() {
        given:
            request.method = 'DELETE'

        when:
            controller.delete(null)

        then:
            response.status == 404
    }
}
