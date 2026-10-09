package org.linketinder.services

import org.linketinder.dao.CandidatoDAO
import org.linketinder.model.Candidato

import java.time.LocalDate
import java.time.Period

class CandidatoService {

    private final CandidatoDAO dao

    CandidatoService(CandidatoDAO dao = new CandidatoDAO()) {
        this.dao = dao
    }

    Candidato createCandidate ( Candidato candidato ) {

        if(!candidato.nome || !candidato.email || !candidato.dataNascimento || !candidato.cpf || !candidato.competencias) {
            throw new IllegalArgumentException("Nenhum desses campos deve estar vazio: nome, email, cpf, idade, competências. Tente novamente!")
        }

        LocalDate dataNascimento = candidato.dataNascimento
        int idade = Period.between(dataNascimento, LocalDate.now()).getYears()

        if (idade < 18) {
            throw new IllegalArgumentException("O candidato deve ter 18 anos ou mais para se cadastrar!")
        }

        dao.inserir(candidato)

        return candidato
    }
}
