package mama.ng.scheduler

import groovy.time.TimeCategory

class SchedulerJob {
    def concurrent = false

    def cronParserService
    def httpRequestService

    static triggers = {
        cron name:'cronTrigger', startDelay:1000, cronExpression: '0 0 * * * ?' } //execute every hour on the hour

    def group = "Scheduler"

    /**
     * Finds all schedules that have a next send date less than or equal to current time.
     * Create a message for each schedule to ensure delivery.
     * Message will be resent every hour until delivery is confirmed.
     * Update each schedule with a new nextSend date as well as send counter.
     * Execute post to schedule's endpoint.
     * Delete schedule if send counter equals frequency.
     *
     * @return
     */
    def execute() {
        use (TimeCategory) {
            def then = new Date() + 1.hour
            def schedules = Schedule.findAllByNextSendLessThanEquals(then)

            schedules.each { Schedule schedule ->

                //Create a new message so this message is repeated every hour until delivery is confirmed
                Message message = new Message(schedule: schedule, nextSend: schedule.nextSend)
                message.save(flush: true, failOnError: true)
                log.debug("Created message [${message.id}] from schedule [${schedule.id}]")

                //Calculate when this schedule needs to be executed next.
                schedule.nextSend = cronParserService.determineNextDate(schedule.cronDefinition, then)
                schedule.save(failOnError: true, flush: true)
                log.debug("Changed schedule [${schedule.id}] - nextSend: ${schedule.nextSend}")

                def success = httpRequestService.postText(schedule.endpoint)

                if (success) {
                    log.debug("Executed schedule [${schedule.id}]")
                    schedule.sendCounter = schedule.sendCounter++
                    if (schedule.sendCounter == schedule.frequency) {
                        schedule.delete(flush: true)
                    } else {
                        schedule.save(failOnError: true, flush: true)
                    }
                } else {
                    log.warn("Executing schedule [${schedule.id}] with endpoint [${schedule.endpoint}] failed")
                }
            }

        }
    }
}