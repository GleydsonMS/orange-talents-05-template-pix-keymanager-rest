package br.com.zup.edu.keyManager.compartilhado.grpc

import br.com.zup.edu.KeyManagerServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun resgistraChave() = KeyManagerServiceGrpc.newBlockingStub(channel)
}