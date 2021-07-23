package br.com.zup.edu.keyManager

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TipoChaveRequestTest {

    @Nested
    inner class CPFTest {

        @Test
        fun `deve ser valido quando o CPF for um valor valido`() {
            val tipoChave = TipoChaveRequest.CPF
            assertTrue(tipoChave.valida("02467781054"))
        }

        @Test
        fun `nao deve ser valido quando o CPF for um valor invalido`() {
            val tipoChave = TipoChaveRequest.CPF
            assertFalse(tipoChave.valida("12345678901"))
        }

        @Test
        fun `nao deve ser valido quando o CPF nao for informado`() {
            val tipoChave = TipoChaveRequest.CPF
            assertFalse(tipoChave.valida(null))
            assertFalse(tipoChave.valida(""))
        }
    }

    @Nested
    inner class CelularTest {

        @Test
        fun `deve ser valido quando o celular informado for um valor valido`() {
            val tipoChave = TipoChaveRequest.CELULAR
            assertTrue(tipoChave.valida("+5538991129112"))
        }

        @Test
        fun `nao deve ser valido quando o celular informado for um valor invalido`() {
            val tipoChave = TipoChaveRequest.CELULAR
            assertFalse(tipoChave.valida("991129112"))
            assertFalse(tipoChave.valida("+bb991129112"))
        }

        @Test
        fun `nao deve ser valido quando o celular nao for informado`() {
            val tipoChave = TipoChaveRequest.CELULAR
            assertFalse(tipoChave.valida(null))
            assertFalse(tipoChave.valida(""))
        }
    }

    @Nested
    inner class EmailTest {

        @Test
        fun `deve ser valido quando o Email infordado for valido`() {
            val tipoChave = TipoChaveRequest.EMAIL
            assertTrue(tipoChave.valida("email@correto.com"))
        }

        @Test
        fun `nao deve ser valido quando o Email infordado for invalido`() {
            val tipoChave = TipoChaveRequest.EMAIL
            assertFalse(tipoChave.valida("email.incorreto.com"))
            assertFalse(tipoChave.valida("email@incorreto.com."))
        }

        @Test
        fun `nao deve ser valido quando o Email nao for infordado`() {
            val tipoChave = TipoChaveRequest.EMAIL
            assertFalse(tipoChave.valida(null))
            assertFalse(tipoChave.valida(""))
        }
    }

    @Nested
    inner class AleatoriaTest {

        @Test
        fun `deve ser valido quando chave informada for nula ou vazia`() {
            val tipoChave = TipoChaveRequest.ALEATORIA
            assertTrue(tipoChave.valida(null))
            assertTrue(tipoChave.valida(""))
        }

        @Test
        fun `nao deve ser valido quando chave informada possuir valor atribuido`() {
            val tipoChave = TipoChaveRequest.ALEATORIA
            assertFalse(tipoChave.valida("valor-atribuido"))
        }
    }
}