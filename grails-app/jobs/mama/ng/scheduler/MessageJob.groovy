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
        Long timeout = System.getenv().SCHEDULER_TIME_SCHEDULE ? System.getenv().SCHEDULER_TIME_SCHEDULE.toInteger() * 1000 : (60 * 60 * 1000)
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(date.getTimeInMillis() + timeout);
        def then = new Date(date.getTimeInMillis())

        def messages = Message.findAllByNextSendLessThanEquals(then)
        log.info("Found ${messages.size()} messages that need to be excecuted.")

        messages.each { Message message ->
            def schedule = message.schedule

            def body = [
                    "schedule-id" : schedule.id,
                    "message-id"  : message.id,
                    "send-counter": schedule.sendCounter
            ]

            def success = httpRequestService.postText(schedule.endpoint, body)

            if (success) {
                log.info("Executed message [${message.id}]")
            } else {
                log.warn("Executing message [${message.id}] with endpoint [${schedule.endpoint}] failed")
            }
        }
    }
}
