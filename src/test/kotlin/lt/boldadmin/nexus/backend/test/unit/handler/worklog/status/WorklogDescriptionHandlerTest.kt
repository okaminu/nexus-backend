package lt.boldadmin.nexus.backend.test.unit.handler.worklog.status

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.nexus.api.service.worklog.status.WorklogDescriptionService
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogDescriptionHandler
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
class WorklogDescriptionHandlerTest {

    @Mock
    private lateinit var worklogDescriptionServiceSpy: WorklogDescriptionService

    private lateinit var webClient: WebTestClient

    @Before
    fun setUp() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(WorklogDescriptionHandler::class.java))
            .doReturn(WorklogDescriptionHandler(worklogDescriptionServiceSpy))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }


    @Test
    fun `Gets description`() {
        val intervalId = "intervalId"
        val description = "description"
        doReturn(description)
            .`when`(worklogDescriptionServiceSpy)
            .getDescription(intervalId)

        val response = webClient.get()
            .uri("/worklog/interval/$intervalId/status/description")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(String::class.java)
            .returnResult()

        assertEquals(description, response.responseBody)
    }

    @Test
    fun `Updates description`() {
        val description = "description"
        val intervalId = "intervalId"

        webClient.post()
            .uri("/worklog/interval/$intervalId/status/description/update")
            .body(description.toMono(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(worklogDescriptionServiceSpy).updateDescription(intervalId, description)
    }

    @Test
    fun `Updates description by collaborator id`() {
        val description = "description"
        val collaboratorId = "collaboratorId"

        webClient.post()
            .uri("/worklog/collaborator/$collaboratorId/status/description/update")
            .body(description.toMono(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(worklogDescriptionServiceSpy)
            .updateDescriptionByCollaboratorId(collaboratorId, description)
    }

}
