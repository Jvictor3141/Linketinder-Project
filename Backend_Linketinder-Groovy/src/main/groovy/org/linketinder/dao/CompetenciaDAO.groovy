package org.linketinder.dao

import org.linketinder.database.ConexaoDB
import org.linketinder.model.Competencia

import java.sql.Connection

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

    Competencia inserir(Competencia competencia) {
        String sql = "INSERT INTO competencias (competencia) VALUES (?) RETURNING ID"

        ConexaoDB.conectar().withCloseable { conn ->
            conn.prepareStatement(sql).withCloseable { stmt ->
                stmt.setString(1, competencia.competencia)
                stmt.executeQuery().withCloseable { rs ->
                    if(rs.next()) {
                        competencia.id = rs.getInt("id")
                    }
                }
            }
        }

        return competencia
    }

    Competencia inserir(Connection conn, Competencia competencia) {
        String sql = "INSERT INTO competencias (competencia) VALUES (?) RETURNING ID"

        conn.prepareStatement(sql).withCloseable { stmt ->
            stmt.setString(1, competencia.competencia)
            stmt.executeQuery().withCloseable { rs ->
                if (rs.next()) {
                    competencia.id = rs.getInt("id")
                }
            }
        }
        return competencia
    }

    Integer buscarIdCompetencia(Connection conn, String nome) {
        String sql = "SELECT id FROM competencias WHERE lower(competencia) = lower(?)"

        conn.prepareStatement(sql).withCloseable { stmt ->
            stmt.setString(1, nome)
            stmt.executeQuery().withCloseable { rs ->
                if(rs.next()) {
                    return rs.getInt("id")
                }
            }
        }
    }

    Competencia obterOuCriar(Connection conn, Competencia competencia) {
        if(competencia.id) return competencia

        Integer competenciaId = buscarIdCompetencia(conn, competencia.competencia)
        if(competenciaId != null) {
            competencia.id = competenciaId
            return competencia
        } else {
            inserir(conn, competencia)
            return competencia
        }
    }

}
