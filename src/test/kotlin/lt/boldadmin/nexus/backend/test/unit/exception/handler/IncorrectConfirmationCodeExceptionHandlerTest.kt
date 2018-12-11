package lt.boldadmin.nexus.backend.test.unit.exception.handler

import com.nhaarman.mockito_kotlin.*
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

class IncorrectConfirmationCodeExceptionHandlerTest {

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    private lateinit var handler: IncorrectConfirmationCodeExceptionHandler

    @Before
    fun `Set up`() {
        handler = IncorrectConfirmationCodeExceptionHandler()
    }

    @Test
    fun `Handles exception`() {
        val exchangeStub: ServerWebExchange = mock()
        doReturn(mock<ServerHttpResponse>()).`when`(exchangeStub).response

        val response = handler.handle(exchangeStub, IncorrectConfirmationCodeException())

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
        val exceptionSpy: Exception = mock()

        handler.handle(mock(), exceptionSpy)

        verify(exceptionSpy).printStackTrace()
    }

    @Test
    fun `Sets http response status for IncorrectConfirmationCodeException`() {
        val exchangeStub: ServerWebExchange = mock()
        val httpResponseSpy: ServerHttpResponse = mock()
        doReturn(httpResponseSpy).`when`(exchangeStub).response

        handler.handleException(exchangeStub, IncorrectConfirmationCodeException())

        verify(httpResponseSpy).statusCode = eq(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `Checks if exception type is IncorrectConfirmationCodeException`() {
        assertTrue(handler.canHandle(IncorrectConfirmationCodeException()))
    }

}