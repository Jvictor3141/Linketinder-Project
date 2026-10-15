package org.linketinder

import org.linketinder.dao.IEmpresaDAO
import org.linketinder.model.Empresa
import org.linketinder.services.EmpresaService
import spock.lang.Specification
import spock.lang.Unroll

class CreateEmpresaSpec extends Specification {

    IEmpresaDAO daoMock
    EmpresaService empresaService

    def setup() {
        daoMock = Mock(IEmpresaDAO)
        empresaService = new EmpresaService(daoMock)
    }

    private Empresa empresaValida(Map campos = [:]) {
        Map padrao = [
                nome             : "Rei da batatinha",
                emailCorporativo : "joao@gmail.com",
                cnpj             : "45.444.444/0004-45",
                pais             : "Brasil",
                cep              : "99999-999",
                descricao        : "Empresa líder na área de tecnologia."
        ]
        return new Empresa(padrao + campos)
    }

    def "cria empresa válida e delega a gravação ao DAO"() {
        when:
        def empresa = empresaService.criaEmpresa(empresaValida())

        then:
        1 * daoMock.inserirEmpresa(_) >> { Empresa e -> e.id = 1; e }
        empresa.id == 1
        empresa.nome == "Rei da batatinha"
        empresa.cnpj == "45.444.444/0004-45"
        empresa.emailCorporativo == "joao@gmail.com"
        empresa.pais == "Brasil"
        empresa.cep == "99999-999"
        empresa.descricao == "Empresa líder na área de tecnologia."
    }

    @Unroll
    def "deve rejeitar empresa inválida: #caso"() {
        when:
        empresaService.criaEmpresa(empresaValida(campos))

        then:
        thrown(IllegalArgumentException)
        0 * daoMock._

        where:
        caso                       | campos
        "nome vazio"               | [nome: ""]
        "e-mail vazio"             | [emailCorporativo: ""]
        "cnpj vazio"               | [cnpj: ""]
        "país vazio"               | [pais: ""]
        "cep vazio"                | [cep: ""]
        "descrição vazia"          | [descricao: "   "]
    }

    def "rejeita dados nulos sem chamar o DAO"() {
        when:
        empresaService.criaEmpresa(null)

        then:
        thrown(IllegalArgumentException)
        0 * daoMock._
    }

    def "rejeita atualização sem ID antes de chamar o DAO"() {
        when:
        empresaService.atualizaEmpresa(empresaValida())

        then:
        thrown(IllegalArgumentException)
        0 * daoMock._
    }
}
