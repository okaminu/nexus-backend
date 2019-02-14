package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.nexus.api.service.CompanyService
import lt.boldadmin.nexus.api.type.entity.Company
import lt.boldadmin.nexus.backend.handler.CompanyHandler
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
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class CompanyHandlerTest {

    @Mock
    private lateinit var companyServiceSpy: CompanyService

    private lateinit var webClient: WebTestClient

    @Before
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(CompanyHandler::class.java))
            .doReturn(CompanyHandler(companyServiceSpy))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }


    @Test
    fun `Company exists by name`() {
        val companyName = "companyName"
        doReturn(true).`when`(companyServiceSpy).existsByName(companyName)

        val response = webClient.get()
            .uri("/company/name/$companyName/exists")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Saves customer`() {
        val company = Company().apply { id = "someFancyId" }

        webClient.post()
            .uri("/company/save")
            .body(company.toMono(), Company::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        argumentCaptor<Company>().apply {
            verify(companyServiceSpy).save(capture())
            assertEquals(company.id, firstValue.id)
        }
    }

}
