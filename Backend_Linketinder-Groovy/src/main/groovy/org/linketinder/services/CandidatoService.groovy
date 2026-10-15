package org.linketinder.services

import org.linketinder.dao.jdbc.CandidatoDAOjdbc
import org.linketinder.model.Candidato

import java.time.LocalDate
import java.time.Period

class CandidatoService {

    private final CandidatoDAOjdbc dao

    CandidatoService(CandidatoDAOjdbc dao = new CandidatoDAOjdbc()) {
        this.dao = dao
    }

    Candidato createCandidate ( Candidato candidato ) {

        if(!candidato.nome || !candidato.sobrenome || !candidato.email || !candidato.dataNascimento || !candidato.cpf || !candidato.competencias) {
            throw new IllegalArgumentException("Nenhum desses campos deve estar vazio: nome, email, cpf, idade, competências. Tente novamente!")
        }

        LocalDate dataNascimento = candidato.dataNascimento
        int idade = Period.between(dataNascimento, LocalDate.now()).getYears()

        if (idade < 18) {
            throw new IllegalArgumentException("O candidato deve ter 18 anos ou mais para se cadastrar!")
        }

        dao.inserirCandidato(candidato)

        return candidato
    }

    void delCandidato(int idCandidato) {
        def candidato = dao.buscaCandidatoPorId(idCandidato)
        if(!candidato) {
            throw new IllegalArgumentException("Candidato nâo encontrado, verifique e tente novamente!")
        }
        dao.deletaCandidato(idCandidato)
    }

    Candidato atualizaCandidato(Candidato candidato) {
        dao.atualizaCandidato(candidato)
        return candidato
    }

    Candidato buscaPorId(int idCandidato) {
        Candidato candidato = dao.buscaCandidatoPorId(idCandidato)
        return candidato
    }
}
