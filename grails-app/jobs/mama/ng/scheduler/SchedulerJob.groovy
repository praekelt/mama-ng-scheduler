package mama.ng.scheduler

import groovy.time.TimeCategory
import groovyx.net.http.ContentEncoding
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import groovyx.net.http.Method

class SchedulerJob {
    def concurrent = false

    def cronParserService

    static triggers = {
        cron name:'cronTrigger', startDelay:1000, cronExpression: '0 0 * * * ?' } //execute every hour on the hour

    def group = "Scheduler"

    def execute() {
        use (TimeCategory) {
            def then = new Date() + 1.hour
            def schedules = Schedule.findAllByNextSendLessThanEquals(then)

            schedules.each { Schedule schedule ->
                Message.findAllBySchedule(schedule).each { it.delete() }

                schedule.nextSend = cronParserService.determineNextDate(schedule.cronDefinition, then)
                schedule.frequency = schedule.frequency--
                schedule.sendCounter = schedule.sendCounter++
                schedule.save(failOnError: true, flush: true)
                log.info("Changed schedule [${schedule.id}] - nextSend: ${schedule.nextSend}")

                Message message = new Message(schedule: schedule, nextSend: schedule.nextSend)
                message.save(flush: true, failOnError: true)
                log.info("Created message [${message.id}] from schedule [${schedule.id}]")

                def success = postText(schedule.endpoint)

                if (success) {
                    log.info("Executed schedule [${schedule.id}]")
                } else {
                    log.warn("Executing schedule [${schedule.id}] with endpoint [${schedule.endpoint}] failed")
                }
            }

            def messages = Message.findAllByNextSendLessThanEquals(then)

            messages.each { Message message ->
                def schedule = message.schedule
                message.nextSend = cronParserService.determineNextDate(schedule.cronDefinition, then)
                message.save(failOnError: true, flush: true)
                log.info("Changed message [${message.id}] - nextSend: ${message.nextSend}")

                def success = postText(schedule.endpoint)

                if (success) {
                    log.info("Executed message [${message.id}]")
                } else {
                    log.warn("Executing message [${message.id}] with endpoint [${message.schedule.endpoint}] failed")
                }
            }
        }
    }

    Boolean postText(String baseUrl, method = Method.POST) {
        try {
            def ret = null
            def http = new HTTPBuilder(baseUrl)
            http.contentEncoding = ContentEncoding.Type.DEFLATE

            // perform a POST request, expecting TEXT response
            http.request(method, ContentType.TEXT) {
                headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'

                // response handler for a success response code
                response.success = { resp, reader ->
                    ret = true
                }
            }
            return ret

        } catch (HttpResponseException ex) {
            ex.printStackTrace()
            return null
        } catch (ConnectException ex) {
            ex.printStackTrace()
            return null
        }
    }
}
