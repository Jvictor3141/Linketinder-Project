package org.linketinder.controller

import org.linketinder.dao.ICandidatoDAO
import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.linketinder.services.CandidatoService
import org.linketinder.ui.CandidatoView
import spock.lang.Specification

import java.time.LocalDate

class CandidatoControllerSpec extends Specification {

    private static Map<String, Object> dadosValidos() {
        [nome: 'João', sobrenome: 'Silva', email: 'joao@email.com', cpf: '123.456.789-00',
         dataNascimento: LocalDate.of(2000, 1, 1), estado: 'CE', cep: '60000-000',
         descricao: 'Desenvolvedor', competencias: [new Competencia(id: 4, competencia: 'Java')]]
    }

    def 'cria o modelo a partir da entrada e informa sucesso após a gravação'() {
        given:
        ICandidatoDAO dao = Mock()
        CandidatoView view = Mock()
        CandidatoController controller = new CandidatoController(new CandidatoService(dao), view)

        when:
        controller.criar()

        then:
        1 * view.lerCadastro() >> dadosValidos()

        then:
        1 * dao.inserirCandidato({ Candidato candidato ->
            candidato.id == null && candidato.nome == 'João' && candidato.competencias*.competencia == ['Java']
        }) >> { Candidato candidato -> candidato.id = 9; candidato }

        then:
        1 * view.exibirMensagem('Candidato João criado com sucesso!')
    }

    def 'atualiza uma nova entidade mantendo o ID e sem enviar as competências preservadas ao DAO'() {
        given:
        ICandidatoDAO dao = Mock()
        CandidatoView view = Mock()
        Candidato atual = new Candidato(dadosValidos() + [id: 7])
        CandidatoController controller = new CandidatoController(new CandidatoService(dao), view)

        when:
        controller.atualizar(7)

        then:
        1 * dao.buscaCandidatoPorId(7) >> atual
        1 * view.lerEdicao(atual) >> (dadosValidos() + [nome: 'Maria', competencias: null])
        1 * dao.atualizaCandidato({ Candidato candidato ->
            !candidato.is(atual) && candidato.id == 7 && candidato.nome == 'Maria' && candidato.competencias == null
        }) >> { Candidato candidato -> candidato }
        1 * view.exibirMensagem('Candidato Maria atualizado com sucesso!')
        atual.nome == 'João'
        atual.competencias*.competencia == ['Java']
        atual.competencias*.id == [4]
    }

    def 'falha de gravação não anuncia sucesso nem modifica a entidade consultada'() {
        given:
        ICandidatoDAO dao = Mock()
        CandidatoView view = Mock()
        Candidato atual = new Candidato(dadosValidos() + [id: 7])
        CandidatoController controller = new CandidatoController(new CandidatoService(dao), view)

        when:
        controller.atualizar(7)

        then:
        1 * dao.buscaCandidatoPorId(7) >> atual
        1 * view.lerEdicao(atual) >> (dadosValidos() + [nome: 'Maria', competencias: null])
        1 * dao.atualizaCandidato(_) >> { throw new IllegalStateException('Falha ao salvar.') }
        0 * view.exibirMensagem(_)
        IllegalStateException erro = thrown()
        erro.message == 'Falha ao salvar.'
        atual.nome == 'João'
        atual.competencias*.id == [4]
    }

    def 'regra de nascimento permanece no serviço e impede a persistência'() {
        given:
        ICandidatoDAO dao = Mock()
        CandidatoView view = Mock()
        CandidatoController controller = new CandidatoController(new CandidatoService(dao), view)

        when:
        controller.criar()

        then:
        1 * view.lerCadastro() >> (dadosValidos() + [dataNascimento: LocalDate.now().plusDays(1)])
        0 * dao._
        0 * view.exibirMensagem(_)
        thrown(IllegalArgumentException)
    }
}
