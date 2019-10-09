package lt.boldadmin.nexus.backend.test.unit.handler.worklog

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import lt.boldadmin.nexus.api.service.worklog.status.WorklogOvertimeService
import lt.boldadmin.nexus.backend.handler.worklog.WorklogOvertimeHandler
import lt.boldadmin.nexus.backend.route.Routes
import lt.boldadmin.nexus.backend.test.unit.handler.create
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(MockitoExtension::class)
class WorklogOvertimeHandlerTest {

    @Mock
    private lateinit var worklogOvertimeServiceStub: WorklogOvertimeService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(WorklogOvertimeHandler::class.java))
            .doReturn(WorklogOvertimeHandler(worklogOvertimeServiceStub))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }

    @Test
    fun `Ends work for collaborators where work time is ended`() {
        webClient.post()
            .uri("/worklog/overtime/end/on-overtime")
            .exchange()
            .expectStatus()
            .isOk

        verify(worklogOvertimeServiceStub).endOnOvertime()
    }
}
