package mama.ng.scheduler


class SchedulerJob {
    def concurrent = false

    static triggers = {
        simple name: 'myFirstJobTrigger', startDelay: 1000, repeatInterval: 1000 }

    def group = "MyGroup"

    def execute(){
        println "MyFirstJob run!"
    }
}
