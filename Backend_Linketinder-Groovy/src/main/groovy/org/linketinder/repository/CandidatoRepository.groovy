package org.linketinder.repository

import org.linketinder.model.Candidato

class CandidatoRepository {

    final List<Candidato> listaCandidatos = new ArrayList<>()

    List<Candidato> listarCandidatos () {
        return listaCandidatos
    }

    void adicionar(Candidato candidate) {
        listaCandidatos.add(candidate)
    }
}
