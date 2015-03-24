package mama.ng.scheduler

import grails.transaction.Transactional
import org.quartz.CronExpression

@Transactional
class CronParserService {

    Date determineNextDate(String cron, Date now) {
        CronDefinition definition = new CronDefinition(cron)
        Date response = null

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
            CronExpression expr = new CronExpression(definition.translateToQuartzFormat()) // add seconds
            response = expr.getNextValidTimeAfter(now)
        }
        return response
    }
}
