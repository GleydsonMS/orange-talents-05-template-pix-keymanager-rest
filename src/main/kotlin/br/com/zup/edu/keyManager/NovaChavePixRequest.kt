package br.com.zup.edu.keyManager

import br.com.zup.edu.ChavePixRequest
import br.com.zup.edu.TipoChave
import br.com.zup.edu.TipoConta
import br.com.zup.edu.keyManager.compartilhado.validacoes.ValidPixKey
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import java.util.*

@ValidPixKey
@Introspected
class NovaChavePixRequest(
    @field:NotNull val tipoChave: TipoChaveRequest?,
    @field:Size(max = 77) val chavePix: String?,
    @field:NotNull val tipoConta: TipoContaRequest?,
) {
    fun toModel(clienteId: UUID): ChavePixRequest {
        return ChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoChave(tipoChave?.tipoChave ?: TipoChave.TIPO_CHAVE_DESCONHECIDA)
            .setTipoConta(tipoConta?.tipoConta ?: TipoConta.TIPO_CONTA_DESCONHECIDA)
            .setChavePix(chavePix ?: "")
            .build()
    }
}

enum class TipoChaveRequest(val tipoChave: TipoChave) {
    CPF(TipoChave.CPF) {
        override fun valida(chavePix: String?): Boolean {
            if (chavePix.isNullOrBlank()) {
                return false
            }
            return CPFValidator().run {
                initialize(null)
                isValid(chavePix, null)
            }
        }
    },
    CELULAR(TipoChave.CELULAR) {
        override fun valida(chavePix: String?): Boolean {
            if (chavePix.isNullOrBlank()) {
                return false
            }
            return chavePix.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    EMAIL(TipoChave.EMAIL) {
        override fun valida(chavePix: String?): Boolean {
            if (chavePix.isNullOrBlank()) {
                return false
            }
            return EmailValidator().run {
                initialize(null)
                isValid(chavePix, null)
            }
        }
    },
    ALEATORIA(TipoChave.ALEATORIA) {
        override fun valida(chavePix: String?): Boolean {
            return chavePix.isNullOrBlank()
        }
    };
    abstract fun valida(chavePix: String?): Boolean
}

enum class TipoContaRequest(val tipoConta: TipoConta) {
    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA)
}