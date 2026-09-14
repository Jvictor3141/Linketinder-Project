package org.linketinder

import org.linketinder.repository.EmpresaRepository
import org.linketinder.services.CandidatoService
import org.linketinder.services.EmpresaService
import spock.lang.Specification


class CreateEmpresaSpec extends Specification {

    def repositorioMock = Mock(EmpresaRepository)

    def "teste para criação de empresa" () {
        given:
        def empresaService = new EmpresaService(repositorioMock)

        when:
        def empresa = empresaService.createEmpresa("EcosCorp", "EcosCorp@gmail.com", "11.111.111/1111-11", "Brasil", "Maranhão", "65930-000", "Empresa voltada ao ramo de banco de dados", ["Java", "Spring", "SQL"])

        then:
        1 * repositorioMock.adicionar(_)
        empresa.nome == "EcosCorp"
        empresa.emailCorporativo == "EcosCorp@gmail.com"
        empresa.cnpj == "11.111.111/1111-11"
        empresa.pais == "Brasil"
        empresa.estado == "Maranhão"
        empresa.cep == "65930-000"
        empresa.descricao == "Empresa voltada ao ramo de banco de dados"
        empresa.competencias == ["Java", "Spring", "SQL"]
    }

    def "deve rejeitar campos obrigatórios vazios" () {

        given:
        def empresaService = new EmpresaService(repositorioMock)

        when:
        empresaService.createEmpresa(nome, email, cnpj, "Brasil", estado, "65930-000", "Empresa voltada ao ramo de banco de dados", competencias)

        then:
        0 * repositorioMock.adicionar(_)
        thrown(IllegalArgumentException)

        where:
        nome       |         email         |          cnpj        |     estado    |        competencias
        ""         | "EcosCorp@gmail.com"  | "11.111.111/1111-11" |  "Maranhão"   | ["Java", "Spring", "SQL"]
        "EcosCorp" | ""                    | "11.111.111/1111-11" |  "Maranhão"   | ["Java", "Spring", "SQL"]
        "EcosCorp" | "EcosCorp@gmail.com"  | ""                   |  "Maranhão"   | ["Java", "Spring", "SQL"]
        "EcosCorp" | "EcosCorp@gmail.com"  | "11.111.111/1111-11" | ""            | ["Java", "Spring", "SQL"]
        "EcosCorp" | "EcosCorp@gmail.com"  | "11.111.111/1111-11" |  "Maranhão"   | []
    }

}