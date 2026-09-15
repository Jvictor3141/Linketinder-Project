package org.linketinder

import org.linketinder.repository.CandidatoRepository
import org.linketinder.services.CandidatoService
import spock.lang.Specification


class CreateCandidateSpec extends Specification {

    def "teste para criação de candidato" () {
        given:
        def repositorioMock = Mock(CandidatoRepository)
        def candidatoService = new CandidatoService(repositorioMock)

        when:
        def candidato = candidatoService.createCandidate("João", "joao@gmail.com", "622.691.163-80", 26, "Maranhão", "65930-000", "Dev apaixonado por tecnologia", ["Java", "Spring", "SQL"])

        then:
        1 * repositorioMock.adicionar(_)
        candidato.nome == "João"
        candidato.email == "joao@gmail.com"
        candidato.cpf == "622.691.163-80"
        candidato.idade == 26
        candidato.estado == "Maranhão"
        candidato.cep == "65930-000"
        candidato.descricao == "Dev apaixonado por tecnologia"
        candidato.competencias == ["Java", "Spring", "SQL"]
    }

    def "deve rejeitar campos obrigatórios vazios" () {

        given:
        def repositorioMock = Mock(CandidatoRepository)
        def candidatoService = new CandidatoService(repositorioMock)

        when:
        candidatoService.createCandidate(nome, email, cpf, idade, "Maranhão", "65930-000", "Dev apaixonado por tecnologia", competencias)

        then:
        thrown(IllegalArgumentException)
        0 * repositorioMock.adicionar(_)

        where:
        nome   |       email       |        cpf       | idade |        competencias
        ""     | "joao@gmail.com"  | "622.691.162-80" |  26   | ["Java", "Spring", "SQL"]
        "joao" | ""                | "622.691.162-80" |  26   | ["Java", "Spring", "SQL"]
        "joao" | "joao@gmail.com"  | ""               |  26   | ["Java", "Spring", "SQL"]
        "joao" | "joao@gmail.com"  | "622.691.162-80" |  0    | ["Java", "Spring", "SQL"]
        "joao" | "joao@gmail.com"  | "622.691.162-80" |  26   | []
    }

    def "não permitir candidato com menos de 18 anos" () {
        given:
        def repositorioMock = Mock(CandidatoRepository)
        def candidatoService = new CandidatoService(repositorioMock)

        when:
        def candidato = candidatoService.createCandidate("João", "joao@gmail.com", "622.691.163-80", 17, "Maranhão", "65930-000", "Dev apaixonado por tecnologia", ["Java", "Spring", "SQL"])

        then:
        thrown(IllegalArgumentException)
        0 * repositorioMock.adicionar(_)
    }

    def "não permitir candidato com 18 anos ou mais" () {
        given:
        def repositorioMock = Mock(CandidatoRepository)
        def candidatoService = new CandidatoService(repositorioMock)

        when:
        def candidato = candidatoService.createCandidate("João", "joao@gmail.com", "622.691.163-80", 18, "Maranhão", "65930-000", "Dev apaixonado por tecnologia", ["Java", "Spring", "SQL"])

        then:
        1 * repositorioMock.adicionar(_)
    }
}