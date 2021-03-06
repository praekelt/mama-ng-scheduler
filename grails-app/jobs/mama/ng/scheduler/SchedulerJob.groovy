package mama.ng.scheduler

import grails.converters.JSON

class SchedulerJob {
    def concurrent = false

    def cronParserService
    def httpRequestService

    def grailsApplication

    def group = "Scheduler"

    /**
     * Finds all schedules that have a next send date less than or equal to current time.
     * Create a message for each schedule to ensure delivery.
     * Message will be resent until delivery is confirmed.
     * Update each schedule with a new nextSend date as well as send counter.
     * Execute post to schedule's endpoint.
     *
     * @return
     */
    def execute() {
        log.info("Schedule Job running...")
        Long timeout = System.getenv().SCHEDULER_TIME_MESSAGE ? System.getenv().SCHEDULER_TIME_MESSAGE.toInteger() * 1000 : (60 * 60 * 1000)
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(date.getTimeInMillis() + timeout);
        def then = new Date(date.getTimeInMillis())

        def schedules = Schedule.findAllByNextSendLessThanEquals(then)
        log.info("Found ${schedules.size()} schedules that need to be excecuted.")

        schedules.each { Schedule schedule ->
            if (schedule.sendCounter < schedule.frequency) {
                //Create a new message so this message is repeated every hour until delivery is confirmed
                Message message = new Message(schedule: schedule, nextSend: schedule.nextSend)
                message.save(flush: true, failOnError: true)
                schedule.sendCounter = schedule.sendCounter++
                log.debug("Created message [${message.id}] from schedule [${schedule.id}]")

                //Calculate when this schedule needs to be executed next.
                schedule.nextSend = cronParserService.determineNextDate(schedule.cronDefinition, then)
                schedule.save(failOnError: true, flush: true)
                log.debug("Changed schedule [${schedule.id}] - nextSend: ${schedule.nextSend}")

                def body = [
                        "schedule-id": schedule.id,
                        "message-id": message.id,
                        "send-counter": schedule.sendCounter
                ]

                def success = httpRequestService.postText(schedule.endpoint, body)

                if (success) {
                    log.debug("Executed schedule [${schedule.id}]")
                } else {
                    log.warn("Executing schedule [${schedule.id}] with endpoint [${schedule.endpoint}] failed")
                }
            }
        }
    }
}
