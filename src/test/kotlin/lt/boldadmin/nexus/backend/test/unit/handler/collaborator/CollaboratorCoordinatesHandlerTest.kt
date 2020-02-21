package lt.boldadmin.nexus.backend.test.unit.handler.collaborator

import com.nhaarman.mockitokotlin2.doReturn
import lt.boldadmin.nexus.api.service.collaborator.CollaboratorCoordinatesService
import lt.boldadmin.nexus.api.type.valueobject.location.CollaboratorCoordinates
import lt.boldadmin.nexus.api.type.valueobject.location.Coordinates
import lt.boldadmin.nexus.backend.handler.collaborator.CollaboratorCoordinatesHandler
import lt.boldadmin.nexus.backend.route.Routes
import lt.boldadmin.nexus.backend.test.unit.handler.create
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(MockitoExtension::class)
class CollaboratorCoordinatesHandlerTest {

    @Mock
    private lateinit var collaboratorCoordinatesServiceStub: CollaboratorCoordinatesService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(CollaboratorCoordinatesHandler::class.java))
            .doReturn(CollaboratorCoordinatesHandler(collaboratorCoordinatesServiceStub))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
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

}
