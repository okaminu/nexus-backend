package lt.boldadmin.nexus.backend.kafka.consumer

import lt.boldadmin.nexus.api.service.worklog.status.location.WorklogLocationService
import lt.boldadmin.nexus.api.type.valueobject.Coordinates
import lt.boldadmin.nexus.backend.kafka.deserializer.CollaboratorCoordinatesDeserializer
import lt.boldadmin.nexus.backend.kafka.factory.PropertiesFactory

class LogWorkByLocationConsumer(
    private val propertiesFactory: PropertiesFactory,
    private val consumer: Consumer,
    private val service: WorklogLocationService
) {
    fun consume() = consumer.consume<Pair<String, Coordinates>>(
        "collaborator-location-update",
        { service.logWork(it.first, it.second) },
        propertiesFactory.create(CollaboratorCoordinatesDeserializer::class.java)
    )
}