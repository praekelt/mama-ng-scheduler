package mama.ng.scheduler

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

class UserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]


    /**
     * Pagination params:
     * max = max items to return; default 10; max 100
     * offset = offset of items; default 0
     *
     * @return
     */
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
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
        if (instance.hasErrors()) {
            withFormat {
                json {
                    response.status = 400
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

    def update() {
        User instance = User.findById(params.id)
        JSONObject jsonObject = request.JSON

        if (instance == null) {
            notFound()
            return
        }

        String username = jsonObject["username"]?:null
        String password = jsonObject["password"]?:null
        String apiKey = jsonObject["apiKey"]?:null

        if (username) {
            instance.username = username
        }
        if (password) {
            instance.passwordHash = password.bytes.encodeAsBase64()
        }
        if (apiKey) {
            instance.apiKey = apiKey
        }
        instance.save()

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

    def delete(User instance) {

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
