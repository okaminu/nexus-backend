package lt.boldadmin.nexus.backend.test.unit.handler.worklog.status.location

import com.nhaarman.mockitokotlin2.*
import lt.boldadmin.nexus.api.service.worklog.status.location.WorklogLocationService
import lt.boldadmin.nexus.api.type.entity.Collaborator
import lt.boldadmin.nexus.api.type.valueobject.Coordinates
import lt.boldadmin.nexus.backend.handler.worklog.status.location.WorklogLocationHandler
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
class WorklogLocationHandlerTest {

    @Mock
    private lateinit var worklogLocationServiceSpy: WorklogLocationService

    private lateinit var webClient: WebTestClient

    @Before
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(WorklogLocationHandler::class.java))
            .doReturn(WorklogLocationHandler(worklogLocationServiceSpy))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }

    @Test
    fun `Logs work by location`() {
        val collaborator = Collaborator().apply { id = "collaboratorId" }
        val coordinates = Coordinates(1234.toDouble(), 1234.toDouble())
        val coordinatesOfCollaborator = Pair(collaborator, coordinates)

        webClient.post()
            .uri("/worklog/status/log-work/location")
            .body(coordinatesOfCollaborator.toMono(), coordinatesOfCollaborator.javaClass)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        argumentCaptor<Collaborator>().apply {
            verify(worklogLocationServiceSpy).logWork(capture(), any())
            assertEquals(collaborator.id, firstValue.id)
        }

        argumentCaptor<Coordinates>().apply {
            verify(worklogLocationServiceSpy).logWork(any(), capture())
            assertEquals(coordinates, firstValue)
        }
    }
}