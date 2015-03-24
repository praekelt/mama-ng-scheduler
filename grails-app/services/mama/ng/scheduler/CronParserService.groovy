package mama.ng.scheduler

import grails.transaction.Transactional
import org.quartz.CronExpression

@Transactional
class CronParserService {

    /**
     * Given a cron definition and date (default now), calculates the next time a message must be sent.
     *
     * @param cron
     * @param now
     * @return
     */
    Date determineNextDate(String cron, Date now = new Date()) {
        CronDefinition definition = new CronDefinition(cron)
        Date response = null

        /* QUARTZ parser does not allow days of week and days of month to both be defined.
         * This function checks for this condition and then, running through the days of month,
         * finds the best match for days of week.
         */
        if (definition.daysOfWeek && definition.daysOfMonth) {
            definition.daysOfMonth.each { Integer it ->
                Date date = new CronExpression(definition.translateToQuartzFormat(it.toString())).getNextValidTimeAfter(now)
                Calendar calendar = Calendar.getInstance()
                calendar.setTime(date)
                if (definition.daysOfWeek.contains(calendar.get(Calendar.DAY_OF_WEEK) as Integer)
                    && (response == null || date < response)) {
                    response = date
                }
            }
        } else {
            CronExpression expr = new CronExpression(definition.translateToQuartzFormat())
            response = expr.getNextValidTimeAfter(now)
        }
        return response
    }
}
