package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.nexus.api.service.CollaboratorService
import lt.boldadmin.nexus.api.type.entity.Collaborator
import lt.boldadmin.nexus.backend.handler.CollaboratorHandler
import lt.boldadmin.nexus.backend.route.Routes
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class CollaboratorHandlerTest {

    @Mock
    private lateinit var collaboratorServiceSpy: CollaboratorService

    private lateinit var webClient: WebTestClient

    @Before
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(CollaboratorHandler::class.java))
            .doReturn(CollaboratorHandler(collaboratorServiceSpy))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }

    @Test
    fun `Creates collaborator with defaults`() {
        val collaborator = Collaborator().apply { id = "collaboratorId" }
        doReturn(collaborator).`when`(collaboratorServiceSpy).createWithDefaults()

        val response = webClient.get()
                .uri("/collaborator/create-with-defaults")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(Collaborator::class.java)
                .returnResult()

        assertEquals(collaborator.id, response.responseBody!!.id)
    }

    @Test
    fun `Finds collaborator by id`() {
        val collaborator = Collaborator().apply { id = "collaboratorId" }
        doReturn(collaborator).`when`(collaboratorServiceSpy).getById(collaborator.id)

        val response = webClient.get()
            .uri("/collaborator/${collaborator.id}")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Collaborator::class.java)
            .returnResult()

        assertEquals(collaborator.id, response.responseBody!!.id)
    }

    @Test
    fun `Finds collaborator by mobile number`() {
        val collaborator = Collaborator().apply { mobileNumber = "mobileNumber" }
        doReturn(collaborator)
            .`when`(collaboratorServiceSpy)
            .getByMobileNumber(collaborator.mobileNumber)

        val response = webClient.get()
            .uri("/collaborator/mobile-number/${collaborator.mobileNumber}")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Collaborator::class.java)
            .returnResult()

        assertEquals(collaborator.mobileNumber, response.responseBody!!.mobileNumber)
    }

    @Test
    fun `Collaborator exists by id`() {
        val collaboratorId = "collaboratorId"
        doReturn(true).`when`(collaboratorServiceSpy).existsById(collaboratorId)

        val response = webClient.get()
            .uri("/collaborator/$collaboratorId/exists")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Collaborator exists by mobile number`() {
        val mobileNumber = "mobileNumber"
        doReturn(true).`when`(collaboratorServiceSpy).existsByMobileNumber(mobileNumber)

        val response = webClient.get()
            .uri("/collaborator/mobile-number/$mobileNumber/exists")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Updates attribute`() {
        val collaboratorId = "collaboratorId"
        val attributeName = "attributeName"
        val attributeValue = "attributeValue"

        webClient.post()
            .uri("/collaborator/$collaboratorId/attribute/$attributeName/update")
            .body(attributeValue.toMono(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(collaboratorServiceSpy).update(collaboratorId, attributeName, attributeValue)
    }

    @Test
    fun `Updates attribute with empty value when body is empty`() {
        val collaboratorId = "collaboratorId"
        val attributeName = "attributeName"

        webClient.post()
            .uri("/collaborator/$collaboratorId/attribute/$attributeName/update")
            .body(Mono.empty(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(collaboratorServiceSpy).update(collaboratorId, attributeName, "")
    }

    @Test
    fun `Updates order number`() {
        val collaboratorId = "collaboratorId"
        val orderNumber: Short = 5

        webClient.post()
            .uri("/collaborator/$collaboratorId/attribute/order-number/update")
            .body(orderNumber.toMono(), Short::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(collaboratorServiceSpy).updateOrderNumber(collaboratorId, orderNumber)
    }

    @Test
    fun `Saves collaborator`() {
        val collaborator = Collaborator().apply { id = "someFancyId" }

        webClient.post()
            .uri("/collaborator/save")
            .body(collaborator.toMono(), Collaborator::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        argumentCaptor<Collaborator>().apply {
            verify(collaboratorServiceSpy).save(capture())
            assertEquals(collaborator.id, firstValue.id)
        }
    }

}
