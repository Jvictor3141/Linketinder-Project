package org.linketinder

import org.linketinder.dao.EmpresaDAO
import org.linketinder.model.Empresa
import org.linketinder.services.EmpresaService
import spock.lang.Specification
import spock.lang.Unroll

class CreateEmpresaSpec extends Specification {

    EmpresaDAO daoMock
    EmpresaService empresaService

    def setup() {
        daoMock = Mock(EmpresaDAO)
        empresaService = new EmpresaService(daoMock)
    }

    private Empresa empresaValida(Map campos = [:]) {
        Map padrao = [
                nome             : "Rei da batatinha",
                emailCorporativo : "joao@gmail.com",
                cnpj             : "45.444.444/0004-45",
                pais             : "Brasil",
                cep              : "99999-999",
                descricao        : "Empresa lider na batatinha."
        ]
        return new Empresa(padrao + campos)
    }

    def "cria empresa válida e delega a gravação ao DAO"() {
        when:
        def empresa = empresaService.createEmpresa(empresaValida())

        then:
        1 * daoMock.inserir(_) >> { Empresa e -> e.id = 1; e }
        empresa.id == 1
        empresa.nome == "Rei da batatinha"
        empresa.cnpj == "45.444.444/0004-45"
        empresa.emailCorporativo == "joao@gmail.com"
        empresa.pais == "Brasil"
        empresa.cep == "99999-999"
        empresa.descricao == "Empresa lider na batatinha."
    }

    @Unroll
    def "deve rejeitar candidato inválido: #caso"() {
        when:
        empresaService.createEmpresa(empresaValida(campos))

        then:
        thrown(IllegalArgumentException)
        0 * daoMock._

        where:
        caso                       | campos
        "nome vazio"               | [nome: ""]
        "email vazio"              | [emailCorporativo: ""]
        "cnpj vazio"               | [cnpj: ""]
        "pais"                     | [pais: ""]
    }
}