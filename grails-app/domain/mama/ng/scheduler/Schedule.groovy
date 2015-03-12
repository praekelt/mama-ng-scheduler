package mama.ng.scheduler

class Schedule {

    static hasMany = [messages: Message]

    String id = UUID.randomUUID().toString()
    String userId
    Integer frequency
    Integer sendCounter = 0
    String cronDefinition
    Date nextSend
    String endpoint
    Date dateCreated

    static constraints = {
        id generator:'assigned'
        userId(blank: false, nullable: false)
        frequency(min: 1, nullable: false)
        sendCounter(min: 0, nullable: false)
        cronDefinition(matches: '^(((([1-5]?[0-9])(\\,|\\-))*([1-5]?[0-9]))|\\*)\\ ' + //min (0 - 59)
            '((((([1]?[0-9])|([2][0-3]))(\\,|\\-))*(([1]?[0-9])|([2][0-3])))|\\*)\\ ' + //hour (0 - 23)
            '((((([1-9])|([1-2][0-9])|([3][0-1]))(\\,|\\-))*(([1-9])|([1-2][0-9])|([3][0-1])))|\\*)\\ ' + //day of month (1 - 31)
            '((((([0-9])|([1][0-2]))(\\,|\\-))*(([0-9])|([1][2])))|\\*)\\ ' + //month (1 - 12)
            '(((([0-6])(\\,|\\-))*([0-6]))|\\*)$', //day of week (0 - 6) (0 to 6 are Sunday to Saturday, or use names; 7 is Sunday, the same as 0)
            nullable: false, blank: false)
        nextSend(min: new Date(), nullable: false)
        endpoint(url: true, blank: false, nullable: false)
    }
}
