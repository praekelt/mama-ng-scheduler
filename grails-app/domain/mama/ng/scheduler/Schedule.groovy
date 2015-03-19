package mama.ng.scheduler

class Schedule {

    static hasMany = [messages: Message]

    String id = UUID.randomUUID().toString()
    String subscriptionId
    Integer frequency
    Integer sendCounter = 0
    String cronDefinition
    Date nextSend
    String endpoint
    Date dateCreated

    static constraints = {
        id generator:'assigned'
        subscriptionId(blank: false, nullable: false)
        frequency(min: 1, nullable: false)
        sendCounter(min: 0, nullable: false)
        cronDefinition(matches: CronDefinition.REGEX,
            nullable: false, blank: false)
        nextSend(min: new Date(), nullable: false)
        endpoint(url: true, blank: false, nullable: false)
    }
}
