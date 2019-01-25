package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.nexus.api.service.ProjectService
import lt.boldadmin.nexus.api.type.entity.Project
import lt.boldadmin.nexus.backend.handler.ProjectHandler
import lt.boldadmin.nexus.backend.route.Routes
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
class ProjectHandlerTest {

    @Mock
    private lateinit var projectServiceSpy: ProjectService

    private val contextStub = create()

    @Before
    fun setUp() {
        val projectHandler = ProjectHandler(projectServiceSpy)
        lenient().`when`(contextStub.getBean(ProjectHandler::class.java)).doReturn(projectHandler)
    }

    @Test
    fun `Creates project with defaults`() {
        val userId = "userId"
        val project = Project().apply { id = "projectId" }
        doReturn(project).`when`(projectServiceSpy).createWithDefaults(userId)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
                .uri("/project/user/$userId/create-with-defaults")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(Project::class.java)
                .returnResult()

        assertEquals(project.id, response.responseBody!!.id)
    }

    @Test
    fun `Finds project by id`() {
        val project = Project().apply { id = "projectId" }
        doReturn(project).`when`(projectServiceSpy).getById(project.id)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/project/${project.id}")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Project::class.java)
            .returnResult()

        assertEquals(project.id, response.responseBody!!.id)
    }

    @Test
    fun `Updates attribute`() {
        val projectId = "projectId"
        val attributeName = "attributeName"
        val attributeValue = "attributeValue"
        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        webTestClient.post()
            .uri("/project/$projectId/attribute/$attributeName/update")
            .body(attributeValue.toMono(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(projectServiceSpy).update(projectId, attributeName, attributeValue)
    }

    @Test
    fun `Updates order number`() {
        val projectId = "projectId"
        val orderNumber: Short = 5
        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        webTestClient.post()
            .uri("/project/$projectId/attribute/order-number/update")
            .body(orderNumber.toMono(), Short::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(projectServiceSpy).updateOrderNumber(projectId, orderNumber)
    }
}
