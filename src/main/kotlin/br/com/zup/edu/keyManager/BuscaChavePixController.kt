package br.com.zup.edu.keyManager

import br.com.zup.edu.BuscaChavePixRequest
import br.com.zup.edu.KeyManagerBuscaServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.util.*

@Controller("/api/clientes/{clienteId}")
class BuscaChavePixController(
    val buscaChavePxClient: KeyManagerBuscaServiceGrpc.KeyManagerBuscaServiceBlockingStub
) {

    @Get("/pix/{pixId}")
    fun buscar(clienteId: UUID, pixId: String): HttpResponse<Any> {

        val response = buscaChavePxClient.buscar(BuscaChavePixRequest.newBuilder()
                                                                    .setPixId(BuscaChavePixRequest.FiltroPorPixId.newBuilder()
                                                                        .setClienteId(clienteId.toString())
                                                                        .setPixId(pixId)
                                                                        .build())
                                                                    .build())

        return HttpResponse.ok(DetalhesChavePixResponse(response))
    }
}