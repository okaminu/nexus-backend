package lt.boldadmin.nexus.backend.test.unit.kafka.factory

import io.mockk.every
import io.mockk.mockk
import lt.boldadmin.nexus.backend.kafka.KafkaServerAddressProvider
import lt.boldadmin.nexus.backend.kafka.factory.PropertiesFactory
import org.apache.kafka.common.serialization.StringDeserializer
import org.junit.Test
import java.math.BigInteger
import java.util.*
import kotlin.test.assertEquals

class PropertiesFactoryTest {

    @Test
    fun `Creates properties`() {
        val addressProviderSpy = mockk<KafkaServerAddressProvider>()
        every { addressProviderSpy.url } returns "url"
        val expectedProperties = Properties().apply {
            this["bootstrap.servers"] = "url"
            this["key.deserializer"] = StringDeserializer::class.java
            this["value.deserializer"] = BigInteger::class.java
            this["group.id"] = "consumer"
        }

        val actualProperties = PropertiesFactory(addressProviderSpy).create(BigInteger::class.java)

        assertEquals(expectedProperties, actualProperties)
    }
}