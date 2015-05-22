package mama.ng.scheduler

import grails.test.mixin.TestFor
import groovy.mock.interceptor.MockFor
import groovyx.net.http.ContentEncoding
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import spock.lang.Specification

@TestFor(HttpRequestService)
class HttpRequestServiceSpec extends Specification {

    def httpMock
    def success
    def requestParameters
    def contentEncoding

    def setup() {
        requestParameters = []
        httpMock = new MockFor(HTTPBuilder.class)
        def requestDelegate = [
            response: [
                'statusLine': [
                    'protocol': 'HTTP/1.1',
                    'statusCode': 200,
                    'status':'OK',
                ],
            ],
        ]

        httpMock.demand.setContentEncoding(1) {
            ContentEncoding.Type encoding ->
            contentEncoding = encoding
        }

        httpMock.demand.request(1) {
            Object uri, Method method, ContentType contentType, Closure c ->
            c.delegate = requestDelegate
            c.headers = [:]
            c.call()
            requestParameters = [uri: uri, method: method, contentType: contentType, headers: c.headers, body: c.body ]
            if (success) {
                requestDelegate.response.success(requestDelegate.response, [:])
            }
        }
    }

    def cleanup() {
    }

    void "test successful postText"() {
        def ret

        given:
            success = true

        when:
            httpMock.use {
                ret = service.postText("http://example.com/", [foo: 'bar'])
            }

        then:
            requestParameters.uri == "http://example.com/"
            requestParameters.method == Method.POST
            requestParameters.contentType == ContentType.JSON
            requestParameters.headers == ['User-Agent': 'mama-ng-scheduler-bot/0.1 (+http://https://github.com/praekelt/mama-ng-scheduler)']
            requestParameters.body == [foo: 'bar']
            contentEncoding == ContentEncoding.Type.DEFLATE
            ret == true
    }
}
