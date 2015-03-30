package mama.ng.scheduler

/**
 * Created by lyndsay on 2015/02/03.
 */
class Auth {

        private final SecurityService securityService
        private AuthToken authToken

        Auth(SecurityService securityService, AuthToken authToken) {
            this.securityService = securityService
            this.authToken = authToken
        }

        /**
         * Get the authenticated user or null if none is available (e.g. master authentication).
         */
        User getUser() { authToken?.user }

        /**
         * Has the master user authenticated?
         */
        boolean isMaster() { authToken && authToken.master }

}
