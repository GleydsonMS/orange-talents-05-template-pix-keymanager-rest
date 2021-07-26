package br.com.zup.edu.keyManager

import br.com.zup.edu.KeyManagerListaServiceGrpc
import br.com.zup.edu.ListaChavePixResponse
import br.com.zup.edu.TipoChave
import br.com.zup.edu.TipoConta
import br.com.zup.edu.keyManager.compartilhado.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavesPixControllerTest {

    @field:Inject
    lateinit var listaChavePixStub: KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve listar todas as chaves pix existentes`() {
        val clienteId = UUID.randomUUID().toString()

        val responseGrpc = listaChavePixResponse(clienteId)

        given(listaChavePixStub.listar(Mockito.any())).willReturn(responseGrpc)

        val request = HttpRequest.GET<Any>("/api/clientes/$clienteId/pix/")
        val response = client.toBlocking().exchange(request, List::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(response.body().size, 2)
    }

    private fun listaChavePixResponse(clienteId: String): ListaChavePixResponse {
        val chavePix01 = ListaChavePixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipoChave(TipoChave.EMAIL)
            .setChavePix("rafael@ponte.com")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            }).build()

        val chavePix02 = ListaChavePixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipoChave(TipoChave.CPF)
            .setChavePix("02467781054")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            }).build()

        return ListaChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .addAllChavesPix(listOf(chavePix01, chavePix02))
            .build()
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class ListaStubFactory {

        @Singleton
        fun stubListaMock() = Mockito.mock(KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub::class.java)
    }
}