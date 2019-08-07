package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockito_kotlin.doReturn
import lt.boldadmin.nexus.api.service.CountryService
import lt.boldadmin.nexus.api.type.valueobject.Country
import lt.boldadmin.nexus.backend.handler.CountryHandler
import lt.boldadmin.nexus.backend.route.Routes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(MockitoExtension::class)
class CountryHandlerTest {

    @Mock
    private lateinit var countryServiceStub: CountryService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(CountryHandler::class.java))
            .doReturn(CountryHandler(countryServiceStub))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }

    @Test
    fun `Gets all countries`() {
        val country = Country("Lithuania")
        doReturn(listOf(country)).`when`(countryServiceStub).countries

        val response = webClient.get()
                .uri("/countries")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(Collection::class.java)
                .returnResult()

        assertEquals(1, response.responseBody!!.size)
        assertEquals(country.name, (response.responseBody!!.first() as Map<*, *>)["name"])
    }

}
