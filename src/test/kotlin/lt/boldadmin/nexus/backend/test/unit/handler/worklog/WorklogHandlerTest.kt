package lt.boldadmin.nexus.backend.test.unit.handler.worklog

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import lt.boldadmin.nexus.api.service.worklog.WorklogService
import lt.boldadmin.nexus.api.type.entity.Worklog
import lt.boldadmin.nexus.api.type.valueobject.DateRange
import lt.boldadmin.nexus.backend.handler.worklog.WorklogHandler
import lt.boldadmin.nexus.backend.route.Routes
import lt.boldadmin.nexus.backend.test.unit.handler.create
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.toMono
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class WorklogHandlerTest {

    @Mock
    private lateinit var worklogServiceSpy: WorklogService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(WorklogHandler::class.java))
            .doReturn(WorklogHandler(worklogServiceSpy))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }


    @Test
    fun `Finds interval ids by collaborator id`() {
        val collaboratorId = "collaboratorId"
        val expectedIntervalIds = listOf("intervalId1")
        doReturn(expectedIntervalIds).`when`(worklogServiceSpy).getIntervalIdsByCollaboratorId(collaboratorId)

        val response = webClient.get()
            .uri("/worklog/collaborator/$collaboratorId/interval-ids")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Collection::class.java)
            .returnResult()

        assertThat(response.responseBody!!).hasSize(1).contains(expectedIntervalIds[0])
    }

    @Test
    fun `Finds interval ids by project id`() {
        val projectId = "projectId"
        val expectedIntervalIds = listOf("intervalId1")
        doReturn(expectedIntervalIds).`when`(worklogServiceSpy).getIntervalIdsByProjectId(projectId)

        val response = webClient.get()
            .uri("/worklog/project/$projectId/interval-ids")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Collection::class.java)
            .returnResult()

        assertThat(response.responseBody!!).hasSize(1).contains(expectedIntervalIds[0])
    }

    @Test
    fun `Finds interval ids by project id and date range`() {
        val projectId = "projectId"
        val expectedIntervalIds = listOf("intervalId1")
        val dateRange = DateRange(LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 15))
        doReturn(expectedIntervalIds).`when`(worklogServiceSpy).getIntervalIdsByProjectId(projectId, dateRange)

        val response = webClient.get()
            .uri("/worklog/project/$projectId/start/2019-05-10/end/2019-05-15/interval-ids")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Collection::class.java)
            .returnResult()

        assertThat(response.responseBody!!).hasSize(1).contains(expectedIntervalIds[0])
    }

    @Test
    fun `Finds interval ids by collaborator id and date range`() {
        val collaboratorId = "collaboratorId"
        val expectedIntervalIds = listOf("intervalId1")
        val dateRange = DateRange(LocalDate.of(2019, 5, 10), LocalDate.of(2019, 5, 15))
        doReturn(expectedIntervalIds)
            .`when`(worklogServiceSpy)
            .getIntervalIdsByCollaboratorId(collaboratorId, dateRange)

        val response = webClient.get()
            .uri("/worklog/collaborator/$collaboratorId/start/2019-05-10/end/2019-05-15/interval-ids")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Collection::class.java)
            .returnResult()

        assertThat(response.responseBody!!).hasSize(1).contains(expectedIntervalIds[0])
    }

    @Test
    fun `Finds interval endpoints by interval id`() {
        val intervalId = "intervalId"
        val worklog = Worklog().apply { id = "worklogId" }
        doReturn(listOf(worklog)).`when`(worklogServiceSpy).getIntervalEndpoints(intervalId)

        val response = webClient.get()
            .uri("/worklog/interval/$intervalId/endpoints")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Collection::class.java)
            .returnResult()

        assertEquals(1, response.responseBody!!.size)
        assertEquals(worklog.id, (response.responseBody!!.first() as Map<*, *>)["id"])
    }

    @Test
    fun `Saves worklog`() {
        val worklog = Worklog().apply { id = "worklogId" }

        webClient.post()
            .uri("/worklog/save")
            .body(worklog.toMono(), Worklog::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        argumentCaptor<Worklog>().apply {
            verify(worklogServiceSpy).save(capture())
            assertEquals(worklog.id, firstValue.id)
        }
    }

}
