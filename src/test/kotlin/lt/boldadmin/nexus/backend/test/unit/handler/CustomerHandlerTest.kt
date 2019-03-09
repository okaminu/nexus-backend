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
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class CustomerHandlerTest {

    @Mock
    private lateinit var customerServiceSpy: CustomerService

    private lateinit var webClient: WebTestClient

    @Before
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(CustomerHandler::class.java))
            .doReturn(CustomerHandler(customerServiceSpy))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }

    @Test
    fun `Creates customer with defaults`() {
        val userId = "userId"
        val customer = Customer().apply { id = "customerId" }
        doReturn(customer).`when`(customerServiceSpy).createWithDefaults(userId)

        val response = webClient.get()
                .uri("/customer/user/$userId/create-with-defaults")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(Customer::class.java)
                .returnResult()

        assertEquals(customer.id, response.responseBody!!.id)
    }

    @Test
    fun `Finds customer by id`() {
        val customer = Customer().apply { id = "customerId" }
        doReturn(customer).`when`(customerServiceSpy).getById(customer.id)

        val response = webClient.get()
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

        webClient.post()
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
    fun `Updates attribute with empty value when body is empty`() {
        val customerId = "customerId"
        val attributeName = "attributeName"

        webClient.post()
            .uri("/customer/$customerId/attribute/$attributeName/update")
            .body(Mono.empty(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(customerServiceSpy).update(customerId, attributeName, "")
    }

    @Test
    fun `Updates order number`() {
        val customerId = "customerId"
        val orderNumber = "5"

        webClient.post()
            .uri("/customer/$customerId/attribute/order-number/update")
            .body(orderNumber.toMono(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        verify(customerServiceSpy).updateOrderNumber(customerId, orderNumber.toShort())
    }

    @Test
    fun `Saves customer`() {
        val customer = Customer().apply { id = "someFancyId" }

        webClient.post()
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
