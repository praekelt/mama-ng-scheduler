import mama.ng.scheduler.MessageJob
import mama.ng.scheduler.SchedulerJob

class BootStrap {

    def init = { servletContext ->
        String scheduleCron = System.getenv().SCHEDULER_TIME_SCHEDULE?:'0 0 * * * ?'
        String messageCron = System.getenv().SCHEDULER_TIME_MESSAGE?:'0 0 * * * ?'
        SchedulerJob.schedule(scheduleCron, null)
        MessageJob.schedule(messageCron, null)
    }
    def destroy = {
    }
}
