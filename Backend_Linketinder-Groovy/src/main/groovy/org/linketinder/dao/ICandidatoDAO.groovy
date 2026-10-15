package org.linketinder.dao

import org.linketinder.model.Candidato

interface ICandidatoDAO {
    List<Candidato> listarCandidatos()
    Candidato inserirCandidato(Candidato candidato)
    Candidato buscaCandidatoPorId(Integer idCandidato)
    void deletaCandidato(Integer idCandidato)
    Candidato atualizaCandidato(Candidato candidato)

}