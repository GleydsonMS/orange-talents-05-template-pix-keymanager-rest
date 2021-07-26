package br.com.zup.edu.keyManager

import br.com.zup.edu.KeyManagerListaServiceGrpc
import br.com.zup.edu.ListaChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.util.*

@Controller("/api/clientes/{clienteId}")
class ListaChavesPixController(
    val listaChavesPixClient: KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub
) {

    @Get("/pix/")
    fun listar(clienteId: UUID): HttpResponse<Any> {

        val lista = listaChavesPixClient.listar(ListaChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .build())

        val chavesPix = lista.chavesPixList.map { ChavePixResponse(it) }
        return HttpResponse.ok(chavesPix)
    }
}