package mama.ng.scheduler

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import groovy.time.TimeCategory
import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestMixin(DomainClassUnitTestMixin)
@TestFor(Message)
@Mock([Schedule, Message])
class MessageSpec extends Specification {

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

    void "test null schedule"() {
        given:
            message.schedule = null
            message.save()
        expect:
            message.hasErrors()
            message.errors["schedule"].equals("nullable")
    }

    void "test incorrect type schedule"() {
        when:
            message.schedule = "schedule-id"
            message.save()
        then:
            GroovyCastException expectedException = thrown()
            expectedException.message.equals("Cannot cast object 'schedule-id' with class 'java.lang.String' to class 'mama.ng.scheduler.Schedule'")
    }

    void "test past nextSend"() {
        given:
            use (TimeCategory) {
                message.nextSend = new Date() - 1.day
                message.save()
            }
        expect:
            message.hasErrors()
            message.errors["nextSend"].equals("min")
    }

    void "test null nextSend"() {
        given:
            message.nextSend = null
            message.save()
        expect:
            message.hasErrors()
            message.errors["nextSend"].equals("nullable")
    }

    void "test incorrectType nextSend"() {
        when:
            message.nextSend = "tomorrow"
            message.save()
        then:
            GroovyCastException expectedException = thrown()
            expectedException.message.equals("Cannot cast object 'tomorrow' with class 'java.lang.String' to class 'java.util.Date'")
    }

    void "test well formed message"() {
        given:
            use (TimeCategory) {
                message.schedule = schedule
                message.nextSend = new Date() + 2.days
                message.save()
            }
        expect:
            !message.hasErrors()
    }
}
