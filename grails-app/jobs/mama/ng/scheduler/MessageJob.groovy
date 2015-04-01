package mama.ng.scheduler

import groovy.time.TimeCategory


class MessageJob {
    def concurrent = false

    def httpRequestService

    static triggers = {
        cron name:'cronTrigger', startDelay:2000, cronExpression: '0 0 * * * ?' } //execute every hour on the hour

    def group = "Message"

    /**
     * Finds all messages that have a next send date less than or equal to current time.
     * Message will be resent every hour until delivery is confirmed.
     * Execute post to schedule's endpoint.
     *
     * @return
     */
    def execute() {
        use (TimeCategory) {
            def then = new Date() + 1.hour
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
}
