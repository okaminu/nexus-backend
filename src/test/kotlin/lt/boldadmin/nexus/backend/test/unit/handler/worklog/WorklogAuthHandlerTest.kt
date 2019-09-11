package lt.boldadmin.nexus.backend.test.unit.handler.worklog

import com.nhaarman.mockitokotlin2.doReturn
import lt.boldadmin.nexus.api.service.worklog.WorklogAuthService
import lt.boldadmin.nexus.backend.handler.worklog.WorklogAuthHandler
import lt.boldadmin.nexus.backend.route.Routes
import lt.boldadmin.nexus.backend.test.unit.handler.create
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(MockitoExtension::class)
class WorklogAuthHandlerTest {

    @Mock
    private lateinit var worklogAuthServiceStub: WorklogAuthService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(WorklogAuthHandler::class.java))
            .doReturn(WorklogAuthHandler(worklogAuthServiceStub))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }

    @Test
    fun `User has worklog interval`() {
        val intervalId = "intervalId"
        val userId = "userId"
        doReturn(true)
            .`when`(worklogAuthServiceStub)
            .doesUserHaveWorkLogInterval(userId, intervalId)

        val response = webClient.get()
            .uri("/worklog/interval/$intervalId/user/$userId/has-interval")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Collaborator has worklog interval`() {
        val intervalId = "intervalId"
        val collaboratorId = "collaboratorId"
        doReturn(true)
            .`when`(worklogAuthServiceStub)
            .doesCollaboratorHaveWorkLogInterval(collaboratorId, intervalId)

        val response = webClient.get()
            .uri("/worklog/interval/$intervalId/collaborator/$collaboratorId/has-interval")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Collaborator has multiple worklogs`() {
        val intervalId1 = "intervalId1"
        val intervalId2 = "intervalId2"
        val collaboratorId = "collaboratorId"
        doReturn(true)
            .`when`(worklogAuthServiceStub)
            .doesCollaboratorHaveWorkLogIntervals(collaboratorId, listOf(intervalId1, intervalId2))

        val response = webClient.get()
            .uri("/worklog/intervals/$intervalId1,$intervalId2/collaborator/$collaboratorId/has-intervals")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }
}
