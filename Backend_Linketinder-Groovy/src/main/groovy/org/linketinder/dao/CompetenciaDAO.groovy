package org.linketinder.dao

import org.linketinder.database.ConexaoDB
import org.linketinder.model.Competencia

class CompetenciaDAO {

    List<Competencia> listarTodas() {
        List<Competencia> competencias = []
        String sql = "SELECT id, competencia FROM competencias"

        ConexaoDB.conectar().withCloseable {
            conn -> conn.prepareStatement(sql).withCloseable {
                stmt -> stmt.executeQuery().withCloseable {
                    rs -> while (rs.next()) {
                        competencias.add(new Competencia(rs.getInt("id"), rs.getString("competencia")))
                    }
                }
            }
        }
        return competencias
    }
}
