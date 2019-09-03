package lt.boldadmin.nexus.backend.test.unit.kafka.consumer

import io.mockk.*
import io.mockk.impl.annotations.MockK
import lt.boldadmin.nexus.api.service.worklog.status.location.WorklogLocationService
import lt.boldadmin.nexus.api.type.valueobject.Coordinates
import lt.boldadmin.nexus.backend.kafka.consumer.Consumer
import lt.boldadmin.nexus.backend.kafka.consumer.LogWorkByLocationConsumer
import lt.boldadmin.nexus.backend.kafka.deserializer.CollaboratorCoordinatesDeserializer
import lt.boldadmin.nexus.backend.kafka.factory.PropertiesFactory
import org.junit.Before
import org.junit.Test
import java.util.*

class LogWorkByLocationConsumerTest {

    @MockK
    private lateinit var propertiesFactoryStub: PropertiesFactory

    @MockK
    private lateinit var consumerSpy: Consumer

    @MockK
    private lateinit var locationServiceSpy: WorklogLocationService

    private lateinit var consumer: LogWorkByLocationConsumer

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        consumer = LogWorkByLocationConsumer(propertiesFactoryStub, consumerSpy, locationServiceSpy)
    }

    @Test
    fun `Logs work on collaborator location update `() {
        val subscribedFunction = slot<(Pair<String, Coordinates>) -> Unit>()
        val coordinates = Coordinates(1.0, 1.0)
        every { propertiesFactoryStub.create(CollaboratorCoordinatesDeserializer::class.java) } returns Properties()
        every { consumerSpy.consume(any(), capture(subscribedFunction), any()) } returns Unit
        every { locationServiceSpy.logWork("collabId", coordinates) } returns Unit

        consumer.consume()
        subscribedFunction.captured(Pair("collabId", coordinates))

        verify { locationServiceSpy.logWork("collabId", coordinates) }
    }

    @Test
    fun `Subscribes to collaborator location update`() {
        val properties = Properties()
        every { propertiesFactoryStub.create(CollaboratorCoordinatesDeserializer::class.java) } returns properties
        every { consumerSpy.consume(any(), any<(Pair<String, Coordinates>) -> Unit>(), any()) } returns Unit
        every { locationServiceSpy.logWork("collabId", any()) } returns Unit

        consumer.consume()

        verify { consumerSpy.consume<Pair<String, Coordinates>>("collaborator-location-update", any(), properties) }
    }
}