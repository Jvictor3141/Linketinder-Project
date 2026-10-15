package org.linketinder.database

import spock.lang.Specification
import spock.lang.Unroll
import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage

import java.sql.SQLException

class PostgresErrorTranslatorSpec extends Specification {

    @Unroll
    def "traduz violação de unicidade da restrição #constraint"() {
        when:
        def translated = PostgresErrorTranslator.translateDetails(
                '23505', constraint, null, DatabaseOperation.CREATE, new SQLException('duplicate')
        )

        then:
        translated instanceof IllegalArgumentException
        translated.message == message

        where:
        constraint                    | message
        'candidato_cpf_idx'           | 'CPF já cadastrado.'
        'empresa_cnpj_idx'            | 'CNPJ já cadastrado.'
        'candidato_competencia_pkey'  | 'A competência já está vinculada a este candidato.'
        'vaga_competencia_pkey'       | 'A competência já está vinculada a esta vaga.'
        'unknown_unique_constraint'   | 'Já existe um registro ou vínculo igual.'
    }

    def "traduz referência de empresa inexistente ao criar uma vaga"() {
        when:
        def translated = PostgresErrorTranslator.translateDetails(
                '23503', 'vagas_id_empresa_fkey', null, DatabaseOperation.CREATE, new SQLException('foreign key')
        )

        then:
        translated instanceof IllegalArgumentException
        translated.message == 'A empresa informada não foi encontrada.'
    }

    def "informa que não é possível excluir um registro ainda associado"() {
        when:
        def translated = PostgresErrorTranslator.translateDetails(
                '23503', 'vagas_id_empresa_fkey', null, DatabaseOperation.DELETE, new SQLException('foreign key')
        )

        then:
        translated instanceof IllegalArgumentException
        translated.message == 'Não é possível excluir este registro porque ele está associado a outros dados.'
    }

    @Unroll
    def "traduz o SQLSTATE #sqlState"() {
        when:
        def translated = PostgresErrorTranslator.translateDetails(
                sqlState, null, column, DatabaseOperation.CREATE, new SQLException('database error')
        )

        then:
        translated instanceof IllegalArgumentException
        translated.message == message

        where:
        sqlState | column | message
        '23502'  | 'cpf'  | 'O campo CPF é obrigatório.'
        '23514'  | null   | 'Um dos valores informados não atende a uma regra do banco.'
        '22001'  | null   | 'Um dos textos excede o tamanho permitido.'
        '22P02'  | null   | 'Um dos valores informados tem formato inválido.'
        '22007'  | null   | 'Um dos valores informados tem formato inválido.'
    }

    def "preserva a exceção original para erros de banco não reconhecidos"() {
        given:
        SQLException cause = new SQLException('connection failed', '08001')

        when:
        def translated = PostgresErrorTranslator.translate(cause, DatabaseOperation.CREATE)

        then:
        translated instanceof IllegalStateException
        translated.cause.is(cause)
        translated.message.contains('08001')
    }

    def "usa SQLSTATE e restrição dos metadados do PostgreSQL encadeado"() {
        given:
        SQLException erroJDBC = new SQLException('erro JDBC sem código')
        PSQLException erroPostgres = Mock()
        ServerErrorMessage detalhes = Mock()
        erroJDBC.nextException = erroPostgres

        erroPostgres.SQLState >> '23505'
        erroPostgres.serverErrorMessage >> detalhes
        detalhes.constraint >> 'candidato_cpf_idx'

        when:
        def traduzido = PostgresErrorTranslator.translate(erroJDBC, DatabaseOperation.CREATE)

        then:
        traduzido instanceof IllegalArgumentException
        traduzido.message == 'CPF já cadastrado.'
        traduzido.cause.is(erroJDBC)
    }
}
