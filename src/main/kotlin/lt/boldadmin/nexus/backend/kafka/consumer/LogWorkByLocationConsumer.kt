package lt.boldadmin.nexus.backend.kafka.consumer

import lt.boldadmin.nexus.api.service.worklog.status.location.WorklogLocationService
import lt.boldadmin.nexus.api.type.valueobject.Coordinates

class LogWorkByLocationConsumer(private val consumer: Consumer, private val service: WorklogLocationService) {
    fun consume() {
        consumer.consume<Pair<String, Coordinates>>("collaborator-location-update") {
            service.logWork(it.first, it.second)
        }
    }
}