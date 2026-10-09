package org.linketinder

import org.linketinder.dao.CandidatoDAO
import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.linketinder.services.CandidatoService
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

class CreateCandidateSpec extends Specification {

    CandidatoDAO daoMock
    CandidatoService candidatoService

    def setup() {
        daoMock = Mock(CandidatoDAO)
        candidatoService = new CandidatoService(daoMock)
    }

    private Candidato candidatoValido(Map campos = [:]) {
        Map padrao = [
                nome          : "João",
                sobrenome     : "Silva",
                email         : "joao@gmail.com",
                cpf           : "622.691.163-80",
                dataNascimento: LocalDate.of(2000, 1, 1),
                estado        : "Maranhão",
                cep           : "65930-000",
                descricao     : "Dev apaixonado por tecnologia",
                competencias  : ["Java", "Spring", "SQL"].collect { new Competencia(competencia: it) }
        ]
        return new Candidato(padrao + campos)
    }

    def "cria candidato válido e delega a gravação ao DAO"() {
        when:
        def candidato = candidatoService.createCandidate(candidatoValido())

        then:
        1 * daoMock.inserir(_) >> { Candidato c -> c.id = 1; c }
        candidato.id == 1
        candidato.nome == "João"
        candidato.sobrenome == "Silva"
        candidato.email == "joao@gmail.com"
        candidato.cpf == "622.691.163-80"
        candidato.dataNascimento == LocalDate.of(2000, 1, 1)
        candidato.estado == "Maranhão"
        candidato.cep == "65930-000"
        candidato.descricao == "Dev apaixonado por tecnologia"
        candidato.competencias*.competencia == ["Java", "Spring", "SQL"]
    }

    @Unroll
    def "deve rejeitar candidato inválido: #caso"() {
        when:
        candidatoService.createCandidate(candidatoValido(campos))

        then:
        thrown(IllegalArgumentException)
        0 * daoMock._

        where:
        caso                       | campos
        "nome vazio"               | [nome: ""]
        "sobrenome vazio"          | [sobrenome: ""]
        "email vazio"              | [email: ""]
        "cpf vazio"                | [cpf: ""]
        "sem data de nascimento"   | [dataNascimento: null]
        "data de nascimento futura"| [dataNascimento: LocalDate.now().plusDays(1)]
        "sem competências"         | [competencias: []]
    }

    def "não permite candidato com menos de 18 anos"() {
        given: "faltando um dia para completar 18 anos"
        def candidato = candidatoValido(dataNascimento: LocalDate.now().minusYears(18).plusDays(1))

        when:
        candidatoService.createCandidate(candidato)

        then:
        thrown(IllegalArgumentException)
        0 * daoMock._
    }

    def "permite candidato que completa 18 anos hoje"() {
        given:
        def candidato = candidatoValido(dataNascimento: LocalDate.now().minusYears(18))

        when:
        candidatoService.createCandidate(candidato)

        then:
        1 * daoMock.inserir(_) >> { Candidato c -> c }
    }

    def "repassa o erro do DAO sem engolir (ex.: CPF duplicado)"() {
        when:
        candidatoService.createCandidate(candidatoValido())

        then:
        1 * daoMock.inserir(_) >> { throw new IllegalStateException("CPF já cadastrado.") }
        def erro = thrown(IllegalStateException)
        erro.message == "CPF já cadastrado."
    }
}