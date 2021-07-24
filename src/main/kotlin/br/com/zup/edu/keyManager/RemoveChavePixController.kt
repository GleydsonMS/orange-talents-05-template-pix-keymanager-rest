package br.com.zup.edu.keyManager

import br.com.zup.edu.KeyManagerRemoveServiceGrpc
import br.com.zup.edu.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import java.util.*

@Controller("/api/clientes/{clienteId}")
class RemoveChavePixController(
    private val removeChavePixClient: KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub
) {

    @Delete("/pix/{pixId}")
    fun deleta(clienteId: UUID, pixId: String): HttpResponse<Any> {

        removeChavePixClient.remover(RemoveChavePixRequest.newBuilder()
                                                            .setClienteId(clienteId.toString())
                                                            .setPixId(pixId)
                                                            .build())

        return HttpResponse.ok()
    }
}