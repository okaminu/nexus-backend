package lt.boldadmin.nexus.backend.test.unit.handler.collaborator

import com.nhaarman.mockitokotlin2.doReturn
import lt.boldadmin.nexus.api.service.collaborator.WorkWeekValidatorService
import lt.boldadmin.nexus.api.type.valueobject.WeekConstraintViolation
import lt.boldadmin.nexus.api.type.valueobject.time.DayMinuteInterval
import lt.boldadmin.nexus.api.type.valueobject.time.MinuteInterval
import lt.boldadmin.nexus.backend.handler.collaborator.WorkWeekValidatorHandler
import lt.boldadmin.nexus.backend.route.Routes
import lt.boldadmin.nexus.backend.test.unit.handler.create
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.time.DayOfWeek.SUNDAY

@ExtendWith(MockitoExtension::class)
class WorkWeekValidatorHandlerTest {

    @Mock
    private lateinit var validatorServiceStub: WorkWeekValidatorService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(WorkWeekValidatorHandler::class.java))
            .doReturn(WorkWeekValidatorHandler(validatorServiceStub))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }

    @Test
    fun `Validates work week`() {
        val workWeek = sortedSetOf(DayMinuteInterval(SUNDAY, MinuteInterval(10, 20), false))
        doReturn(setOf(WeekConstraintViolation("message", SUNDAY)))
            .`when`(validatorServiceStub)
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

}
