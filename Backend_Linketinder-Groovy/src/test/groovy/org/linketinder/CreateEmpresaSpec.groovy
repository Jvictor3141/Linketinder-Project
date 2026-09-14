package org.linketinder

import org.linketinder.repository.EmpresaRepository
import org.linketinder.services.EmpresaService
import spock.lang.Specification


class CreateEmpresaSpec extends Specification {

    def repositorioMock = Mock(EmpresaRepository)

    def "teste para criação de empresa" () {
        given:
        def empresaService = new EmpresaService(repositorioMock)

        when:
        def empresa = empresaService.createEmpresa("João", "joao@gmail.com", "622.691.163-80", "Brasil", "Maranhão", "65930-000", "Empresa voltada ao ramo de banco de dados", ["Java", "Spring", "SQL"])

        then:
        1 * repositorioMock.adicionar(_)
        empresa.nome == "João"
        empresa.emailCorporativo == "joao@gmail.com"
        empresa.cnpj == "622.691.163-80"
        empresa.pais == "Brasil"
        empresa.estado == "Maranhão"
        empresa.cep == "65930-000"
        empresa.descricao == "Empresa voltada ao ramo de banco de dados"
        empresa.competencias == ["Java", "Spring", "SQL"]
    }

}