package org.linketinder.dao

import org.linketinder.model.Competencia

import java.sql.Connection

interface ICompetenciaDAO {
    List<Competencia> listarCompetencias()
    Competencia inserirCompetencia(Connection conn, Competencia competencia)
    Integer buscarIdCompetencia(Connection conn, String nome)
    Competencia obterOuCriarCompetencia(Connection conn, Competencia competencia)
    void deleteCompetencia(Integer idCompetencia)

}