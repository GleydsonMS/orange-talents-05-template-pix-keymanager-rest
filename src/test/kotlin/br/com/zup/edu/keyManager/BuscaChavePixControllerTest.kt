package br.com.zup.edu.keyManager

import br.com.zup.edu.BuscaChavePixResponse
import br.com.zup.edu.KeyManagerBuscaServiceGrpc
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
internal class BuscaChavePixControllerTest {

    @field:Inject
    lateinit var buscaChavePixStub: KeyManagerBuscaServiceGrpc.KeyManagerBuscaServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve buscar uma chave pix existente`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(buscaChavePixStub.buscar(Mockito.any())).willReturn(buscaChavePixResponse(clienteId, pixId))

        val request = HttpRequest.GET<Any>("/api/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    private fun buscaChavePixResponse(clienteId: String, pixId: String): BuscaChavePixResponse {
        return BuscaChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .setChavePix(BuscaChavePixResponse.ChavePix.newBuilder()
                .setTipoChave(TipoChave.EMAIL)
                .setChave("rafael@ponte.com")
                .setConta(BuscaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipoConta(TipoConta.CONTA_CORRENTE)
                    .setInstituicao("Itau Unibanco SA")
                    .setNomeTitular("Rafael M C Ponte")
                    .setCpfTitular("02467781054")
                    .setAgencia("0001")
                    .setNumeroConta("291900")
                    .build()
                ).setCriadaEm(LocalDateTime.now().let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                })).build()
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class BuscaStubFactory {

        @Singleton
        fun stubBuscaMock() = Mockito.mock(KeyManagerBuscaServiceGrpc.KeyManagerBuscaServiceBlockingStub::class.java)
    }
}