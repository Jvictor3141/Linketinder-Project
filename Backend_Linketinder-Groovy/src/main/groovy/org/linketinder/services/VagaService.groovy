package org.linketinder.services

import org.linketinder.dao.VagaDAO
import org.linketinder.model.Vagas

class VagaService {
    private final VagaDAO dao

    VagaService(VagaDAO dao = new VagaDAO()){
        this.dao = dao
    }

    Vagas createVaga (Vagas vaga) {
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

    Vagas attVaga(Vagas vaga) {
        dao.attVagas(vaga)
        return vaga
    }

    Vagas buscaPorId(int idVaga) {
        Vagas vaga = dao.buscaPorId(idVaga)
        return vaga
    }
}
