package mama.ng.scheduler

import grails.converters.JSON

class MessageController {

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
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
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

    def update(Message instance) {
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

    def delete(Message instance) {

        if (instance == null) {
            notFound()
            return
        }
        def schedule = instance.schedule

        instance.delete(flush:true, failOnError: true)

        if (schedule.messages.size() == 0 && schedule.sendCounter >= schedule.frequency) {
            schedule.delete(flush: true)
        }

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
