package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.nexus.api.service.CustomerService
import lt.boldadmin.nexus.api.type.entity.Customer
import lt.boldadmin.nexus.backend.handler.CustomerHandler
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
class CustomerHandlerTest {

    @Mock
    private lateinit var customerServiceSpy: CustomerService

    private val contextStub = create()

    @Before
    fun setUp() {
        val customerHandler = CustomerHandler(customerServiceSpy)
        lenient().`when`(contextStub.getBean(CustomerHandler::class.java)).doReturn(customerHandler)
    }

    @Test
    fun `Creates customer with defaults`() {
        val userId = "userId"
        val customer = Customer().apply { id = "customerId" }
        doReturn(customer).`when`(customerServiceSpy).createWithDefaults(userId)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
                .uri("/customer/user/$userId/create-with-defaults")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(Customer::class.java)
                .returnResult()

        assertEquals(customer.id, response.responseBody!!.id)
    }

    @Test
    fun `Create with defaults`() {
        val customer = Customer().apply { id = "customerId" }
        doReturn(customer).`when`(customerServiceSpy).getById(customer.id)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/customer/${customer.id}")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Customer::class.java)
            .returnResult()

        assertEquals(customer.id, response.responseBody!!.id)
    }

    @Test
    fun `Updates attribute`() {
        val customerId = "customerId"
        val attributeName = "attributeName"
        val attributeValue = "attributeValue"
        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        webTestClient.post()
            .uri("/customer/$customerId/attribute/$attributeName/update")
            .body(attributeValue.toMono(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(customerServiceSpy).update(customerId, attributeName, attributeValue)
    }

    @Test
    fun `Updates order number`() {
        val customerId = "customerId"
        val orderNumber: Short = 5
        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        webTestClient.post()
            .uri("/customer/$customerId/attribute/order-number/update")
            .body(orderNumber.toMono(), Short::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(customerServiceSpy).updateOrderNumber(customerId, orderNumber)
    }

    @Test
    fun `Saves customer`() {
        val customer = Customer().apply { id = "someFancyId" }

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        webTestClient.post()
            .uri("/customer/save")
            .body(customer.toMono(), Customer::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        argumentCaptor<Customer>().apply {
            verify(customerServiceSpy).save(capture())
            assertEquals(customer.id, firstValue.id)
        }
    }

}