package org.linketinder.controller

import org.linketinder.model.Candidato
import org.linketinder.services.CandidatoService
import org.linketinder.ui.CandidatoView

class CandidatoController {

    private final CandidatoService service
    private final CandidatoView view

    CandidatoController(CandidatoService service, CandidatoView view) {
        this.service = service
        this.view = view
    }

    void listar() {
        view.exibirLista(service.listaCandidatos())
    }

    void criar() {
        Candidato candidato = new Candidato(view.lerCadastro())
        Candidato criado = service.criaCandidato(candidato)
        view.exibirMensagem("Candidato ${criado.nome} criado com sucesso!")
    }

    void atualizar(Integer id) {
        Candidato atual = service.buscaCandidatoPorId(id)
        Candidato candidato = new Candidato(view.lerEdicao(atual) + [id: atual.id])
        Candidato atualizado = service.atualizaCandidato(candidato)
        view.exibirMensagem("Candidato ${atualizado.nome} atualizado com sucesso!")
    }

    void excluir(Integer id) {
        service.deletaCandidato(id)
        view.exibirMensagem('Candidato excluído com sucesso!')
    }
}
