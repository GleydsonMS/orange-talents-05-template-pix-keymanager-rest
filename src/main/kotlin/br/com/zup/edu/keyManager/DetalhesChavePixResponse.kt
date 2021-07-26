package br.com.zup.edu.keyManager

import br.com.zup.edu.BuscaChavePixResponse
import br.com.zup.edu.TipoConta
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class DetalhesChavePixResponse(response: BuscaChavePixResponse) {

    val pixId = response.pixId
    val tipoChave = response.chavePix.tipoChave
    val chavePix = response.chavePix.chave

    val criadaEm = response.chavePix.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

    val tipoConta = when (response.chavePix.conta.tipoConta) {
        TipoConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
        TipoConta.CONTA_POUPANCA -> "CONTA_POUPANCA"
        else -> "NAO_CONHECIDA"
    }

    val conta = mapOf(
        Pair("tipoConta", tipoConta),
        Pair("instituicao", response.chavePix.conta.instituicao),
        Pair("nomeTitular", response.chavePix.conta.nomeTitular),
        Pair("cpfTitular", response.chavePix.conta.cpfTitular),
        Pair("agencia", response.chavePix.conta.agencia),
        Pair("numero", response.chavePix.conta.numeroConta)
    )
}
