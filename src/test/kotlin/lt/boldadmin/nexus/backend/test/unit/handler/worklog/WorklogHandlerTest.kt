package lt.boldadmin.nexus.backend.test.unit.handler.worklog

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.nexus.api.service.worklog.WorklogService
import lt.boldadmin.nexus.api.type.entity.Worklog
import lt.boldadmin.nexus.backend.handler.worklog.WorklogHandler
import lt.boldadmin.nexus.backend.route.Routes
import lt.boldadmin.nexus.backend.test.unit.handler.create
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.toMono
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class WorklogHandlerTest {

    @Mock
    private lateinit var worklogServiceSpy: WorklogService

    private lateinit var webClient: WebTestClient

    @Before
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

        assertEquals(1, response.responseBody!!.size)
        assertEquals(expectedIntervalIds[0], (response.responseBody!!.first()))
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

        assertEquals(1, response.responseBody!!.size)
        assertEquals(expectedIntervalIds[0], (response.responseBody!!.first()))
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
