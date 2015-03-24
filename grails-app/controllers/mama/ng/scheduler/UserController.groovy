package mama.ng.scheduler

import grails.converters.JSON

class UserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 50, 200)
        def list = User.list(params)
        withFormat {
            json {
                response.status = 200
                render list as JSON
            }
        }
    }

    def show(User instance) {
        withFormat {
            json {
                response.status = 200
                render instance as JSON
            }
        }
    }

    def create(User instance) {
        if (instance == null) {
            notFound()
            return
        }

        if (instance.hasErrors()) {
            withFormat {
                json {
                    response.status = 403
                    render instance.errors as JSON
                }
            }
            return
        }

        instance.passwordHash = instance.passwordHash.encodeAsBase64()
        instance.save(flush:true, failOnError: true)

        withFormat {
            json {
                response.status = 200
                render instance as JSON
            }
        }
    }

    def update(User instance) {
        if (instance == null) {
            notFound()
            return
        }

        if (instance.hasErrors()) {
            withFormat {
                json {
                    response.status = 403
                    render instance.errors as JSON
                }
            }
            return
        }

        instance.save(flush:true, failOnError: true)

        withFormat {
            json {
                response.status = 200
                render instance as JSON
            }
        }
    }

    def delete(User instance) {

        if (instance == null) {
            notFound()
            return
        }

        instance.delete(flush:true, failOnError: true)

        withFormat {
            json {
                response.status = 200
                render ''
            }
        }
    }

    protected void notFound() {
        withFormat {
            json { response.sendError(404) }
        }
    }
}
