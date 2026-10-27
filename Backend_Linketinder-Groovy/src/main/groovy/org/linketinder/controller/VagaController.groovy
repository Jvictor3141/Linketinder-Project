package org.linketinder.controller

import org.linketinder.model.Vaga
import org.linketinder.services.VagaService
import org.linketinder.view.VagaView

class VagaController {

    private final VagaService service
    private final VagaView view

    VagaController(VagaService service, VagaView view) {
        this.service = service
        this.view = view
    }

    void listar() {
        view.exibirLista(service.listaVagas())
    }

    void criar() {
        Vaga vaga = new Vaga(view.lerCadastro())
        Vaga criada = service.criaVaga(vaga)
        view.exibirMensagem("Vaga ${criada.nome} criada com sucesso!")
    }

    void atualizar(Integer id) {
        Vaga atual = service.buscaVagaPorId(id)
        Vaga alterada = new Vaga(view.lerEdicao(atual) + [id: atual.id])
        Vaga atualizada = service.atualizaVaga(alterada)
        view.exibirMensagem("Vaga ${atualizada.nome} atualizada com sucesso!")
    }

    void excluir(Integer id) {
        service.deletaVaga(id)
        view.exibirMensagem('Vaga excluída com sucesso!')
    }
}
