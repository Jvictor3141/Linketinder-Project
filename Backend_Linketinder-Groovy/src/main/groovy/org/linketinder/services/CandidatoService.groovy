package org.linketinder.services

import org.linketinder.model.Candidato
import org.linketinder.repository.CandidatoRepository

class CandidatoService {

    final CandidatoRepository repository

    CandidatoService (CandidatoRepository repository) {
        this.repository = repository
    }

    Candidato createCandidate (
            String nome,
            String email,
            String cpf,
            int idade,
            String estado,
            String cep,
            String descricao,
            List<String> competencias
    ) {
        if(!nome || !email || !cpf || !idade || !competencias) {
            throw new IllegalArgumentException("Nenhum desses campos deve estar vazio: nome, email, cpf, idade, competências. Tente novamente!")
        }
        Candidato candidate = new Candidato(nome, email, cpf, idade, estado, cep, descricao, competencias)

        repository.adicionar(candidate)
        return candidate
    }
}
