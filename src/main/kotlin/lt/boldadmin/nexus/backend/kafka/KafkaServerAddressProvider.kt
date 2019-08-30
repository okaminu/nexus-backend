package lt.boldadmin.nexus.backend.kafka


class KafkaServerAddressProvider {
    val url get() = System.getenv("KAFKA_SERVER_URL") ?: throw KafkaServerAddressNotSetException
}