package org.linketinder.model

import spock.lang.Specification

import java.time.LocalDate

class ModeloSpec extends Specification {

    def "exibe a data de nascimento do candidato com o rótulo correto"() {
        given:
        Candidato candidato = new Candidato(dataNascimento: LocalDate.of(2000, 1, 1))

        expect:
        candidato.toString().contains('Data de nascimento: 2000-01-01')
        !candidato.toString().contains('Idade:')
    }

    def "exibe CNPJ e país da empresa com os rótulos corretos"() {
        given:
        Empresa empresa = new Empresa(cnpj: '12.345.678/0001-90', pais: 'Brasil')

        expect:
        empresa.toString().contains('CNPJ: 12.345.678/0001-90')
        empresa.toString().contains('País: Brasil')
        !empresa.toString().contains('CPF:')
        !empresa.toString().contains('Idade:')
    }

    def "a vaga guarda o ID da empresa associada"() {
        expect:
        new Vaga(idEmpresa: 12).idEmpresa == 12
    }
}
