package org.linketinder.controller

import org.linketinder.dao.ICandidatoDAO
import org.linketinder.dao.IEmpresaDAO
import org.linketinder.dao.IVagaDAO
import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.linketinder.model.Empresa
import org.linketinder.model.Vaga
import org.linketinder.services.CandidatoService
import org.linketinder.services.EmpresaService
import org.linketinder.services.VagaService
import org.linketinder.ui.CandidatoView
import org.linketinder.ui.ConsoleIO
import org.linketinder.ui.EmpresaView
import org.linketinder.ui.MenuView
import org.linketinder.ui.VagaView
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

class MenuControllerSpec extends Specification {
    ICandidatoDAO candidatos = Mock()
    IEmpresaDAO empresas = Mock()
    IVagaDAO vagas = Mock()

    def 'lista as três entidades e retorna ao menu até sair'() {
        when:
        String saida = executar('1\n2\n3\n9\n')

        then:
        1 * candidatos.listarCandidatos() >> [new Candidato(id: 1, nome: 'Ana')]
        1 * empresas.listarEmpresas() >> [new Empresa(id: 2, nome: 'Acme')]
        1 * vagas.listarVagas() >> [new Vaga(id: 3, nome: 'Dev')]
        saida.contains('Ana')
        saida.contains('Acme')
        saida.contains('Dev')
        saida.contains('Saindo...')
    }

    def 'cadastro e navegação compartilham a mesma entrada do terminal'() {
        given:
        String entrada = ['5', 'Ana', 'Silva', 'ana@example.com', '123.456.789-00',
                          '01-01-2000', 'CE', '60000-000', 'Desenvolvedora',
                          'Groovy', '', '9'].join('\n') + '\n'

        when:
        String saida = executar(entrada)

        then:
        1 * candidatos.inserirCandidato({ it.nome == 'Ana' && it.competencias*.competencia == ['Groovy'] }) >>
                { Candidato candidato -> candidato.id = 1; candidato }
        saida.contains('criado com sucesso')
        saida.contains('Saindo...')
        !saida.contains('Entrada encerrada.')
    }

    @Unroll
    def 'edita #entidade pelo submenu preservando campos vazios e volta ao menu'() {
        given:
        Candidato candidato = candidatoAtual(id)
        Empresa empresa = empresaAtual(id)
        Vaga vaga = vagaAtual(id)
        String entrada = "8\n${tipo}\n${id}\n" + ('\n' * quantidadeCampos) + '9\n'

        when:
        String saida = executar(entrada)

        then:
        (tipo == 1 ? 1 : 0) * candidatos.buscaCandidatoPorId(id) >> candidato
        (tipo == 1 ? 1 : 0) * candidatos.atualizaCandidato({ Candidato alterado ->
            !alterado.is(candidato) && alterado.id == id && alterado.nome == candidato.nome &&
                    alterado.sobrenome == candidato.sobrenome && alterado.email == candidato.email &&
                    alterado.cpf == candidato.cpf && alterado.dataNascimento == candidato.dataNascimento &&
                    alterado.estado == candidato.estado && alterado.cep == candidato.cep &&
                    alterado.descricao == candidato.descricao && alterado.competencias == null
        }) >> { Candidato alterado -> alterado }
        (tipo == 2 ? 1 : 0) * empresas.buscaPorIdEmpresa(id) >> empresa
        (tipo == 2 ? 1 : 0) * empresas.atualizaEmpresa({ Empresa alterada ->
            !alterada.is(empresa) && alterada.id == id && alterada.nome == empresa.nome &&
                    alterada.emailCorporativo == empresa.emailCorporativo && alterada.cnpj == empresa.cnpj &&
                    alterada.pais == empresa.pais && alterada.cep == empresa.cep &&
                    alterada.descricao == empresa.descricao
        }) >> { Empresa alterada -> alterada }
        (tipo == 3 ? 1 : 0) * vagas.buscaPorIdVaga(id) >> vaga
        (tipo == 3 ? 1 : 0) * vagas.atualizaVaga({ Vaga alterada ->
            !alterada.is(vaga) && alterada.id == id && alterada.nome == vaga.nome &&
                    alterada.descricao == vaga.descricao && alterada.endereco == vaga.endereco &&
                    alterada.idEmpresa == vaga.idEmpresa && alterada.competencias == null
        }) >> { Vaga alterada -> alterada }
        0 * candidatos._
        0 * empresas._
        0 * vagas._
        candidato.competencias*.id == [4]
        vaga.competencias*.id == [5]
        saida.contains(mensagem)
        saida.contains('Saindo...')
        !saida.contains('Entrada encerrada.')

        where:
        tipo | id | entidade    | quantidadeCampos | mensagem
        1    | 11 | 'candidato' | 9                | 'Candidato Ana atualizado com sucesso!'
        2    | 22 | 'empresa'   | 6                | 'Empresa Acme atualizada com sucesso!'
        3    | 33 | 'vaga'      | 4                | 'Vaga Dev atualizada com sucesso!'
    }

    @Unroll
    def 'exclui #entidade pelo submenu usando o ID informado e volta ao menu'() {
        when:
        String saida = executar("7\n${tipo}\n${id}\n9\n")

        then:
        (tipo == 1 ? 1 : 0) * candidatos.buscaCandidatoPorId(id) >> candidatoAtual(id)
        (tipo == 1 ? 1 : 0) * candidatos.deletaCandidato(id)
        (tipo == 2 ? 1 : 0) * empresas.buscaPorIdEmpresa(id) >> empresaAtual(id)
        (tipo == 2 ? 1 : 0) * empresas.deletaEmpresa(id)
        (tipo == 3 ? 1 : 0) * vagas.buscaPorIdVaga(id) >> vagaAtual(id)
        (tipo == 3 ? 1 : 0) * vagas.deletaVaga(id)
        0 * candidatos._
        0 * empresas._
        0 * vagas._
        saida.contains(mensagem)
        saida.contains('Saindo...')
        !saida.contains('Entrada encerrada.')

        where:
        tipo | id | entidade    | mensagem
        1    | 11 | 'candidato' | 'Candidato excluído com sucesso!'
        2    | 22 | 'empresa'   | 'Empresa excluída com sucesso!'
        3    | 33 | 'vaga'      | 'Vaga excluída com sucesso!'
    }

    def 'erros de entrada e opções desconhecidas permitem continuar no menu'() {
        when:
        String saida = executar('texto\n0\n7\n4\n8\n4\n8\n1\nabc\n9\n')

        then:
        0 * candidatos._
        0 * empresas._
        0 * vagas._
        saida.contains('Digite apenas números.')
        saida.contains('Escolha um número de 1 a 9.')
        saida.contains('Escolha um número de 1 a 3.')
        saida.contains('Saindo...')
    }

    @Unroll
    def 'apresenta erro #tipo e permite continuar'() {
        when:
        String saida = executar('1\n9\n')

        then:
        1 * candidatos.listarCandidatos() >> { throw falha }
        saida.contains(mensagem)
        saida.contains('Saindo...')

        where:
        tipo             | falha                                           | mensagem
        'de validação'   | new IllegalArgumentException('Dados inválidos')  | 'Não foi possível concluir a operação: Dados inválidos'
        'de persistência'| new IllegalStateException('Banco indisponível')  | 'Falha ao acessar os dados. Banco indisponível'
    }

    def 'data impossível é apresentada como erro de entrada sem persistir candidato'() {
        given:
        String entrada = ['5', 'Ana', 'Silva', 'ana@example.com', '123.456.789-00',
                          '31-02-2000', 'CE', '60000-000', 'Desenvolvedora',
                          'Groovy', '', '9'].join('\n') + '\n'

        when:
        String saida = executar(entrada)

        then:
        0 * candidatos._
        saida.contains('Digite uma data válida no formato dd-MM-yyyy.')
        saida.contains('Saindo...')
    }

    def 'fim da entrada durante um formulário encerra sem gravar dados incompletos'() {
        when:
        String saida = executar('5\nAna\n')

        then:
        0 * candidatos._
        saida.contains('Entrada encerrada.')
    }

    private static Candidato candidatoAtual(Integer id) {
        return new Candidato(
                id: id, nome: 'Ana', sobrenome: 'Silva', email: 'ana@example.com',
                cpf: '123.456.789-00', dataNascimento: LocalDate.of(2000, 1, 1),
                estado: 'CE', cep: '60000-000', descricao: 'Desenvolvedora',
                competencias: [new Competencia(id: 4, competencia: 'Groovy')]
        )
    }

    private static Empresa empresaAtual(Integer id) {
        return new Empresa(
                id: id, nome: 'Acme', emailCorporativo: 'contato@acme.com',
                cnpj: '12.345.678/0001-90', pais: 'Brasil', cep: '60000-000',
                descricao: 'Tecnologia'
        )
    }

    private static Vaga vagaAtual(Integer id) {
        return new Vaga(
                id: id, nome: 'Dev', descricao: 'Backend', endereco: 'Fortaleza',
                idEmpresa: 22, nomeEmpresa: 'Acme',
                competencias: [new Competencia(id: 5, competencia: 'Groovy')]
        )
    }

    private String executar(String entrada) {
        ByteArrayOutputStream saida = new ByteArrayOutputStream()
        ConsoleIO console = new ConsoleIO(new Scanner(entrada), new PrintStream(saida, true, 'UTF-8'))
        CandidatoController candidatoController = new CandidatoController(
                new CandidatoService(candidatos), new CandidatoView(console))
        EmpresaController empresaController = new EmpresaController(
                new EmpresaService(empresas), new EmpresaView(console))
        VagaController vagaController = new VagaController(
                new VagaService(vagas), new VagaView(console))

        new MenuController(new MenuView(console), candidatoController, empresaController, vagaController).iniciar()
        return saida.toString('UTF-8')
    }
}
