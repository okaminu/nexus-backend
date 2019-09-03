package lt.boldadmin.nexus.backend.kafka.consumer

import lt.boldadmin.nexus.backend.kafka.factory.KafkaConsumerFactory
import java.time.Duration.ofSeconds
import java.util.*

class Consumer(private val consumerFactory: KafkaConsumerFactory) {

    fun <T>consume(topic: String, function: (T) -> Unit, properties: Properties) {
        val consumer = consumerFactory.create<T>(properties)
        consumer.subscribe(listOf(topic))

//        while (true) {
            consumer.poll(ofSeconds(1))
//                .forEach { function(it.value()) }
//        }
    }
}