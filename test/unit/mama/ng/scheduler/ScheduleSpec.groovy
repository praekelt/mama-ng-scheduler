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
@TestFor(Schedule)
@Mock([Schedule])
class ScheduleSpec extends Specification {

    Schedule schedule

    def setup() {
        use (TimeCategory) {
            schedule = new Schedule(
                userId: "user-id",
                frequency: 5,
                cronDefinition: "0 9 * * 3",
                nextSend: new Date() + 2.days,
                endpoint: "http://test.endpoint.com"
            )
        }
    }

    def cleanup() {
        schedule.delete()
    }

    void "test everything works"() {
        use (TimeCategory) {
            schedule.userId = "user-id"
            schedule.frequency = 5
            schedule.cronDefinition = "0 9 * * 3"
            schedule.nextSend = new Date() + 2.days
            schedule.endpoint = "http://test.endpoint.com"
        }
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test blank user id"() {
        given:
        schedule.userId = ""
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["userId"].toString().contains("blank")
    }

    void "test null user id"() {
        given:
        schedule.userId = null
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["userId"].toString().contains("nullable")
    }

    void "test null frequency"() {
        given:
        schedule.frequency = null
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["frequency"].toString().contains("nullable")
    }

    void "test negative frequency"() {
        given:
        schedule.frequency = -1
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["frequency"].toString().contains("min")
    }

    void "test incorrect type frequency"() {
        when:
        schedule.frequency = "frequency"
        schedule.save()
        then:
        GroovyCastException expectedException = thrown()
        expectedException.message.equals("Cannot cast object 'frequency' with class 'java.lang.String' to class 'java.lang.Integer'")
    }

    void "test null send counter"() {
        given:
        schedule.sendCounter = null
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["sendCounter"].toString().contains("nullable")
    }

    void "test negative send counter"() {
        given:
        schedule.sendCounter = -1
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["sendCounter"].toString().contains("min")
    }

    void "test incorrect type send counter"() {
        when:
        schedule.sendCounter = "counter"
        schedule.save()
        then:
        GroovyCastException expectedException = thrown()
        expectedException.message.equals("Cannot cast object 'counter' with class 'java.lang.String' to class 'java.lang.Integer'")
    }

    void "test past nextSend"() {
        given:
        use (TimeCategory) {
            schedule.nextSend = new Date() - 1.day
            schedule.save()
        }
        expect:
        schedule.hasErrors()
        schedule.errors["nextSend"].toString().contains("min")
    }

    void "test null nextSend"() {
        given:
        schedule.nextSend = null
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["nextSend"].toString().contains("nullable")
    }

    void "test incorrectType nextSend"() {
        when:
        schedule.nextSend = "tomorrow"
        schedule.save()
        then:
        GroovyCastException expectedException = thrown()
        expectedException.message.equals("Cannot cast object 'tomorrow' with class 'java.lang.String' to class 'java.util.Date'")
    }

    void "test blank endpoint"() {
        given:
        schedule.endpoint = ""
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["endpoint"].toString().contains("blank")
    }

    void "test bad format endpoint"() {
        given:
        schedule.endpoint = "something that's not an url"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["endpoint"].toString().contains("url")
    }

    void "test null endpoint"() {
        given:
        schedule.endpoint = null
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["endpoint"].toString().contains("nullable")
    }

    void "test cron definition minute 1"() {
        // Out of range
        given:
        schedule.cronDefinition = "60 * * * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition minute 2"() {
        // Illegal character
        given:
        schedule.cronDefinition = "0+48 * * * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition minute 3"() {
        // Too many spaces
        given:
        schedule.cronDefinition = "0 - 5 * * * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition minute 4"() {
        // Range out of range
        given:
        schedule.cronDefinition = "0-80 * * * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition minute 5"() {
        // Single digit correct
        given:
        schedule.cronDefinition = "5 * * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition minute 6"() {
        // Double digit correct
        given:
        schedule.cronDefinition = "25 * * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition minute 7"() {
        // Single digit range correct
        given:
        schedule.cronDefinition = "1-5 * * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition minute 8"() {
        // Single/Double digit range correct
        given:
        schedule.cronDefinition = "1-25 * * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition minute 9"() {
        // Double digit range correct
        given:
        schedule.cronDefinition = "15-25 * * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition minute 10"() {
        // Comma separated numbers
        given:
        schedule.cronDefinition = "1,3 * * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition minute 11"() {
        // Comma separated numbers
        given:
        schedule.cronDefinition = "1,3,5 * * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition hour 1"() {
        // Out of range
        given:
        schedule.cronDefinition = "* 60 * * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition hour 2"() {
        // Illegal character
        given:
        schedule.cronDefinition = "* 0+48 * * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition hour 3"() {
        // Too many spaces
        given:
        schedule.cronDefinition = "* 0 - 5 * * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition hour 4"() {
        // Range out of range
        given:
        schedule.cronDefinition = "* 0-80 * * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition hour 5"() {
        // Single digit correct
        given:
        schedule.cronDefinition = "* 5 * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition hour 6"() {
        // Double digit correct
        given:
        schedule.cronDefinition = "* 21 * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition hour 7"() {
        // Single digit range correct
        given:
        schedule.cronDefinition = "* 1-5 * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition hour 8"() {
        // Single/Double digit range correct
        given:
        schedule.cronDefinition = "* 1-19 * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition hour 9"() {
        // Double digit range correct
        given:
        schedule.cronDefinition = "* 15-19 * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition hour 10"() {
        // Comma separated numbers
        given:
        schedule.cronDefinition = "* 1,3 * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition hour 11"() {
        // Comma separated numbers
        given:
        schedule.cronDefinition = "* 1,3,5 * * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of month 1"() {
        // Out of range
        given:
        schedule.cronDefinition = "* * 60 * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition day of month 2"() {
        // Illegal character
        given:
        schedule.cronDefinition = "* * 1+15 * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition day of month 3"() {
        // Too many spaces
        given:
        schedule.cronDefinition = "* * 1 - 5 * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition day of month 4"() {
        // Range out of range
        given:
        schedule.cronDefinition = "* * 0-80 * *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition day of month 5"() {
        // Single digit correct
        given:
        schedule.cronDefinition = "* * 5 * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of month 6"() {
        // Double digit correct
        given:
        schedule.cronDefinition = "* * 31 * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of month 7"() {
        // Single digit range correct
        given:
        schedule.cronDefinition = "* * 1-5 * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of month 8"() {
        // Single/Double digit range correct
        given:
        schedule.cronDefinition = "* * 1-19 * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of month 9"() {
        // Double digit range correct
        given:
        schedule.cronDefinition = "* * 15-19 * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of month 10"() {
        // Comma separated numbers
        given:
        schedule.cronDefinition = "* * 1,3 * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of month 11"() {
        // Comma separated numbers
        given:
        schedule.cronDefinition = "* * 1,3,5 * *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition month 1"() {
        // Out of range
        given:
        schedule.cronDefinition = "* * * 60 *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition month 2"() {
        // Illegal character
        given:
        schedule.cronDefinition = "* * * 1+12 *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition month 3"() {
        // Too many spaces
        given:
        schedule.cronDefinition = "* * * 1 - 5 *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition month 4"() {
        // Range out of range
        given:
        schedule.cronDefinition = "* * * 0-80 *"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition month 5"() {
        // Single digit correct
        given:
        schedule.cronDefinition = "* * * 5 *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition month 6"() {
        // Double digit correct
        given:
        schedule.cronDefinition = "* * * 12 *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition month 7"() {
        // Single digit range correct
        given:
        schedule.cronDefinition = "* * * 1-9 *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition month 8"() {
        // Single/Double digit range correct
        given:
        schedule.cronDefinition = "* * * 1-12 *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition month 9"() {
        // Double digit range correct
        given:
        schedule.cronDefinition = "* * * 11-12 *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition month 10"() {
        // Comma separated numbers
        given:
        schedule.cronDefinition = "* * * 1,3 *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition month 11"() {
        // Comma separated numbers
        given:
        schedule.cronDefinition = "* * * 1,3,5 *"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of week 1"() {
        // Out of range
        given:
        schedule.cronDefinition = "* * * * 7"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition day of week 2"() {
        // Illegal character
        given:
        schedule.cronDefinition = "* * * * 1+6"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition day of week 3"() {
        // Too many spaces
        given:
        schedule.cronDefinition = "* * * * 1 - 5"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition day of week 4"() {
        // Range out of range
        given:
        schedule.cronDefinition = "* * * * 0-80"
        schedule.save()
        expect:
        schedule.hasErrors()
        schedule.errors["cronDefinition"].toString().contains("matches")
    }

    void "test cron definition day of week 5"() {
        // Single digit correct
        given:
        schedule.cronDefinition = "* * * * 5"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of week 6"() {
        // Single digit range correct
        given:
        schedule.cronDefinition = "* * * * 1-3"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of week 7"() {
        // Comma separated numbers
        given:
        schedule.cronDefinition = "* * * * 1,3"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }

    void "test cron definition day of week 8"() {
        // Comma separated numbers
        given:
        schedule.cronDefinition = "* * * * 1,3,5"
        schedule.save()
        expect:
        !schedule.hasErrors()
    }
}
