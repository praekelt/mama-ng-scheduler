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


    Boolean postText(String url, Map bodyParams, method = Method.POST) {
        try {
            def ret = null
            def http = new HTTPBuilder()
            http.contentEncoding = ContentEncoding.Type.DEFLATE
            http.request(url, method, ContentType.JSON) {
                headers.'User-Agent' = 'mama-ng-scheduler-bot/0.1 (+http://https://github.com/praekelt/mama-ng-scheduler)'
                body = bodyParams

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
