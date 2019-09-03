package lt.boldadmin.nexus.backend.test.unit.kafka.consumer

import io.mockk.*
import io.mockk.impl.annotations.MockK
import lt.boldadmin.nexus.backend.kafka.consumer.Consumer
import lt.boldadmin.nexus.backend.kafka.factory.KafkaConsumerFactory
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.Duration.ofSeconds
import java.util.*

class ConsumerTest {

    @MockK
    private lateinit var consumerFactoryStub: KafkaConsumerFactory

    @MockK
    private lateinit var kafkaConsumerSpy: KafkaConsumer<String, String>

    private lateinit var consumer: Consumer

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        consumer = Consumer(consumerFactoryStub)
    }

    @Test
    fun `Subscribes to topic`() {
        val properties = Properties()
        every { consumerFactoryStub.create<String>(properties) } returns kafkaConsumerSpy
        every { kafkaConsumerSpy.subscribe(any<Collection<String>>()) } returns Unit
        every { kafkaConsumerSpy.poll(any<Duration>()) } returns mockk<ConsumerRecords<String, String>>()

        consumer.consume<String>("topic", {}, properties)

        verify { kafkaConsumerSpy.subscribe(listOf("topic")) }
    }

    @Test
    fun `Polls for events each second`() {
        val properties = Properties()
        every { consumerFactoryStub.create<String>(properties) } returns kafkaConsumerSpy
        every { kafkaConsumerSpy.subscribe(any<Collection<String>>()) } returns Unit
        every { kafkaConsumerSpy.poll(any<Duration>()) } returns mockk<ConsumerRecords<String, String>>()

        consumer.consume<String>("topic", {}, properties)

        verify { kafkaConsumerSpy.poll(ofSeconds(1)) }
    }

    @Test
    fun `Executes subscription function with event data`() {
        val properties = Properties()
        every { consumerFactoryStub.create<String>(properties) } returns kafkaConsumerSpy
        every { kafkaConsumerSpy.subscribe(any<Collection<String>>()) } returns Unit
        val records = mockk<ConsumerRecords<String, String>>()
        val record = mockk<ConsumerRecord<String, String>>()
        every { records.iterator() } returns mutableListOf(record)
        every { kafkaConsumerSpy.poll(any<Duration>()) } returns records

        consumer.consume<String>("topic", {}, properties)

        verify { kafkaConsumerSpy.poll(ofSeconds(1)) }
    }

}