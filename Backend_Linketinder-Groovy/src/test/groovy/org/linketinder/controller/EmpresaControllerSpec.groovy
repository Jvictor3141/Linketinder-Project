package org.linketinder.controller

import org.linketinder.dao.IEmpresaDAO
import org.linketinder.model.Empresa
import org.linketinder.services.EmpresaService
import org.linketinder.view.ConsoleIO
import org.linketinder.view.EmpresaView
import spock.lang.Specification

class EmpresaControllerSpec extends Specification {

    private IEmpresaDAO dao = Mock()
    private ByteArrayOutputStream saida = new ByteArrayOutputStream()

    def 'cadastra empresa com os dados do formulário e apresenta o sucesso'() {
        given:
        EmpresaController controller = controller('Tecnologia\ncontato@empresa.com\n12.345.678/0001-90\nBrasil\n60000-000\nServiços de tecnologia\n')

        when:
        controller.criar()

        then:
        1 * dao.inserirEmpresa({ Empresa empresa ->
            empresa.nome == 'Tecnologia' &&
                    empresa.emailCorporativo == 'contato@empresa.com' &&
                    empresa.cnpj == '12.345.678/0001-90' &&
                    empresa.pais == 'Brasil' &&
                    empresa.cep == '60000-000' &&
                    empresa.descricao == 'Serviços de tecnologia'
        }) >> { Empresa empresa -> empresa.id = 7; empresa }
        saida.toString('UTF-8').contains('Empresa Tecnologia criada com sucesso!')
    }

    def 'edita uma cópia e mantém os campos deixados em branco'() {
        given:
        Empresa atual = empresaValida()
        EmpresaController controller = controller('Novo nome\n\n\n\n\n\n')

        when:
        controller.atualizar(7)

        then:
        1 * dao.buscaPorIdEmpresa(7) >> atual
        1 * dao.atualizaEmpresa({ Empresa alterada ->
            !alterada.is(atual) && alterada.id == 7 &&
                    alterada.nome == 'Novo nome' &&
                    alterada.emailCorporativo == atual.emailCorporativo &&
                    alterada.cnpj == atual.cnpj && alterada.pais == atual.pais &&
                    alterada.cep == atual.cep && alterada.descricao == atual.descricao
        }) >> { Empresa alterada -> alterada }
        atual.nome == 'Tecnologia'
        saida.toString('UTF-8').contains('Empresa Novo nome atualizada com sucesso!')
    }

    def 'lista os dados da empresa com ID CNPJ e país'() {
        given:
        EmpresaController controller = controller('')

        when:
        controller.listar()

        then:
        1 * dao.listarEmpresas() >> [empresaValida()]
        String texto = saida.toString('UTF-8')
        texto.contains('ID: 7')
        texto.contains('CNPJ: 12.345.678/0001-90')
        texto.contains('País: Brasil')
        texto.contains('Email Corporativo: contato@empresa.com')
    }

    def 'exclui a empresa informada antes de apresentar o sucesso'() {
        given:
        EmpresaController controller = controller('')

        when:
        controller.excluir(7)

        then:
        1 * dao.buscaPorIdEmpresa(7) >> empresaValida()
        1 * dao.deletaEmpresa(7)
        saida.toString('UTF-8').contains('Empresa excluída com sucesso!')
    }

    def 'propaga falhas de validação sem anunciar um cadastro concluído'() {
        given:
        EmpresaController controller = controller('\ncontato@empresa.com\n12.345.678/0001-90\nBrasil\n60000-000\nDescrição\n')

        when:
        controller.criar()

        then:
        thrown(IllegalArgumentException)
        0 * dao._
        !saida.toString('UTF-8').contains('sucesso')
    }

    private EmpresaController controller(String entrada) {
        ConsoleIO console = new ConsoleIO(new Scanner(entrada), new PrintStream(saida, true, 'UTF-8'))
        return new EmpresaController(new EmpresaService(dao), new EmpresaView(console))
    }

    private static Empresa empresaValida() {
        return new Empresa(
                id: 7,
                nome: 'Tecnologia',
                emailCorporativo: 'contato@empresa.com',
                cnpj: '12.345.678/0001-90',
                pais: 'Brasil',
                cep: '60000-000',
                descricao: 'Serviços de tecnologia'
        )
    }
}
