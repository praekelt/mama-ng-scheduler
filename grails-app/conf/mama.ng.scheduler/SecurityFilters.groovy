package mama.ng.scheduler

class SecurityFilters {

    def securityService
    def grailsApplication

    def filters = {
        all(controller:'*', action:'*') {
            before = {

                String uri = request.forwardURI - request.contextPath

                if (uri.endsWith(".json") || uri.startsWith("/rest/")) {
                    def auth = params.Authorization ?: request.getHeader("Authorization")
                    def authToken = securityService.authenticate(auth, request.remoteAddr,
                        request.getHeader('User-Agent'), false)
                    if (!authToken) {
                        // set this header so browser will prompt for basic auth
                        response.setHeader('WWW-Authenticate', grailsApplication.config.scheduler.realm.toString())
                        response.sendError(401)
                        return false
                    }
                    params.auth = new Auth(securityService, authToken)
                    return true
                }

                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
