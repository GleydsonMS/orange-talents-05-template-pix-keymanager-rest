package br.com.zup.edu.keyManager.compartilhado

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest {

    val request = HttpRequest.GET<Any>("/")

    @Test
    fun `deve retornar 404 quando o status da excessao for not found`() {
        val mensagem = "nao encontrado"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND
            .withDescription(mensagem))

        val response = GlobalExceptionHandler().handle(request, notFoundException)

        assertEquals(HttpStatus.NOT_FOUND, response.status)
        assertNotNull(response.body())
        assertEquals(mensagem, (response.body() as JsonError).message)
    }

    @Test
    fun `deve retornar 422 quando o status da excessao for already exists`() {
        val mensagem = "chave existente no banco de dados"
        val alreadyExistsException = StatusRuntimeException(Status.ALREADY_EXISTS
            .withDescription(mensagem))

        val response = GlobalExceptionHandler().handle(request, alreadyExistsException)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.status)
        assertNotNull(response.body())
        assertEquals(mensagem, (response.body() as JsonError).message)
    }

    @Test
    fun `deve retornar 400 quando o status da excessao for invalid argument`() {
        val mensagem = "Dados inválidos"
        val invalidArgumentException = StatusRuntimeException(Status.INVALID_ARGUMENT
            .withDescription(mensagem))

        val response = GlobalExceptionHandler().handle(request, invalidArgumentException)

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
        assertNotNull(response.body())
        assertEquals(mensagem, (response.body() as JsonError).message)
    }

    @Test
    fun `deve retornar 500 quando qualquer outro erro for lancado`() {
        val internalException = StatusRuntimeException(Status.INTERNAL)

        val response = GlobalExceptionHandler().handle(request, internalException)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status)
        assertNotNull(response.body())
        assertTrue((response.body() as JsonError).message.contains("INTERNAL"))
    }
}