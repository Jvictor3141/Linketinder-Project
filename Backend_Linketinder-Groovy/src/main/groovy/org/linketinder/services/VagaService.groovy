package org.linketinder.services

import org.linketinder.dao.IVagaDAO
import org.linketinder.model.Vaga

class VagaService {
    private final IVagaDAO dao

    VagaService(IVagaDAO dao){
        this.dao = dao
    }

    Vaga criaVaga (Vaga vaga) {
        validarDados(vaga, false)
        dao.inserirVaga(vaga)

        return vaga
    }

    List<Vaga> listaVagas() {
        return dao.listarVagas()
    }

    void deletaVaga(Integer idVaga) {
        ValidacaoService.validarIdPositivo('vaga', idVaga)
        Vaga vaga = dao.buscaPorIdVaga(idVaga)
        if(!vaga) {
            throw new IllegalArgumentException('Vaga não encontrada.')
        }
        dao.deletaVaga(idVaga)
    }

    Vaga atualizaVaga(Vaga vaga) {
        validarDados(vaga, true)
        dao.atualizaVaga(vaga)
        return vaga
    }

    Vaga buscaVagaPorId(Integer idVaga) {
        ValidacaoService.validarIdPositivo('vaga', idVaga)
        Vaga vaga = dao.buscaPorIdVaga(idVaga)
        if (!vaga) {
            throw new IllegalArgumentException('Vaga não encontrada.')
        }
        return vaga
    }

    private static void validarDados(Vaga vaga, boolean exigeId) {
        if (vaga == null) {
            throw new IllegalArgumentException('Os dados da vaga são obrigatórios.')
        }
        if (exigeId) {
            ValidacaoService.validarIdPositivo('vaga', vaga.id)
        }

        ValidacaoService.validarCamposObrigatorios('vaga', [
                'nome'       : vaga.nome,
                'descrição'  : vaga.descricao,
                'endereço'   : vaga.endereco
        ])
        ValidacaoService.validarIdPositivo('empresa associada à vaga', vaga.idEmpresa)
        ValidacaoService.validarCompetencias('vaga', vaga.competencias, !exigeId)
    }
}
