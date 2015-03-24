package mama.ng.scheduler

class User {

    String id = UUID.randomUUID().toString()
    String username
    String passwordHash
    String apiKey
    UserType type = UserType.GENERAL
    Date dateCreated

    static constraints = {
        id generator:'assigned'
        username(blank: false, nullable: false, unique: true)
        passwordHash(blank: false, nullable: false)
        apiKey(blank: false, nullable: true, unique: true)
    }

    static mapping = { table 'service_users' }

    enum UserType {
        GENERAL,
        SUPER
    }

    def setPasswordHash(String passwordHash) {
        if (!this.passwordHash.equals(passwordHash)) {
            this.passwordHash = passwordHash.encodeAsBase64()
        }
    }
}
