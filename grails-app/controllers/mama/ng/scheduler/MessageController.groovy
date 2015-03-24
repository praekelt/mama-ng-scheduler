package mama.ng.scheduler

import grails.converters.JSON

class MessageController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def cronParserService

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 50, 200)
        def list = Message.list(params)
        withFormat {
            json {
                response.status = 200
                render list as JSON
            }
        }
    }

    def show(Message instance) {
        withFormat {
            json {
                response.status = 200
                render instance as JSON
            }
        }
    }

    def create(Message instance) {
        if (!instance.nextSend) {
            instance.nextSend = cronParserService.determineNextDate(instance.schedule.cronDefinition)
            instance.save()
        }

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

    def update(Message instance) {
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

    def delete(Message instance) {

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
