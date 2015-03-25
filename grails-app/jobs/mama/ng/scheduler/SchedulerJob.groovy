package mama.ng.scheduler


class SchedulerJob
{
    def startDelay = 60000 //every minute
    def timeout = 1000

    def group = "MyGroup"

    def execute() {
        print "Job run!"
    }

}
