package mama.ng.scheduler

import grails.converters.JSON
import grails.transaction.Transactional
import groovyx.net.http.ContentEncoding
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import groovyx.net.http.Method

@Transactional
class HttpRequestService {


    Boolean postText(String baseUrl, Map body, method = Method.POST) {
        try {
            def ret = null
            def http = new HTTPBuilder(baseUrl)
            http.contentEncoding = ContentEncoding.Type.DEFLATE

            // perform a POST request, expecting TEXT response
            http.request(method, ContentType.JSON) {
                uri.query = body
                headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'

                // response handler for a success response code
                response.success = { resp, reader ->
                    ret = true
                }
            }
            return ret

        } catch (HttpResponseException ex) {
            ex.printStackTrace()
            return null
        } catch (ConnectException ex) {
            ex.printStackTrace()
            return null
        }
    }
}
