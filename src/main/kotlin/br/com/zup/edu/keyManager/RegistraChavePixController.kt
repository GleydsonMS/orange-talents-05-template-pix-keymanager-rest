package br.com.zup.edu.keyManager

import br.com.zup.edu.KeyManagerServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/clientes/{clienteId}")
class RegistraChavePixController(
    private val registraChavePixClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub
) {

    @Post("/pix")
    fun registra(clienteId: UUID, @Valid @Body request: NovaChavePixRequest): HttpResponse<Any> {
        val response = registraChavePixClient.registrar(request.toModel(clienteId))

        return HttpResponse.created(location(clienteId, response.pixId))
    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse
        .uri("/api/clientes/$clienteId/pix/${pixId}")
}