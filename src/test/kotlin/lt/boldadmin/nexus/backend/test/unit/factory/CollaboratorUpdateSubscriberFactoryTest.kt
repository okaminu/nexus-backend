package lt.boldadmin.nexus.backend.test.unit.factory

import com.nhaarman.mockitokotlin2.*
import lt.boldadmin.nexus.api.service.collaborator.WorkWeekUpdateSubscriber
import lt.boldadmin.nexus.backend.factory.CollaboratorUpdateSubscriberFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.context.support.GenericApplicationContext

class CollaboratorUpdateSubscriberFactoryTest {

    @Test
    @Suppress("RemoveExplicitTypeArguments")
    fun `Provides Collaborator work week update subscriber`() {
        val contextStub: GenericApplicationContext = mock()
        val expectedSubscriber: WorkWeekUpdateSubscriber = mock()
        doReturn(expectedSubscriber).`when`(contextStub)
            .getBean(any<String>(), eq(WorkWeekUpdateSubscriber::class.java))

        val actualSubscriber = CollaboratorUpdateSubscriberFactory(contextStub).create().invoke()

        assertEquals(expectedSubscriber, actualSubscriber)
    }
}
