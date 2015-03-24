package mama.ng.scheduler

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(CronParserService)
class CronParserServiceSpec extends Specification {


    def setup() {
    }

    def cleanup() {
    }

    void "test next date 0 9 * * 1 basic"() {
        given:
        Calendar calendar = new GregorianCalendar(2015,0,1,0,0,0);
        Date nextDate = service.determineNextDate("0 9 * * 1", new Date(calendar.getTimeInMillis()))

        Calendar expectedCalendar = new GregorianCalendar(2015,0,4,9,0,0);
        Date expectedDate = new Date(expectedCalendar.getTimeInMillis())

        expect:
        nextDate == expectedDate
    }

    void "test next date 0 9 * * 1 complex"() {
        given:
        Calendar calendar = new GregorianCalendar(2015,0,30,0,0,0);
        Date nextDate = service.determineNextDate("0 9 * * 1", new Date(calendar.getTimeInMillis()))

        Calendar expectedCalendar = new GregorianCalendar(2015,1,1,9,0,0);
        Date expectedDate = new Date(expectedCalendar.getTimeInMillis())

        expect:
        nextDate == expectedDate
    }

    void "test next date 0 9 2 * * basic"() {
        given:
        Calendar calendar = new GregorianCalendar(2015,0,1,0,0,0);
        Date nextDate = service.determineNextDate("0 9 2 * *", new Date(calendar.getTimeInMillis()))

        Calendar expectedCalendar = new GregorianCalendar(2015,0,2,9,0,0);
        Date expectedDate = new Date(expectedCalendar.getTimeInMillis())

        expect:
        nextDate == expectedDate
    }

    void "test next date 0 9 2 * * complex"() {
        given:
        Calendar calendar = new GregorianCalendar(2015,0,30,0,0,0);
        Date nextDate = service.determineNextDate("0 9 2 * *", new Date(calendar.getTimeInMillis()))

        Calendar expectedCalendar = new GregorianCalendar(2015,1,2,9,0,0);
        Date expectedDate = new Date(expectedCalendar.getTimeInMillis())

        expect:
        nextDate == expectedDate
    }

    void "test next date 0 11 * * 2,4 basic"() {
        given:
        Calendar calendar = new GregorianCalendar(2015,0,1,0,0,0);
        Date nextDate = service.determineNextDate("0 11 * * 2,4", new Date(calendar.getTimeInMillis()))

        Calendar expectedCalendar = new GregorianCalendar(2015,0,5,11,0,0);
        Date expectedDate = new Date(expectedCalendar.getTimeInMillis())

        expect:
        nextDate == expectedDate
    }

    void "test next date 0 11 * * 2,4 complex"() {
        given:
        Calendar calendar = new GregorianCalendar(2015,0,27,0,0,0);
        Date nextDate = service.determineNextDate("0 11 * * 2,4", new Date(calendar.getTimeInMillis()))

        Calendar expectedCalendar = new GregorianCalendar(2015,0,28,11,0,0);
        Date expectedDate = new Date(expectedCalendar.getTimeInMillis())

        expect:
        nextDate == expectedDate
    }

    void "test next date 0 10 1-7 * 2 basic"() {
        given:
        Calendar calendar = new GregorianCalendar(2015,0,1,0,0,0);
        Date nextDate = service.determineNextDate("0 10 1-7 * 2", new Date(calendar.getTimeInMillis()))

        Calendar expectedCalendar = new GregorianCalendar(2015,0,5,10,0,0);
        Date expectedDate = new Date(expectedCalendar.getTimeInMillis())

        expect:
        nextDate == expectedDate
    }

    void "test next date 0 10 1-7 * 2 complex"() {
        given:
        Calendar calendar = new GregorianCalendar(2015,0,27,0,0,0);
        Date nextDate = service.determineNextDate("0 10 1-7 * 2", new Date(calendar.getTimeInMillis()))

        Calendar expectedCalendar = new GregorianCalendar(2015,1,2,10,0,0);
        Date expectedDate = new Date(expectedCalendar.getTimeInMillis())

        expect:
        nextDate == expectedDate
    }

    void "test next date 0 10 1-30 * 2,4 complex"() {
        given:
        Calendar calendar = new GregorianCalendar(2015,0,6,0,0,0);
        Date nextDate = service.determineNextDate("0 10 1-30 * 2,4", new Date(calendar.getTimeInMillis()))

        Calendar expectedCalendar = new GregorianCalendar(2015,0,7,10,0,0);
        Date expectedDate = new Date(expectedCalendar.getTimeInMillis())

        expect:
        nextDate == expectedDate
    }
}
