package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import lt.boldadmin.nexus.api.service.UserService
import lt.boldadmin.nexus.api.type.entity.Collaborator
import lt.boldadmin.nexus.api.type.entity.User
import lt.boldadmin.nexus.backend.handler.UserHandler
import lt.boldadmin.nexus.backend.route.Routes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.toMono

@ExtendWith(MockitoExtension::class)
class UserHandlerTest {

    @Mock
    private lateinit var userServiceSpy: UserService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(UserHandler::class.java))
            .doReturn(UserHandler(userServiceSpy))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }

    @Test
    fun `Creates user with defaults`() {
        val user = User().apply { id = "someFancyId" }
        doReturn(user).`when`(userServiceSpy).createWithDefaults()

        val response = webClient.get()
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

        val response = webClient.get()
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

        val response = webClient.get()
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

        val response = webClient.get()
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

        val response = webClient.get()
            .uri("/user/email/${user.email}/exists")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Exists user by company name`() {
        val user = User().apply { companyName = "boldadmin" }
        doReturn(true).`when`(userServiceSpy).existsByCompanyName(user.companyName)

        val response = webClient.get()
            .uri("/user/company-name/${user.companyName}/exists")
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

        val response = webClient.get()
            .uri("/user/project/$projectId")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(User::class.java)
            .returnResult()

        assertEquals(user.id, response.responseBody!!.id)
    }

    @Test
    fun `Gets collaborators by user id`() {
        val userId = "userId"
        val collab = Collaborator().apply { id = "Collab" }
        val collaborators = setOf(collab)
        doReturn(collaborators).`when`(userServiceSpy).getCollaborators(userId)

        val response = webClient.get()
            .uri("/user/$userId/collaborators")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Set::class.java)
            .returnResult()

        assertEquals(1, response.responseBody!!.size)
        assertEquals(collab.id, (response.responseBody!!.first() as Map<*, *>)["id"])
    }

    @Test
    fun `User has project`() {
        val projectId = "projectId"
        val userId = "userId"
        doReturn(true).`when`(userServiceSpy).doesUserHaveProject(userId, projectId)

        val response = webClient.get()
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

        val response = webClient.get()
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

        val response = webClient.get()
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

        webClient.post()
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
