package lt.boldadmin.nexus.backend.test.unit.factory

import com.nhaarman.mockitokotlin2.*
import lt.boldadmin.nexus.api.service.worklog.CollaboratorUpdateSubscriber
import lt.boldadmin.nexus.backend.factory.CollaboratorUpdateSubscriberFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.context.support.GenericApplicationContext

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
            .getBean(any(), eq(CollaboratorUpdateSubscriber::class.java))

        val actualSubscribersMap = CollaboratorUpdateSubscriberFactory(contextStub).create().invoke()

        assertEquals(expectedSubscribersMap, actualSubscribersMap)
    }

}
