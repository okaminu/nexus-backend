package lt.boldadmin.nexus.backend.test.unit.handler

import lt.boldadmin.nexus.backend.route.Routes
import org.junit.Test
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertTrue

class IsHealthyHandlerTest {

    @Test
    fun `Checks health`() {
        val isHealthyResponse = WebTestClient.bindToRouterFunction(Routes(create()).router())
            .build()
            .get()
            .uri("/is-healthy")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(isHealthyResponse.responseBody!!)
    }
}
