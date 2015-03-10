package mama.ng.scheduler

class Message {

    static hasOne = [schedule: Schedule]

    String id = UUID.randomUUID().toString()
    Date nextSend
    Date created

    static constraints = {
        id generator:'assigned'
        schedule(nullable: false)
        nextSend(min: new Date(), nullable: false)
    }
}
