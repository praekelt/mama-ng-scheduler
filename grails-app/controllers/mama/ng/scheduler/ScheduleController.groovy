package mama.ng.scheduler

import grails.converters.JSON

class ScheduleController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 50, 200)
        def list = Schedule.list(params)
        withFormat {
            json {
                response.status = 200
                render list as JSON
            }
        }
    }

    def show(Schedule instance) {
        withFormat {
            json {
                response.status = 200
                render instance as JSON
            }
        }
    }

    def messages(Schedule instance) {
        def list = Message.findAllBySchedule(instance)
        withFormat {
            json {
                response.status = 200
                render list as JSON
            }
        }
    }

    def create(Schedule instance) {
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

    def update(Schedule instance) {
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

    def delete(Schedule instance) {

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
