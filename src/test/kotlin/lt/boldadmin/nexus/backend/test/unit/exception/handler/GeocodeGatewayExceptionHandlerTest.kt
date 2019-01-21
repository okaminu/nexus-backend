package lt.boldadmin.nexus.backend.test.unit.exception.handler

import com.nhaarman.mockito_kotlin.*
import lt.boldadmin.nexus.api.exception.GeocodeGatewayException
import lt.boldadmin.nexus.backend.exception.handler.GeocodeGatewayExceptionHandler
import lt.boldadmin.nexus.backend.exception.handler.TemplateExceptionHandler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeocodeGatewayExceptionHandlerTest {

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    private lateinit var handler: GeocodeGatewayExceptionHandler

    @Before
    fun `Set up`() {
        handler = GeocodeGatewayExceptionHandler()
    }

    @Test
    fun `Handles exception`() {
        val exchangeStub: ServerWebExchange = mock()
        doReturn(mock<ServerHttpResponse>()).`when`(exchangeStub).response

        val response = handler.handle(exchangeStub, GeocodeGatewayException(""))

        assertEquals(Mono.empty(), response)
    }

    @Test
    fun `Returns error when exception could not be handled`() {
        expectedException.expect(Exception::class.java)
        doReturn(false).`when`(mock<TemplateExceptionHandler>()).canHandle(any())

        val response = handler.handle(mock(), mock<Exception>())
        response.block()
    }
    @Test
    fun `Logs an exception`() {
        val exceptionSpy = ExceptionSpy()

        handler.handle(mock(), exceptionSpy)

        assertTrue { exceptionSpy.wasPrintStackTraceCalled }
    }

    @Test
    fun `Sets http response status for GeocodeGatewayException`() {
        val exchangeStub: ServerWebExchange = mock()
        val httpResponseSpy: ServerHttpResponse = mock()
        doReturn(httpResponseSpy).`when`(exchangeStub).response

        handler.handleException(exchangeStub, GeocodeGatewayException("message"))

        verify(httpResponseSpy).statusCode = eq(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `Checks if exception type is GeocodeGatewayException`() {
        assertTrue(handler.canHandle(GeocodeGatewayException("message")))
    }

    class ExceptionSpy: Exception() {
        var wasPrintStackTraceCalled: Boolean = false
            private set

        override fun printStackTrace() {
            wasPrintStackTraceCalled = true
        }
    }
}