package mama.ng.scheduler

/**
 * An authenticated user's permissions. Note that user might be null if the master credentials were used to
 * authenticate.
 */
class AuthToken {

    enum IdentifiedBy { API_KEY, PASSWORD, TICKET }

    final String basicAuthHeader
    final String userAgent
    final String ipAddress
    final User user
    final IdentifiedBy identifiedBy
    final boolean rememberMe
    final String newTicket
    final long expiresAt

    AuthToken(String basicAuthHeader, String userAgent, String ipAddress, User user,
              IdentifiedBy identifiedBy, boolean rememberMe, String newTicket) {
        this.basicAuthHeader = basicAuthHeader
        this.userAgent = userAgent
        this.ipAddress = ipAddress
        this.user = user
        this.identifiedBy = identifiedBy
        this.rememberMe = rememberMe
        this.newTicket = newTicket
        this.expiresAt = System.currentTimeMillis() + (long)(2 * 60 * 1000L * (0.75 + Math.random() / 2))
    }

    boolean isMaster() { user == null }

    boolean isExpired() { user && System.currentTimeMillis() > expiresAt }

    @Override
    String toString() {
        StringBuilder b = new StringBuilder()
        b.append(user ? user.username : "Chess Lord")
        if (identifiedBy) b.append(' by ').append(identifiedBy)
        return b.toString()
    }
}
