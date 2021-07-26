package br.com.zup.edu.keyManager.compartilhado.grpc

import br.com.zup.edu.KeyManagerBuscaServiceGrpc
import br.com.zup.edu.KeyManagerListaServiceGrpc
import br.com.zup.edu.KeyManagerRemoveServiceGrpc
import br.com.zup.edu.KeyManagerServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun resgistraChavePix() = KeyManagerServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun deletaChavePix() = KeyManagerRemoveServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun buscaChavePix() = KeyManagerBuscaServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChavesPix() = KeyManagerListaServiceGrpc.newBlockingStub(channel)
}