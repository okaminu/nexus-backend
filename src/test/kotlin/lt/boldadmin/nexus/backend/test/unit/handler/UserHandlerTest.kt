package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.nexus.api.service.UserService
import lt.boldadmin.nexus.api.type.entity.User
import lt.boldadmin.nexus.backend.handler.*
import lt.boldadmin.nexus.backend.handler.worklog.WorklogAuthHandler
import lt.boldadmin.nexus.backend.handler.worklog.WorklogHandler
import lt.boldadmin.nexus.backend.handler.worklog.duration.WorklogDurationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogDescriptionHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogStartEndHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.location.WorklogLocationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.message.WorklogMessageHandler
import lt.boldadmin.nexus.backend.route.Routes
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.toMono
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class UserHandlerTest {

    @Mock
    private lateinit var contextStub: AbstractApplicationContext

    @Mock
    private lateinit var userServiceSpy: UserService

    @Before
    fun setUp() {
        val userHandler = UserHandler(userServiceSpy)

        doReturn(mock<StartedProjectWorkTokenHandler>()).`when`(contextStub).getBean(StartedProjectWorkTokenHandler::class.java)
        doReturn(mock<WorklogDurationHandler>()).`when`(contextStub).getBean(WorklogDurationHandler::class.java)
        doReturn(mock<WorklogLocationHandler>()).`when`(contextStub).getBean(WorklogLocationHandler::class.java)
        doReturn(mock<WorklogMessageHandler>()).`when`(contextStub).getBean(WorklogMessageHandler::class.java)
        doReturn(mock<WorklogDescriptionHandler>()).`when`(contextStub).getBean(WorklogDescriptionHandler::class.java)
        doReturn(mock<WorklogStartEndHandler>()).`when`(contextStub).getBean(WorklogStartEndHandler::class.java)
        doReturn(mock<WorklogAuthHandler>()).`when`(contextStub).getBean(WorklogAuthHandler::class.java)
        doReturn(mock<WorklogHandler>()).`when`(contextStub).getBean(WorklogHandler::class.java)
        doReturn(mock<CollaboratorHandler>()).`when`(contextStub).getBean(CollaboratorHandler::class.java)
        doReturn(mock<CompanyHandler>()).`when`(contextStub).getBean(CompanyHandler::class.java)
        doReturn(mock<CountryHandler>()).`when`(contextStub).getBean(CountryHandler::class.java)
        doReturn(mock<CustomerHandler>()).`when`(contextStub).getBean(CustomerHandler::class.java)
        doReturn(mock<ProjectHandler>()).`when`(contextStub).getBean(ProjectHandler::class.java)
        doReturn(userHandler).`when`(contextStub).getBean(UserHandler::class.java)
    }

    @Test
    fun `Creates user with defaults`() {
        val user = User().apply { id = "someFancyId" }
        doReturn(user).`when`(userServiceSpy).createWithDefaults()

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
                .uri("/user/create-with-defaults")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(User::class.java)
                .returnResult()

        assertEquals(user.id, response.responseBody!!.id)
    }

    @Test
    fun `Exists any user`() {
        doReturn(true).`when`(userServiceSpy).existsAny()

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/user/exists-any")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Gets user by id`() {
        val user = User().apply { id = "someFancyId" }
        doReturn(user).`when`(userServiceSpy).getById(user.id)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/user/${user.id}")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(User::class.java)
            .returnResult()

        assertEquals(user.id, response.responseBody!!.id)
    }

    @Test
    fun `Gets user by email`() {
        val user = User().apply { email = "someFancyEmail" }
        doReturn(user).`when`(userServiceSpy).getByEmail(user.email)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/user/email/${user.email}")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(User::class.java)
            .returnResult()

        assertEquals(user.email, response.responseBody!!.email)
    }

    @Test
    fun `Exists user by email`() {
        val user = User().apply { email = "someFancyEmail" }
        doReturn(true).`when`(userServiceSpy).existsByEmail(user.email)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/user/email/${user.email}/exists")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Gets user by project id`() {
        val projectId = "projectId"
        val user = User().apply { id = "someFancyId" }
        doReturn(user).`when`(userServiceSpy).getByProjectId(projectId)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/user/project/$projectId")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(User::class.java)
            .returnResult()

        assertEquals(user.id, response.responseBody!!.id)
    }

    @Test
    fun `User has customer`() {
        val customerId = "customerId"
        val userId = "userId"
        doReturn(true).`when`(userServiceSpy).doesUserHaveCustomer(userId, customerId)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/user/$userId/customer/$customerId/has-customer")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `User has project`() {
        val projectId = "projectId"
        val userId = "userId"
        doReturn(true).`when`(userServiceSpy).doesUserHaveProject(userId, projectId)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/user/$userId/project/$projectId/has-project")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `User has collaborator`() {
        val collaboratorId = "collaboratorId"
        val userId = "userId"
        doReturn(true).`when`(userServiceSpy).doesUserHaveCollaborator(userId, collaboratorId)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/user/$userId/collaborator/$collaboratorId/has-collaborator")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Is project name unique`() {
        val projectName = "projectName"
        val projectId = "projectId"
        val userId = "userId"
        doReturn(true).`when`(userServiceSpy).isProjectNameUnique(projectName, projectId, userId)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/user/$userId/project/$projectId/name/$projectName/is-unique")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Saves user`() {
        val user = User().apply { id = "someFancyId" }

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        webTestClient.post()
            .uri("/user/save")
            .body(user.toMono(), User::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        argumentCaptor<User>().apply {
            verify(userServiceSpy).save(capture())
            assertEquals(user.id, firstValue.id)
        }
    }
}
