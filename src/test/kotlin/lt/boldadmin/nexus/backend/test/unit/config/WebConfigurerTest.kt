package lt.boldadmin.nexus.backend.test.unit.config

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonEncoder

class WebConfigurerTest {

    @Test
    fun `Replaces default JSON encoder`() {
        val serverCodecConfigurerStub: ServerCodecConfigurer = mock()
        val serverDefaultCodecSpy: ServerCodecConfigurer.ServerDefaultCodecs = mock()

        doReturn(serverDefaultCodecSpy).`when`(serverCodecConfigurerStub).defaultCodecs()

        WebConfigurer().configureHttpMessageCodecs(serverCodecConfigurerStub)

        verify(serverDefaultCodecSpy).jackson2JsonEncoder(any<Jackson2JsonEncoder>())
    }
}