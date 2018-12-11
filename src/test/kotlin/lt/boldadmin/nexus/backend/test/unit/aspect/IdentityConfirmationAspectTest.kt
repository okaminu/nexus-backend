package lt.boldadmin.nexus.backend.test.unit.aspect

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.crowbar.IdentityConfirmation
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.web.reactive.function.server.ServerRequest

@RunWith(MockitoJUnitRunner::class)
class IdentityConfirmationAspectTest {

    @Mock
    private lateinit var headersMock: ServerRequest.Headers
    @Mock
    private lateinit var identityConfirmationMock: IdentityConfirmation

    @Mock
    private lateinit var serverRequestMock: ServerRequest

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    private lateinit var identityConfirmationAspect: IdentityConfirmationAspect

    @Before
    fun `Set up`() {
        identityConfirmationAspect = IdentityConfirmationAspect(
                identityConfirmationMock
        )
    }

    @Test
    fun `Checks if authentication token exists`() {
        mockHeaderResponse()
        doReturn(true).`when`(identityConfirmationMock).doesTokenExist(HEADER_LIST[0])

        identityConfirmationAspect.tokenExistsAdvice(serverRequestMock)

        verify(identityConfirmationMock).doesTokenExist(HEADER_LIST[0])
    }

    @Test
    fun `Throws exception when authentication token is not found`() {
        expectedException.expect(IncorrectTokenException::class.java)
        mockHeaderResponse()
        doReturn(false).`when`(identityConfirmationMock).doesTokenExist(HEADER_LIST[0])

        identityConfirmationAspect.tokenExistsAdvice(serverRequestMock)
    }

    @Test
    fun `Throws exception when authentication token is not provided`() {
        expectedException.expect(IncorrectTokenException::class.java)
        doReturn(headersMock).`when`(serverRequestMock).headers()
        doReturn(emptyList<String>()).`when`(headersMock).header("auth-token")

        identityConfirmationAspect.tokenExistsAdvice(serverRequestMock)
    }

    @Test
    fun `Checks if user by the given confirmation code exists`() {
        doReturn(true).`when`(identityConfirmationMock).doesUserByCodeExist("confirmationCode")
        doReturn("confirmationCode").`when`(serverRequestMock).pathVariable("code")

        identityConfirmationAspect.confirmationCodeUserExistsAdvice(serverRequestMock)

        verify(identityConfirmationMock).doesUserByCodeExist("confirmationCode")
        verify(serverRequestMock).pathVariable("code")
    }

    @Test
    fun `Throws exception when confirmation code is incorrect`() {
        expectedException.expect(IncorrectConfirmationCodeException::class.java)
        doReturn(false).`when`(identityConfirmationMock).doesUserByCodeExist("confirmationCode")
        doReturn("confirmationCode").`when`(serverRequestMock).pathVariable("code")

        identityConfirmationAspect.confirmationCodeUserExistsAdvice(serverRequestMock)
    }

    private fun mockHeaderResponse() {
        doReturn(headersMock).`when`(serverRequestMock).headers()
        doReturn(HEADER_LIST).`when`(headersMock).header("auth-token")
    }

    companion object {
        private val HEADER_LIST = listOf("saf654as6df48a")
    }
}