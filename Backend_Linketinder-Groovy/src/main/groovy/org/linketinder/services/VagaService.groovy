package org.linketinder.services

import org.linketinder.dao.IVagaDAO
import org.linketinder.model.Vaga

class VagaService {
    private final IVagaDAO dao

    VagaService(IVagaDAO dao){
        this.dao = dao
    }

    Vaga criaVaga (Vaga vaga) {
        if(!vaga.nome || !vaga.descricao || !vaga.competencias || !vaga.endereco) {
            throw new IllegalArgumentException("Nenhum desses campos deve estar vazio: nome, descrição, competencias, endereço. Tente novamente!")
        }
        dao.inserirVaga(vaga)

        return vaga
    }

    void deletaVaga(int idVaga) {
        def vaga = dao.buscaPorIdVaga(idVaga)
        if(!vaga) {
            throw new IllegalArgumentException("Vaga nâo encontrada, verifique e tente novamente!")
        }
        dao.deletaVaga(idVaga)
    }

    Vaga atualizaVaga(Vaga vaga) {
        dao.atualizaVaga(vaga)
        return vaga
    }

    Vaga buscaVagaPorId(int idVaga) {
        Vaga vaga = dao.buscaPorIdVaga(idVaga)
        return vaga
    }
}
