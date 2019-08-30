package lt.boldadmin.nexus.backend.kafka.consumer

import lt.boldadmin.nexus.backend.kafka.deserializer.CollaboratorCoordinatesDeserializer
import lt.boldadmin.nexus.backend.kafka.factory.KafkaConsumerFactory
import lt.boldadmin.nexus.backend.kafka.factory.PropertiesFactory
import java.time.Duration

class Consumer(
    private val propertiesFactory: PropertiesFactory,
    private val consumerFactory: KafkaConsumerFactory
) {
    fun <T>consume(topic: String, function: (T) -> Unit) {
        val props = propertiesFactory.create(CollaboratorCoordinatesDeserializer::class.java)
        val consumer = consumerFactory.create<T>(props)
        consumer.subscribe(listOf(topic))

        while (true) {
            consumer.poll(Duration.ofSeconds(1)).forEach {
                function(it.value())
            }
        }
    }
}