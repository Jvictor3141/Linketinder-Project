package org.linketinder

import org.linketinder.dao.IVagaDAO
import org.linketinder.model.Competencia
import org.linketinder.model.Vaga
import org.linketinder.services.VagaService
import spock.lang.Specification
import spock.lang.Unroll

class VagaServiceSpec extends Specification {

    IVagaDAO daoMock
    VagaService vagaService

    def setup() {
        daoMock = Mock(IVagaDAO)
        vagaService = new VagaService(daoMock)
    }

    private Vaga vagaValida(Map campos = [:]) {
        Map padrao = [
                nome        : 'Desenvolvedor Groovy',
                descricao   : 'Desenvolvimento de serviços backend.',
                endereco    : 'Fortaleza - CE',
                empresa     : 1,
                competencias: [new Competencia(competencia: 'Groovy')]
        ]
        return new Vaga(padrao + campos)
    }

    def "cria vaga válida e delega a gravação ao DAO"() {
        when:
        Vaga vaga = vagaService.criaVaga(vagaValida())

        then:
        1 * daoMock.inserirVaga(_) >> { Vaga salva -> salva.id = 10; salva }
        vaga.id == 10
        vaga.nome == 'Desenvolvedor Groovy'
    }

    @Unroll
    def "rejeita vaga inválida: #caso"() {
        when:
        vagaService.criaVaga(vagaValida(campos))

        then:
        thrown(IllegalArgumentException)
        0 * daoMock._

        where:
        caso                          | campos
        'nome em branco'              | [nome: '   ']
        'descrição vazia'             | [descricao: '']
        'endereço vazio'              | [endereco: '']
        'sem empresa associada'       | [empresa: null]
        'ID de empresa não positivo'  | [empresa: 0]
        'sem competências'            | [competencias: []]
        'competência em branco'       | [competencias: [new Competencia(competencia: '  ')]]
    }

    def "rejeita atualização sem ID antes de chamar o DAO"() {
        when:
        vagaService.atualizaVaga(vagaValida())

        then:
        thrown(IllegalArgumentException)
        0 * daoMock._
    }

    def "permite não alterar competências ao atualizar vaga"() {
        given:
        Vaga vaga = vagaValida(id: 10, competencias: null)

        when:
        vagaService.atualizaVaga(vaga)

        then:
        1 * daoMock.atualizaVaga(vaga) >> vaga
    }

    def "rejeita ID inválido na busca sem chamar o DAO"() {
        when:
        vagaService.buscaVagaPorId(0)

        then:
        thrown(IllegalArgumentException)
        0 * daoMock._
    }

    def "informa quando a vaga não existe"() {
        when:
        vagaService.buscaVagaPorId(55)

        then:
        1 * daoMock.buscaPorIdVaga(55) >> null
        thrown(IllegalArgumentException)
    }
}
