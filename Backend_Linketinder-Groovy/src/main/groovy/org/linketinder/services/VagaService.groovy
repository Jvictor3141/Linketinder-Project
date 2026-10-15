package org.linketinder.services

import org.linketinder.dao.jdbc.VagaDAOjdbc
import org.linketinder.model.Vaga

class VagaService {
    private final VagaDAOjdbc dao

    VagaService(VagaDAOjdbc dao = new VagaDAOjdbc()){
        this.dao = dao
    }

    Vaga createVaga (Vaga vaga) {
        if(!vaga.nome || !vaga.descricao || !vaga.competencias || !vaga.endereco) {
            throw new IllegalArgumentException("Nenhum desses campos deve estar vazio: nome, descrição, competencias, endereço. Tente novamente!")
        }
        dao.inserirVaga(vaga)

        return vaga
    }

    void delVaga(int idVaga) {
        def vaga = dao.buscaPorIdVaga(idVaga)
        if(!vaga) {
            throw new IllegalArgumentException("Vaga nâo encontrada, verifique e tente novamente!")
        }
        dao.deletaVaga(idVaga)
    }

    Vaga attVaga(Vaga vaga) {
        dao.atualizaVaga(vaga)
        return vaga
    }

    Vaga buscaPorId(int idVaga) {
        Vaga vaga = dao.buscaPorIdVaga(idVaga)
        return vaga
    }
}
