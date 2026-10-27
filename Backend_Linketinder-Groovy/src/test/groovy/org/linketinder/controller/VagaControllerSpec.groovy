package org.linketinder.controller

import org.linketinder.dao.IVagaDAO
import org.linketinder.model.Competencia
import org.linketinder.model.Vaga
import org.linketinder.services.VagaService
import org.linketinder.ui.ConsoleIO
import org.linketinder.ui.VagaView
import spock.lang.Specification

class VagaControllerSpec extends Specification {

    private IVagaDAO dao = Mock()
    private ByteArrayOutputStream saida = new ByteArrayOutputStream()

    def 'cadastra vaga com empresa e competências informadas no formulário'() {
        given:
        VagaController controller = controller('Desenvolvedor Groovy\nBackend\nFortaleza\n8\nGroovy\nSQL\n\n')

        when:
        controller.criar()

        then:
        1 * dao.inserirVaga({ Vaga vaga ->
            vaga.nome == 'Desenvolvedor Groovy' &&
                    vaga.descricao == 'Backend' && vaga.endereco == 'Fortaleza' &&
                    vaga.idEmpresa == 8 && vaga.competencias*.competencia == ['Groovy', 'SQL']
        }) >> { Vaga vaga -> vaga.id = 10; vaga }
        saida.toString('UTF-8').contains('Vaga Desenvolvedor Groovy criada com sucesso!')
    }

    def 'edição vazia mantém dados e empresa e sinaliza preservação das competências'() {
        given:
        Vaga atual = vagaValida()
        List<Competencia> competenciasAtuais = atual.competencias
        VagaController controller = controller('\n\n\n\n')

        when:
        controller.atualizar(10)

        then:
        1 * dao.buscaPorIdVaga(10) >> atual
        1 * dao.atualizaVaga({ Vaga alterada ->
            !alterada.is(atual) && alterada.id == 10 &&
                    alterada.nome == atual.nome && alterada.descricao == atual.descricao &&
                    alterada.endereco == atual.endereco && alterada.idEmpresa == 8 &&
                    alterada.competencias == null
        }) >> { Vaga alterada -> alterada }
        atual.competencias.is(competenciasAtuais)
        atual.competencias*.competencia == ['Groovy']
        saida.toString('UTF-8').contains('atualizada com sucesso!')
    }

    def 'repete o ID da empresa inválido sem perder os demais campos do cadastro'() {
        given:
        VagaController controller = controller('Desenvolvedor Groovy\nBackend\nFortaleza\nabc\n8\nGroovy\n\n')

        when:
        controller.criar()

        then:
        1 * dao.inserirVaga({ Vaga vaga ->
            vaga.nome == 'Desenvolvedor Groovy' && vaga.descricao == 'Backend' &&
                    vaga.endereco == 'Fortaleza' && vaga.idEmpresa == 8 &&
                    vaga.competencias*.competencia == ['Groovy']
        }) >> { Vaga vaga -> vaga.id = 10; vaga }
        saida.toString('UTF-8').contains('digite um número válido')
        saida.toString('UTF-8').contains('criada com sucesso!')
    }

    def 'falha ao salvar edição não altera a entidade lida nem anuncia sucesso'() {
        given:
        Vaga atual = vagaValida()
        VagaController controller = controller('Novo nome\nNova descrição\nNovo endereço\nSQL\n\n')

        when:
        controller.atualizar(10)

        then:
        1 * dao.buscaPorIdVaga(10) >> atual
        1 * dao.atualizaVaga({ Vaga alterada ->
            !alterada.is(atual) && alterada.id == 10 && alterada.idEmpresa == 8 &&
                    alterada.nome == 'Novo nome' && alterada.descricao == 'Nova descrição' &&
                    alterada.endereco == 'Novo endereço' && alterada.competencias*.competencia == ['SQL']
        }) >> { throw new IllegalStateException('Banco indisponível') }
        thrown(IllegalStateException)
        atual.nome == 'Desenvolvedor Groovy'
        atual.descricao == 'Backend'
        atual.endereco == 'Fortaleza'
        atual.competencias*.competencia == ['Groovy']
        !saida.toString('UTF-8').contains('sucesso')
    }

    def 'lista vagas com o ID da entidade empresa e competências'() {
        given:
        VagaController controller = controller('')

        when:
        controller.listar()

        then:
        1 * dao.listarVagas() >> [vagaValida()]
        String texto = saida.toString('UTF-8')
        texto.contains('ID: 10')
        texto.contains('ID Empresa: 8')
        texto.contains('Empresa: Acme')
        texto.contains('Competências: [Groovy]')
        texto.contains('Endereço: Fortaleza')
    }

    def 'exclui a vaga informada e apresenta o sucesso'() {
        given:
        VagaController controller = controller('')

        when:
        controller.excluir(10)

        then:
        1 * dao.buscaPorIdVaga(10) >> vagaValida()
        1 * dao.deletaVaga(10)
        saida.toString('UTF-8').contains('Vaga excluída com sucesso!')
    }

    def 'propaga empresa inválida para o tratamento comum de erros'() {
        given:
        VagaController controller = controller('Desenvolvedor\nBackend\nFortaleza\n0\nGroovy\n\n')

        when:
        controller.criar()

        then:
        thrown(IllegalArgumentException)
        0 * dao._
        !saida.toString('UTF-8').contains('sucesso')
    }

    private VagaController controller(String entrada) {
        ConsoleIO console = new ConsoleIO(new Scanner(entrada), new PrintStream(saida, true, 'UTF-8'))
        return new VagaController(new VagaService(dao), new VagaView(console))
    }

    private static Vaga vagaValida() {
        return new Vaga(
                id: 10,
                nome: 'Desenvolvedor Groovy',
                descricao: 'Backend',
                endereco: 'Fortaleza',
                idEmpresa: 8,
                nomeEmpresa: 'Acme',
                competencias: [new Competencia(id: 3, competencia: 'Groovy')]
        )
    }
}
