package mama.ng.scheduler

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import groovy.time.TimeCategory
import spock.lang.Specification

@TestMixin(DomainClassUnitTestMixin)
@TestFor(ScheduleController)
@Mock([Schedule])
class ScheduleControllerSpec extends Specification {

    Schedule schedule

    def setup() {
        use (TimeCategory) {
            mockForConstraintsTests(Schedule)
            schedule = new Schedule(
                subscriptionId: "user-id",
                frequency: 5,
                cronDefinition: "0 9 * * 3",
                nextSend: new Date() + 2.days,
                endpoint: "http://test.endpoint.com"
            ).save(failOnError: true)
        }
    }

    def cleanup() {
        schedule.delete()
    }

    void "test show schedule"() {
        when:
            controller.show(schedule)

        then:
            response.status == 200
    }

    void "test show null schedule"() {
        when:
            controller.show(null)

        then:
            response.status == 404
    }

    void "test list schedule"() {
        when:
            controller.list()

        then:
            response.status == 200
    }

    void "test create schedule"() {
        use (TimeCategory) {
            given:
                Schedule schedule2 = new Schedule(
                    subscriptionId: "user-id",
                    frequency: 5,
                    cronDefinition: "0 9 * * 3",
                    nextSend: new Date() + 2.days,
                    endpoint: "http://test.endpoint.com"
                )
                request.method = 'POST'

            when:
                controller.create(schedule2)

            then:
                response.status == 200
        }
    }

    void "test create error schedule"() {
        use (TimeCategory) {
            given:
                Schedule schedule2 = new Schedule(
                    subscriptionId: "user-id",
                    frequency: 5,
                    cronDefinition: "0 9 * * 3",
                    nextSend: new Date() + 2.days,
                    endpoint: null
                )
                request.method = 'POST'

            when:
                controller.create(schedule2)

            then:
                response.status == 400
        }
    }

    void "test update schedule"() {
        given:
            request.method = 'PUT'

        when:
            controller.update(schedule)

        then:
            response.status == 200
    }

    void "test update null schedule"() {
        given:
            request.method = 'PUT'

        when:
            controller.update(null)

        then:
            response.status == 404
    }

    void "test delete schedule"() {
        given:
            request.method = 'DELETE'

        when:
            controller.delete(schedule)

        then:
            response.status == 200
    }


    void "test delete null schedule"() {
        given:
            request.method = 'DELETE'

        when:
            controller.delete(null)

        then:
            response.status == 404
    }
}
