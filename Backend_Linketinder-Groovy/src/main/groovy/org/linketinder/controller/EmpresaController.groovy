package org.linketinder.controller

import org.linketinder.model.Empresa
import org.linketinder.services.EmpresaService
import org.linketinder.view.EmpresaView

class EmpresaController {

    private final EmpresaService service
    private final EmpresaView view

    EmpresaController(EmpresaService service, EmpresaView view) {
        this.service = service
        this.view = view
    }

    void listar() {
        view.exibirLista(service.listaEmpresas())
    }

    void criar() {
        Empresa empresa = new Empresa(view.lerCadastro())
        Empresa criada = service.criaEmpresa(empresa)
        view.exibirMensagem("Empresa ${criada.nome} criada com sucesso!")
    }

    void atualizar(Integer id) {
        Empresa atual = service.buscaEmpresaPorId(id)
        Empresa alterada = new Empresa(view.lerEdicao(atual) + [id: atual.id])
        Empresa atualizada = service.atualizaEmpresa(alterada)
        view.exibirMensagem("Empresa ${atualizada.nome} atualizada com sucesso!")
    }

    void excluir(Integer id) {
        service.deletaEmpresa(id)
        view.exibirMensagem('Empresa excluída com sucesso!')
    }
}
