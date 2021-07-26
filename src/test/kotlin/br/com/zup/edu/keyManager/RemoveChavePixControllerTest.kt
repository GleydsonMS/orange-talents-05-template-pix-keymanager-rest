package br.com.zup.edu.keyManager

import br.com.zup.edu.KeyManagerRemoveServiceGrpc
import br.com.zup.edu.RemoveChavePixResponse
import br.com.zup.edu.keyManager.compartilhado.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChavePixControllerTest {

    @field:Inject
    lateinit var removeStub: KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve remover uma chave pix existente no banco de dados`() {
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val responseGrpc = RemoveChavePixResponse.newBuilder()
                                                .setClienteId(clienteId)
                                                .setPixId(pixId)
                                                .build()

        given(removeStub.remover(any())).willReturn(responseGrpc)

        val request = HttpRequest.DELETE<Any>("/api/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class RemoveStubFactory {
        @Singleton
        fun deletaChavePix() = Mockito.mock(KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub::class.java)
    }
}