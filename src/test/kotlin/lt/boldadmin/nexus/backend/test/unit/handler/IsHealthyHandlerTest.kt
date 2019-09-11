package lt.boldadmin.nexus.backend.test.unit.handler

import lt.boldadmin.nexus.backend.route.Routes
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient

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
