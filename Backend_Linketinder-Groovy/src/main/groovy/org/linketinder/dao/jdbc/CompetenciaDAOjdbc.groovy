package org.linketinder.dao.jdbc

import org.linketinder.dao.ICompetenciaDAO
import org.linketinder.database.ConexaoDB
import org.linketinder.database.DatabaseOperation
import org.linketinder.database.PostgresErrorTranslator
import org.linketinder.model.Competencia

import java.sql.Connection
import java.sql.SQLException

class CompetenciaDAOjdbc implements ICompetenciaDAO{

    List<Competencia> listarCompetencias() {
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

    Competencia inserirCompetencia(Connection conn, Competencia competencia) {
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

    Competencia obterOuCriarCompetencia(Connection conn, Competencia competencia) {
        if(competencia.id) return competencia

        Integer competenciaId = buscarIdCompetencia(conn, competencia.competencia)
        if(competenciaId != null) {
            competencia.id = competenciaId
            return competencia
        } else {
            inserirCompetencia(conn, competencia)
            return competencia
        }
    }

    void deleteCompetencia(Integer idCompetencia) {
        String sql = "DELETE FROM competencias WHERE id = ?"

        try {
            ConexaoDB.conectar().withCloseable { conn ->
                conn.prepareStatement(sql).withCloseable { stmt ->
                    stmt.setInt(1, idCompetencia)
                    stmt.executeUpdate()
                }
            }
        } catch (SQLException e) {
            throw PostgresErrorTranslator.translate(e, DatabaseOperation.DELETE)
        }
    }

}
