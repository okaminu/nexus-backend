package lt.boldadmin.nexus.backend.test.unit.httpserver.factory

import com.nhaarman.mockito_kotlin.*
import lt.boldadmin.nexus.api.service.worklog.CollaboratorUpdateSubscriber
import lt.boldadmin.nexus.backend.httpserver.factory.CollaboratorUpdateSubscriberFactory
import org.junit.Test
import org.springframework.context.support.GenericApplicationContext
import kotlin.test.assertEquals

class CollaboratorUpdateSubscriberFactoryTest {

    @Test
    fun `Provides Collaborator update subscribers map`() {
        val contextStub: GenericApplicationContext = mock()
        val startTimeUpdateDummy: CollaboratorUpdateSubscriber = mock()
        val endTimeUpdateDummy: CollaboratorUpdateSubscriber = mock()
        val expectedSubscribersMap = mapOf(
            "workTime.startOfDayInMinutes" to startTimeUpdateDummy,
            "workTime.endOfDayInMinutes" to endTimeUpdateDummy
        )
        doReturn(startTimeUpdateDummy, endTimeUpdateDummy).`when`(contextStub)
            .getBean(any<String>(), eq(CollaboratorUpdateSubscriber::class.java))

        val actualSubscribersMap = CollaboratorUpdateSubscriberFactory(contextStub).create().invoke()

        assertEquals(expectedSubscribersMap, actualSubscribersMap)
    }

}