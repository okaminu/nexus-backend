package lt.boldadmin.nexus.backend.test.unit.handler.identityconfirmed

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import lt.boldadmin.crowbar.IdentityConfirmation
import lt.boldadmin.nexus.api.service.CollaboratorService
import lt.boldadmin.nexus.api.type.entity.Collaborator
import lt.boldadmin.nexus.api.type.valueobject.TimeRange
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class CollaboratorHandlerTest {

    @Mock
    private lateinit var collaboratorServiceStub: CollaboratorService

    @Mock
    private lateinit var identityConfirmationStub: IdentityConfirmation

    private lateinit var collaboratorHandler: CollaboratorHandler

    @Before
    fun setUp() {
        val collaboratorAuthService = CollaboratorAuthenticationService(
                collaboratorServiceStub,
                identityConfirmationStub
        )

        collaboratorHandler = CollaboratorHandler(collaboratorAuthService)

    }

    @Test
    fun `Takes collaborator work time`() {
        val workTime = TimeRange(0, 1)
        doReturn(USER_ID).`when`(identityConfirmationStub).getUserIdByToken(any())
        doReturn(Collaborator().apply { this.workTime = workTime }).`when`(collaboratorServiceStub).getById(USER_ID)

        val routerFunction = Routes(mock(), collaboratorHandler, mock(), mock()).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val workTimeResponseBody = webTestClient.get()
                .uri("/collaborator/workTime")
                .header("auth-token",
                    AUTH_TOKEN
                )
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(TimeRange::class.java)
                .returnResult()

        assertEquals(workTime, workTimeResponseBody.responseBody)

    }

    companion object {
        private const val USER_ID = "userId"
        private const val AUTH_TOKEN = "asda454s6d"
    }
}
