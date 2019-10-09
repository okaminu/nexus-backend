package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import lt.boldadmin.nexus.api.service.collaborator.CollaboratorCoordinatesService
import lt.boldadmin.nexus.api.service.collaborator.CollaboratorService
import lt.boldadmin.nexus.api.type.entity.collaborator.Collaborator
import lt.boldadmin.nexus.api.type.valueobject.CollaboratorCoordinates
import lt.boldadmin.nexus.api.type.valueobject.Coordinates
import lt.boldadmin.nexus.backend.handler.CollaboratorHandler
import lt.boldadmin.nexus.backend.route.Routes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@ExtendWith(MockitoExtension::class)
class CollaboratorHandlerTest {

    @Mock
    private lateinit var collaboratorServiceSpy: CollaboratorService

    @Mock
    private lateinit var collaboratorCoordinatesServiceSpy: CollaboratorCoordinatesService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(CollaboratorHandler::class.java))
            .doReturn(CollaboratorHandler(collaboratorServiceSpy, collaboratorCoordinatesServiceSpy))

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
    fun `Finds coordinates`() {
        val coordinates = listOf(
            CollaboratorCoordinates(
                "collabId",
                Coordinates(1.2, 3.4),
                123
            )
        )
        doReturn(coordinates).`when`(collaboratorCoordinatesServiceSpy).getByCollaboratorId("collabId")

        val response = webClient.get()
            .uri("/collaborator/collabId/coordinates")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Collection::class.java)
            .returnResult()

        assertEquals(1, response.responseBody!!.size)
        assertEquals("collabId", (response.responseBody!!.first() as Map<*, *>)["collaboratorId"])
        assertEquals(123, (response.responseBody!!.first() as Map<*, *>)["timestamp"])
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
        val orderNumber = "5"

        webClient.post()
            .uri("/collaborator/$collaboratorId/attribute/order-number/update")
            .body(orderNumber.toMono(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(collaboratorServiceSpy).updateOrderNumber(collaboratorId, orderNumber.toShort())
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
