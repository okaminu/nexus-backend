package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import lt.boldadmin.nexus.api.service.collaborator.CollaboratorCoordinatesService
import lt.boldadmin.nexus.api.service.collaborator.CollaboratorService
import lt.boldadmin.nexus.api.service.collaborator.WorkWeekValidatorService
import lt.boldadmin.nexus.api.type.entity.Collaborator
import lt.boldadmin.nexus.api.type.valueobject.*
import lt.boldadmin.nexus.backend.handler.CollaboratorHandler
import lt.boldadmin.nexus.backend.route.Routes
import org.assertj.core.api.Assertions.assertThat
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
import java.time.DayOfWeek.SUNDAY
import java.time.DayOfWeek.TUESDAY

@ExtendWith(MockitoExtension::class)
class CollaboratorHandlerTest {

    @Mock
    private lateinit var collaboratorServiceSpy: CollaboratorService

    @Mock
    private lateinit var workWeekValidatorServiceStub: WorkWeekValidatorService

    @Mock
    private lateinit var collaboratorCoordinatesServiceStub: CollaboratorCoordinatesService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(CollaboratorHandler::class.java))
            .doReturn(
                CollaboratorHandler(
                    collaboratorServiceSpy,
                    workWeekValidatorServiceStub,
                    collaboratorCoordinatesServiceStub
                )
            )

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
    fun `Finds coordinates`() {
        val coordinates = listOf(CollaboratorCoordinates("uniqueCollabId", Coordinates(1.2, 3.4), 123))
        doReturn(coordinates).`when`(collaboratorCoordinatesServiceStub).getByCollaboratorId("uniqueCollabId")

        val response = webClient.get()
            .uri("/collaborator/uniqueCollabId/coordinates")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Collection::class.java)
            .returnResult()

        assertThat(response.responseBody!!).hasSize(1).containsOnly(
            mapOf(
                "collaboratorId" to "uniqueCollabId",
                "timestamp" to 123,
                "coordinates" to mapOf(
                    "latitude" to 1.2,
                    "longitude" to 3.4
                )
            )
        )
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
    fun `Validates work week`() {
        val workWeek = sortedSetOf(DayMinuteInterval(dayOfWeek = SUNDAY))
        doReturn(setOf(WeekConstraintViolation("message", SUNDAY)))
            .`when`(workWeekValidatorServiceStub)
            .validate(workWeek)

        val responseBody = webClient.post()
            .uri("/collaborator/work-week/validate")
            .body(BodyInserters.fromObject(workWeek))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Set::class.java)
            .returnResult()

        assertEquals(setOf(mapOf("message" to "message", "dayOfWeek" to "SUNDAY")), responseBody.responseBody)
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
