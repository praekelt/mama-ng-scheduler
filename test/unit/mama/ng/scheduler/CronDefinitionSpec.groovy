package mama.ng.scheduler

import spock.lang.Specification

class CronDefinitionSpec extends Specification {

    def cronParserService

    def setup() {

    }

    def cleanup() {

    }

    void "test cron regex 0 9 * * 3"() {
        given:
        CronDefinition cronDefinition = new CronDefinition("0 9 * * 3")

        expect:
        cronDefinition.minutes.size() == 1
        cronDefinition.minutes.contains(0)

        cronDefinition.hours.size() == 1
        cronDefinition.hours.contains(9)

        cronDefinition.daysOfMonth == null
        cronDefinition.months == null

        cronDefinition.daysOfWeek.size() == 1
        cronDefinition.daysOfWeek.contains(3)
    }

    void "test cron regex 0 9 2 * *"() {
        given:
        CronDefinition cronDefinition = new CronDefinition("0 9 2 * *")

        expect:
        cronDefinition.minutes.size() == 1
        cronDefinition.minutes.contains(0)

        cronDefinition.hours.size() == 1
        cronDefinition.hours.contains(9)

        cronDefinition.daysOfMonth.size() == 1
        cronDefinition.daysOfMonth.contains(2)

        cronDefinition.months == null
        cronDefinition.daysOfWeek == null
    }

    void "test cron regex 0 10 1-7 * 1"() {
        given:
        CronDefinition cronDefinition = new CronDefinition("0 10 1-3 * 1")

        expect:
        cronDefinition.minutes.size() == 1
        cronDefinition.minutes.contains(0)

        cronDefinition.hours.size() == 1
        cronDefinition.hours.contains(10)

        cronDefinition.daysOfMonth.size() == 3
        cronDefinition.daysOfMonth.contains(1)
        cronDefinition.daysOfMonth.contains(2)
        cronDefinition.daysOfMonth.contains(3)

        cronDefinition.months == null

        cronDefinition.daysOfWeek.size() == 1
        cronDefinition.daysOfWeek.contains(1)
    }

    void "test cron regex 0 11 * * 1,4"() {
        given:
        CronDefinition cronDefinition = new CronDefinition("0 11 * * 1,4")

        expect:
        cronDefinition.minutes.size() == 1
        cronDefinition.minutes.contains(0)

        cronDefinition.hours.size() == 1
        cronDefinition.hours.contains(11)

        cronDefinition.daysOfMonth == null
        cronDefinition.months == null

        cronDefinition.daysOfWeek.size() == 2
        cronDefinition.daysOfWeek.contains(1)
        cronDefinition.daysOfWeek.contains(4)
    }

    void "test cron regex 1-2 11 * * 1,4"() {
        given:
        CronDefinition cronDefinition = new CronDefinition("1-2 11 * * 1,4")

        expect:
        cronDefinition.minutes.size() == 2
        cronDefinition.minutes.contains(1)
        cronDefinition.minutes.contains(2)

        cronDefinition.hours.size() == 1
        cronDefinition.hours.contains(11)

        cronDefinition.daysOfMonth == null
        cronDefinition.months == null

        cronDefinition.daysOfWeek.size() == 2
        cronDefinition.daysOfWeek.contains(1)
        cronDefinition.daysOfWeek.contains(4)
    }

    void "test cron regex 0 11-12 * * 1,4,3"() {
        given:
        CronDefinition cronDefinition = new CronDefinition("0 11-12 * * 1,4,3")

        expect:
        cronDefinition.minutes.size() == 1
        cronDefinition.minutes.contains(0)

        cronDefinition.hours.size() == 2
        cronDefinition.hours.contains(11)
        cronDefinition.hours.contains(12)

        cronDefinition.daysOfMonth == null
        cronDefinition.months == null

        cronDefinition.daysOfWeek.size() == 3
        cronDefinition.daysOfWeek.contains(1)
        cronDefinition.daysOfWeek.contains(4)
        cronDefinition.daysOfWeek.contains(3)
    }

    void "test cron regex 0 11 * 1-2 "() {
        given:
        CronDefinition cronDefinition = new CronDefinition("0 11 * 1-2 1")

        expect:
        cronDefinition.minutes.size() == 1
        cronDefinition.minutes.contains(0)

        cronDefinition.hours.size() == 1
        cronDefinition.hours.contains(11)

        cronDefinition.daysOfMonth == null

        cronDefinition.months.size() == 2
        cronDefinition.months.contains(1)
        cronDefinition.months.contains(2)

        cronDefinition.daysOfWeek.size() == 1
        cronDefinition.daysOfWeek.contains(1)
    }

    void "test cron regex * * * * 3-1 "() {
        given:
        CronDefinition cronDefinition = new CronDefinition("* * * * 3-1")

        expect:
        cronDefinition.minutes == null
        cronDefinition.hours == null
        cronDefinition.daysOfMonth == null
        cronDefinition.months == null
        cronDefinition.daysOfWeek == null
    }

    void "test get values dash"() {
        given:
        List list = CronDefinition.getListFromString("1-5")

        expect:
        list.size() == 5
        list.contains(1)
        list.contains(2)
        list.contains(3)
        list.contains(4)
        list.contains(5)
    }

    void "test get values one comma"() {
        given:
        List list = CronDefinition.getListFromString("1,5")

        expect:
        list.size() == 2
        list.contains(1)
        list.contains(5)
    }

    void "test get values two commas"() {
        given:
        List list = CronDefinition.getListFromString("1,5,12")

        expect:
        list.size() == 3
        list.contains(1)
        list.contains(5)
        list.contains(12)
    }

    void "test get values single"() {
        given:
        List list = CronDefinition.getListFromString("15")

        expect:
        list.size() == 1
        list.contains(15)
    }

    void "test translation to QUARTZ format 0 10 1-3 * 1"() {
        given:
        CronDefinition cronDefinition = new CronDefinition("0 10 1-3 * 1")
        String newDefinition = cronDefinition.translateToQuartzFormat()

        expect:
        newDefinition.equals("0 0 10 1,2,3 * ?")
    }

    void "test translation to QUARTZ format 0 9-10 * * 3,5"() {
        given:
        CronDefinition cronDefinition = new CronDefinition("0 9-10 * * 3,5")
        String newDefinition = cronDefinition.translateToQuartzFormat()

        expect:
        newDefinition.equals("0 0 9,10 ? * 3,5")
    }

}
