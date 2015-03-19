package mama.ng.scheduler

import java.util.regex.Matcher
import java.util.regex.Pattern

class CronDefinition {

    List<Integer> minutes
    List<Integer> hours
    List<Integer> daysOfMonth
    List<Integer> months
    List<Integer> daysOfWeek

    int minute = 1
    int hour = 7
    int dayOfMonth = 17
    int month = 29
    int dayOfWeek = 39

    final static String REGEX = '^(((([1-5]?[0-9])(\\,|\\-))*([1-5]?[0-9]))|\\*)\\ ' + //min (0 - 59)
        '((((([1]?[0-9])|([2][0-3]))(\\,|\\-))*(([1]?[0-9])|([2][0-3])))|\\*)\\ ' + //hour (0 - 23)
        '((((([1-9])|([1-2][0-9])|([3][0-1]))(\\,|\\-))*(([1-9])|([1-2][0-9])|([3][0-1])))|\\*)\\ ' + //day of month (1 - 31)
        '((((([0-9])|([1][0-2]))(\\,|\\-))*(([0-9])|([1][2])))|\\*)\\ ' + //month (1 - 12)
        '(((([0-6])(\\,|\\-))*([0-6]))|\\*)$' //day of week (0 - 6) (0 to 6 are Sunday to Saturday, or use names; 7 is Sunday, the same as 0)

    private Pattern pattern = Pattern.compile(REGEX)

    CronDefinition(String cronDefinition) {
        Matcher matcher = pattern.matcher(cronDefinition)
        assert (matcher.matches())

        minutes = getListFromString(matcher.group(minute))
        hours = getListFromString(matcher.group(hour))
        daysOfMonth = getListFromString(matcher.group(dayOfMonth))
        months = getListFromString(matcher.group(month))
        daysOfWeek = getListFromString(matcher.group(dayOfWeek))
    }

    /**
     * Converts String values such as "1-5" and "1,2,5" to a list of Integers
     *
     * @param value
     * @return
     */
    static List getListFromString(String value) {
        def response = []
        if (value.contains('-')) {
            def split = value.split('-')
            def start = split[0] as Integer
            def end = split[1] as Integer

            if (start > end) return null

            for (int i = start; i <= end; i++) {
                response.add(i)
            }
        } else if (value.contains(',')) {
            value.split(',').each {
                response.add(it as Integer)
            }
        } else if (value.contains('*')) {
            response = null
        } else {
            response.add(value as Integer)
        }
        return response
    }
}
