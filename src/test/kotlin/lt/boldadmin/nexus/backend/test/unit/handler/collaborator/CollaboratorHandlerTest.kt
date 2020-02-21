package lt.boldadmin.nexus.backend.test.unit.handler.collaborator

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import lt.boldadmin.nexus.api.service.collaborator.CollaboratorService
import lt.boldadmin.nexus.api.type.entity.Collaborator
import lt.boldadmin.nexus.api.type.valueobject.time.DayMinuteInterval
import lt.boldadmin.nexus.api.type.valueobject.time.MinuteInterval
import lt.boldadmin.nexus.backend.handler.collaborator.CollaboratorHandler
import lt.boldadmin.nexus.backend.route.Routes
import lt.boldadmin.nexus.backend.test.unit.handler.create
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.time.DayOfWeek.TUESDAY

@ExtendWith(MockitoExtension::class)
class CollaboratorHandlerTest {

    @Mock
    private lateinit var collaboratorServiceSpy: CollaboratorService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(CollaboratorHandler::class.java))
            .doReturn(CollaboratorHandler(collaboratorServiceSpy))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }

    @Test
    fun `Creates collaborator with defaults`() {
        val collaborator = Collaborator().apply { id = "uniqueCollabId" }
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
        val collaborator = Collaborator().apply { id = "uniqueCollabId" }
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
        val collaboratorId = "uniqueCollabId"
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
        val collaboratorId = "uniqueCollabId"
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
        val collaboratorId = "uniqueCollabId"
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
    fun `Updates work week`() {
        val collaboratorId = "uniqueCollabId"
        val workWeek = sortedSetOf(DayMinuteInterval(TUESDAY, MinuteInterval(100, 200), false))

        webClient.post()
            .uri("/collaborator/$collaboratorId/work-week/update")
            .body(BodyInserters.fromObject(workWeek))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(collaboratorServiceSpy).update(collaboratorId, workWeek)
    }

    @Test
    fun `Updates order number`() {
        val collaboratorId = "uniqueCollabId"
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
