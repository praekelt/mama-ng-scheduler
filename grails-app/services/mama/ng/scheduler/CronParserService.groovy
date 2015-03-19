package mama.ng.scheduler

import grails.transaction.Transactional

@Transactional
class CronParserService {

    def determineNextDate(String cron) {
        CronDefinition cronDefinition =  new CronDefinition(cron)
        Date now = new Date()
    }
}
