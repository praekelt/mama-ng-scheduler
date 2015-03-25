package mama.ng.scheduler

import grails.converters.JSON

class ScheduleController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def cronParserService


    /**
     * Pagination params:
     * max = max items to return; default 10; max 100
     * offset = offset of items; default 0
     *
     * @return
     */
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
        if (!instance.nextSend) {
            instance.nextSend = cronParserService.determineNextDate(instance.cronDefinition)
            instance.save()
        }

        if (instance.hasErrors()) {
            withFormat {
                json {
                    response.status = 400
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
                    response.status = 400
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
                render {success: true} as JSON
            }
        }
    }

    protected void notFound() {
        withFormat {
            json { response.sendError(404) }
        }
    }
}
