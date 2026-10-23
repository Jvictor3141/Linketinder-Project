package org.linketinder.database

import org.postgresql.util.PSQLException
import org.postgresql.util.ServerErrorMessage

import java.sql.SQLException

class PostgresErrorTranslator implements IDatabaseErrorTranslator {

    @Override
    RuntimeException translate(SQLException exception, DatabaseOperation operation) {
        SQLException postgresException = exception
        while (postgresException != null && !(postgresException instanceof PSQLException)) {
            postgresException = postgresException.nextException
        }

        ServerErrorMessage serverError = postgresException instanceof PSQLException
                ? (postgresException as PSQLException).serverErrorMessage
                : null

        String sqlState = exception.SQLState ?: postgresException?.SQLState
        String constraint = serverError?.constraint
        String column = serverError?.column

        return translateDetails(sqlState, constraint, column, operation, exception)
    }

    static RuntimeException translateDetails(String sqlState,
                                            String constraint,
                                            String column,
                                            DatabaseOperation operation,
                                            Throwable cause) {
        switch (sqlState) {
            case '23505': // unique_violation
                return new IllegalArgumentException(mensagemDuplicidade(constraint), cause)
            case '23503': // foreign_key_violation
                String mensagem = operation == DatabaseOperation.DELETE
                        ? 'Não é possível excluir este registro porque ele está associado a outros dados.'
                        : mensagemReferenciaAusente(constraint)
                return new IllegalArgumentException(mensagem, cause)
            case '23502': // not_null_violation
                return new IllegalArgumentException("O campo ${nomeCampo(column)} é obrigatório.", cause)
            case '23514': // check_violation
                return new IllegalArgumentException('Um dos valores informados não atende a uma regra do banco.', cause)
            case '22001': // string_data_right_truncation
                return new IllegalArgumentException('Um dos textos excede o tamanho permitido.', cause)
            case '22P02': // invalid_text_representation
            case '22007': // invalid_datetime_format
                return new IllegalArgumentException('Um dos valores informados tem formato inválido.', cause)
            case '40001': // serialization_failure
            case '40P01': // deadlock_detected
                return new IllegalStateException('A transação foi interrompida pelo banco. Tente novamente.', cause)
            default:
                String code = sqlState ? " (SQLSTATE ${sqlState})" : ''
                return new IllegalStateException("Falha ao acessar o banco de dados${code}.", cause)
        }
    }

    private static String mensagemDuplicidade(String constraint) {
        switch (constraint) {
            case 'candidato_cpf_idx':
                return 'CPF já cadastrado.'
            case 'empresa_cnpj_idx':
                return 'CNPJ já cadastrado.'
            case 'candidato_competencia_pkey':
                return 'A competência já está vinculada a este candidato.'
            case 'vaga_competencia_pkey':
                return 'A competência já está vinculada a esta vaga.'
            default:
                return 'Já existe um registro ou vínculo igual.'
        }
    }

    private static String mensagemReferenciaAusente(String constraint) {
        switch (constraint) {
            case 'vagas_id_empresa_fkey':
                return 'A empresa informada não foi encontrada.'
            default:
                return 'Uma referência informada não foi encontrada.'
        }
    }

    private static String nomeCampo(String column) {
        Map<String, String> nomes = [
                nome       : 'Nome',
                sobrenome  : 'Sobrenome',
                data_nascimento: 'Data de nascimento',
                e_mail     : 'E-mail',
                cpf        : 'CPF',
                cnpj       : 'CNPJ',
                estado     : 'Estado',
                cep        : 'CEP',
                descricao  : 'Descrição',
                competencia: 'Competência',
                endereco   : 'Endereço',
                id_empresa : 'Empresa'
        ]
        return column ? (nomes[column] ?: 'Este campo') : 'Um campo'
    }
}
