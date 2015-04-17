package mama.ng.scheduler

import grails.converters.JSON

class MessageJob {
    def concurrent = false

    def httpRequestService
    def grailsApplication

    def group = "Message"

    /**
     * Finds all messages that have a next send date less than or equal to current time.
     * Message will be resent every hour until delivery is confirmed.
     * Execute post to schedule's endpoint.
     *
     * @return
     */
    def execute() {
        log.info("Message Job running...")
        def then = new Date() + (grailsApplication.config.scheduler.time.message)
        def messages = Message.findAllByNextSendLessThanEquals(then)
        log.info("Found ${messages.size()} messages that need to be excecuted.")

        messages.each { Message message ->
            def schedule = message.schedule

            def body = [
                    "schedule-id" : schedule.id,
                    "message-id"  : message.id,
                    "send-counter": schedule.sendCounter
            ]

            def success = httpRequestService.postText(schedule.endpoint, body as JSON)

            if (success) {
                log.info("Executed message [${message.id}]")
            } else {
                log.warn("Executing message [${message.id}] with endpoint [${schedule.endpoint}] failed")
            }
        }
    }
}
