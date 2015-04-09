package mama.ng.scheduler

import grails.transaction.Transactional

import javax.annotation.PostConstruct
import java.security.SecureRandom
import java.util.concurrent.ConcurrentHashMap

@Transactional
class SecurityService {

    def grailsApplication

    private SecureRandom random

    protected final Map<String, AuthToken> authCache = new ConcurrentHashMap<String, AuthToken>()

    @PostConstruct
    protected void init() {
        random = new SecureRandom()
    }

    /**
     * Authenticates a user given a basic auth header
     *
     * @param basicAuthHeader
     * @param ipAddress
     * @param userAgent
     * @param ignoreCache
     * @return AuthToken
     */
    AuthToken authenticate(String basicAuthHeader, String ipAddress, String userAgent, boolean ignoreCache) {
        if (!basicAuthHeader) return null

        int i = basicAuthHeader.indexOf(' ')
        if (i < 0) return null

        String decodedHeader = new String(basicAuthHeader.substring(i + 1).decodeBase64())
        i = decodedHeader.indexOf(':')
        if (i < 0) return null

        String key = decodedHeader + " " + userAgent
        if (!ignoreCache) {
            def auth = authCache.get(key)
            if (auth && !auth.expired) return auth
        }

        String username = decodedHeader.substring(0, i)
        String password = decodedHeader.substring(i + 1)

        User user = null
        AuthToken.IdentifiedBy identifiedBy = null
        boolean rememberMe = false
        String newTicket = null

        if (username && password) { // username:password
            user = User.findByUsername(username)
            if (!user) {
                log.debug("User not found for username [${username}] from ${ipAddress}")
                return null
            }

            if (!user.checkPassword(password)) {
                log.debug("Invalid password for [${username}] from ${ipAddress}")
                return null
            }

            identifiedBy = AuthToken.IdentifiedBy.PASSWORD
            log.debug("Successful Login")

        }

        def auth = new AuthToken(basicAuthHeader, userAgent, ipAddress, user, identifiedBy, rememberMe, newTicket)
        authCache.put(key, auth)
        return auth
    }
}
