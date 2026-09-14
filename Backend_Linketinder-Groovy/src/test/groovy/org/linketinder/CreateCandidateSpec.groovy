package org.linketinder

import org.linketinder.repository.CandidatoRepository
import org.linketinder.services.CandidatoService
import spock.lang.Specification


class CreateCandidateSpec extends Specification {

    def repositorioMock = Mock(CandidatoRepository)

    def "teste para criação de candidato" () {
        given:
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

}