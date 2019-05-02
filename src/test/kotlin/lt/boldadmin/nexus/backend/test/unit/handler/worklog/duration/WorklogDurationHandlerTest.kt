package lt.boldadmin.nexus.backend.test.unit.handler.worklog.duration

import com.nhaarman.mockito_kotlin.doReturn
import lt.boldadmin.nexus.api.service.worklog.duration.WorklogDurationService
import lt.boldadmin.nexus.backend.handler.worklog.duration.WorklogDurationHandler
import lt.boldadmin.nexus.backend.route.Routes
import lt.boldadmin.nexus.backend.test.unit.handler.create
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class WorklogDurationHandlerTest {

    @Mock
    private lateinit var worklogDurationServiceStub: WorklogDurationService

    private lateinit var webClient: WebTestClient

    @Before
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
//
//    @Test
//    fun `Sums work duration`() {
//        val intervalId1 = "intervalId1"
//        val intervalId2 = "intervalId2"
//        val durationSum = 354L
//        doReturn(durationSum).`when`(worklogDurationServiceStub).sumWorkDurations(listOf(intervalId1, intervalId2))
//
//        val response = webClient.get()
//            .uri("/worklog/intervals/$intervalId1,$intervalId2/durations-sum")
//            .exchange()
//            .expectStatus()
//            .isOk
//            .expectBody(Long::class.java)
//            .returnResult()
//
//        assertEquals(durationSum, response.responseBody)
//    }

    @Test
    fun `Sums work durations by collaborator`() {
        val collaboratorId = "id"
        val durationSum = 354L
        doReturn(durationSum).`when`(worklogDurationServiceStub).sumWorkDurationsByCollaboratorId(collaboratorId)

        val response = webClient.get()
            .uri("/worklog/collaborator/id/durations-sum")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Long::class.java)
            .returnResult()

        assertEquals(durationSum, response.responseBody)
    }

    @Test
    fun `Sums work durations by project`() {
        val projectId = "id"
        val durationSum = 354L
        doReturn(durationSum).`when`(worklogDurationServiceStub).sumWorkDurationsByProjectId(projectId)

        val response = webClient.get()
            .uri("/worklog/project/id/durations-sum")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Long::class.java)
            .returnResult()

        assertEquals(durationSum, response.responseBody)
    }
}