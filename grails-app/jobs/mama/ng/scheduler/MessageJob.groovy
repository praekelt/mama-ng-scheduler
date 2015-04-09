package mama.ng.scheduler

class MessageJob {
    def concurrent = false

    def httpRequestService

    def grailsApplication

    static triggers = {
        cron name:'cronTrigger', startDelay:2000, cronExpression: grailsApplication.config.scheduler.cron.expression.message }

    def group = "Message"

    /**
     * Finds all messages that have a next send date less than or equal to current time.
     * Message will be resent every hour until delivery is confirmed.
     * Execute post to schedule's endpoint.
     *
     * @return
     */
    def execute() {
        def then = new Date() + (grailsApplication.config.scheduler.time.message)
        def messages = Message.findAllByNextSendLessThanEquals(then)

        messages.each { Message message ->
            def schedule = message.schedule

            def success = httpRequestService.postText(schedule.endpoint)

            if (success) {
                log.info("Executed message [${message.id}]")
            } else {
                log.warn("Executing message [${message.id}] with endpoint [${schedule.endpoint}] failed")
            }
        }
    }
}
