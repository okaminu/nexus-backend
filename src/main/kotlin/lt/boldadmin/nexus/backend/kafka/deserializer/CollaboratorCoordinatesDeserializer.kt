package lt.boldadmin.nexus.backend.kafka.deserializer

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import lt.boldadmin.nexus.api.type.valueobject.Coordinates
import org.apache.kafka.common.serialization.Deserializer

class CollaboratorCoordinatesDeserializer: Deserializer<Pair<String, Coordinates>> {
    override fun deserialize(topic: String?, data: ByteArray?): Pair<String, Coordinates> =
        jacksonObjectMapper().readValue(data, object : TypeReference<Pair<String, Coordinates>>() {})

    override fun close() {}

    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {}
}