package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockito_kotlin.doReturn
import lt.boldadmin.nexus.api.service.CountryService
import lt.boldadmin.nexus.api.type.valueobject.Country
import lt.boldadmin.nexus.backend.handler.CountryHandler
import lt.boldadmin.nexus.backend.route.Routes
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class CountryHandlerTest {

    @Mock
    private lateinit var countryServiceStub: CountryService

    private val contextStub = create()

    @Before
    fun setUp() {
        val countryHandler = CountryHandler(countryServiceStub)
        lenient()
            .`when`(contextStub.getBean(CountryHandler::class.java))
            .doReturn(countryHandler)
    }

    @Test
    fun `Gets all countries`() {
        val countries = listOf(Country("Lithuania"))
        doReturn(countries).`when`(countryServiceStub).countries

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
                .uri("/countries")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(Collection::class.java)
                .returnResult()

        assertEquals(1, response.responseBody!!.size)
    }

}
