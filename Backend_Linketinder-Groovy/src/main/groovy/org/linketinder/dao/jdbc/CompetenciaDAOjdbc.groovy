package org.linketinder.dao.jdbc

import org.linketinder.dao.ICompetenciaDAO
import org.linketinder.database.DatabaseOperation
import org.linketinder.database.IConexaoFactory
import org.linketinder.database.IDatabaseErrorTranslator
import org.linketinder.model.Competencia

import java.sql.Connection
import java.sql.SQLException

class CompetenciaDAOjdbc implements ICompetenciaDAO{

    private final IConexaoFactory conexaoFactory
    private final IDatabaseErrorTranslator tradutorErros

    CompetenciaDAOjdbc(IConexaoFactory conexaoFactory, IDatabaseErrorTranslator tradutorErros) {
        this.conexaoFactory = conexaoFactory
        this.tradutorErros = tradutorErros
    }

    List<Competencia> listarCompetencias() {
        List<Competencia> competencias = []
        String sql = "SELECT id, competencia FROM competencias"

        conexaoFactory.criarConexao().withCloseable {
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
        String sql = "INSERT INTO competencias (competencia) VALUES (?)"

        conn.prepareStatement(sql, ['id'] as String[]).withCloseable { stmt ->
            stmt.setString(1, competencia.competencia)
            stmt.executeUpdate()
            stmt.getGeneratedKeys().withCloseable { rs ->
                if (!rs.next()) {
                    throw new SQLException("O banco não retornou o ID da competência.")
                }
                int idGerado = rs.getInt(1)
                if (rs.wasNull()) {
                    throw new SQLException("O banco retornou um ID nulo para a competência.")
                }
                competencia.id = idGerado
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
            conexaoFactory.criarConexao().withCloseable { conn ->
                conn.prepareStatement(sql).withCloseable { stmt ->
                    stmt.setInt(1, idCompetencia)
                    stmt.executeUpdate()
                }
            }
        } catch (SQLException e) {
            throw tradutorErros.translate(e, DatabaseOperation.DELETE)
        }
    }

}
