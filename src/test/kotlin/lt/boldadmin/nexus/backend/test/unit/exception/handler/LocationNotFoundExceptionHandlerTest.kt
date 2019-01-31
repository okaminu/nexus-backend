package lt.boldadmin.nexus.backend.test.unit.exception.handler

import com.nhaarman.mockito_kotlin.*
import lt.boldadmin.nexus.api.exception.LocationNotFoundException
import lt.boldadmin.nexus.backend.exception.handler.LocationNotFoundExceptionHandler
import lt.boldadmin.nexus.backend.exception.handler.TemplateExceptionHandler
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocationNotFoundExceptionHandlerTest {

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    private val handler = LocationNotFoundExceptionHandler

    @Test
    fun `Handles exception`() {
        val exchangeStub: ServerWebExchange = mock()
        doReturn(mock<ServerHttpResponse>()).`when`(exchangeStub).response

        val response = handler.handle(exchangeStub, LocationNotFoundException(""))

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
    fun `Sets http response status for LocationNotFoundException`() {
        val exchangeStub: ServerWebExchange = mock()
        val httpResponseSpy: ServerHttpResponse = mock()
        doReturn(httpResponseSpy).`when`(exchangeStub).response

        handler.handleException(exchangeStub, LocationNotFoundException("message"))

        verify(httpResponseSpy).statusCode = eq(HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @Test
    fun `Checks if exception type is LocationNotFoundException`() {
        assertTrue(handler.canHandle(LocationNotFoundException("message")))
    }

    class ExceptionSpy: Exception() {
        var wasPrintStackTraceCalled: Boolean = false
            private set

        override fun printStackTrace() {
            wasPrintStackTraceCalled = true
        }
    }
}