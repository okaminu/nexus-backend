package lt.boldadmin.nexus.backend.test.unit.handler

import lt.boldadmin.nexus.backend.route.Routes
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class IsHealthyHandlerTest {

    @Test
    fun `Checks if is healthy`() {

        val routerFunction = Routes(create()).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val isHealthyResponse = webTestClient.get()
                .uri("/is-healthy")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(Boolean::class.java)
                .returnResult()

        assertTrue(isHealthyResponse.responseBody!!)
    }
}
