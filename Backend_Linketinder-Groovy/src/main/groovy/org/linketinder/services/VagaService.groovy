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
        dao.inserir(vaga)

        return vaga
    }

    void delVaga(int idVaga) {
        def vaga = dao.buscaPorId(idVaga)
        if(!vaga) {
            throw new IllegalArgumentException("Vaga nâo encontrada, verifique e tente novamente!")
        }
        dao.deletaVaga(idVaga)
    }

    Vaga attVaga(Vaga vaga) {
        dao.attVagas(vaga)
        return vaga
    }

    Vaga buscaPorId(int idVaga) {
        Vaga vaga = dao.buscaPorId(idVaga)
        return vaga
    }
}
