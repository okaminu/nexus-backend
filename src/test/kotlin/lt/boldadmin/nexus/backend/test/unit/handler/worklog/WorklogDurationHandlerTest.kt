package lt.boldadmin.nexus.backend.test.unit.handler.worklog

import com.nhaarman.mockitokotlin2.doReturn
import lt.boldadmin.nexus.api.service.worklog.WorklogDurationService
import lt.boldadmin.nexus.api.type.valueobject.DateRange
import lt.boldadmin.nexus.backend.handler.worklog.WorklogDurationHandler
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
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class WorklogDurationHandlerTest {

    @Mock
    private lateinit var worklogDurationServiceStub: WorklogDurationService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun setUp() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(WorklogDurationHandler::class.java))
            .doReturn(WorklogDurationHandler(worklogDurationServiceStub))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }

    @Test
    fun `Measures duration`() {
        val intervalId = "intervalId"
        val duration = 354L
        doReturn(duration).`when`(worklogDurationServiceStub).measureDuration(intervalId)

        val response = webClient.get()
            .uri("/worklog/interval/$intervalId/duration")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Long::class.java)
            .returnResult()

        assertEquals(duration, response.responseBody)
    }

    @Test
    fun `Sums work durations by collaborator`() {
        val durationsSum = 354L
        val collaboratorId= "id"
        doReturn(durationsSum).`when`(worklogDurationServiceStub).sumWorkDurationsByCollaboratorId(collaboratorId)

        val response = webClient.get()
            .uri("/worklog/collaborator/$collaboratorId/durations-sum")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Long::class.java)
            .returnResult()

        assertEquals(durationsSum, response.responseBody)
    }

    @Test
    fun `Sums work durations by project`() {
        val durationsSum = 354L
        val projectId = "id"
        doReturn(durationsSum).`when`(worklogDurationServiceStub).sumWorkDurationsByProjectId(projectId)

        val response = webClient.get()
            .uri("/worklog/project/$projectId/durations-sum")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Long::class.java)
            .returnResult()

        assertEquals(durationsSum, response.responseBody)
    }

    @Test
    fun `Sums work durations by project id and date range`() {
        val projectId = "projectId"
        val expectedDurationsSum = 123L
        val dateRange = DateRange(LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 15))
        doReturn(expectedDurationsSum)
            .`when`(worklogDurationServiceStub)
            .sumWorkDurationsByProjectId(projectId, dateRange)

        val response = webClient.get()
            .uri("/worklog/project/$projectId/start/2019-05-10/end/2019-05-15/durations-sum")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Long::class.java)
            .returnResult()

        assertEquals(expectedDurationsSum, response.responseBody!!)
    }

    @Test
    fun `Sums work durations by collaborator id and date range`() {
        val collaboratorId = "collaboratorId"
        val expectedDurationsSum = 123L
        val dateRange = DateRange(LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 15))
        doReturn(expectedDurationsSum)
            .`when`(worklogDurationServiceStub)
            .sumWorkDurationsByCollaboratorId(collaboratorId, dateRange)

        val response = webClient.get()
            .uri("/worklog/collaborator/$collaboratorId/start/2019-05-10/end/2019-05-15/durations-sum")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Long::class.java)
            .returnResult()

        assertEquals(expectedDurationsSum, response.responseBody!!)
    }

}
