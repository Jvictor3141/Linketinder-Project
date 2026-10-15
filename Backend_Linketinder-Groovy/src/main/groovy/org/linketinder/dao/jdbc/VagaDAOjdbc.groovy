package org.linketinder.dao.jdbc

import org.linketinder.dao.IVagaDAO
import org.linketinder.database.ConexaoDB
import org.linketinder.database.DatabaseOperation
import org.linketinder.database.PostgresErrorTranslator
import org.linketinder.model.Competencia
import org.linketinder.model.Vaga

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class VagaDAOjdbc implements IVagaDAO{

    private final competenciaDAO = new CompetenciaDAOjdbc()

    List<Vaga> listarVagas() {
        List<Vaga> vagas =[]
        String sql = "SELECT v.id, v.nome, v.descricao, v.endereco, e.nome AS empresa FROM vagas v JOIN empresa e ON e.id = v.id_empresa"

        ConexaoDB.conectar().withCloseable {
            conn -> conn.prepareStatement(sql).withCloseable {
                stmt -> stmt.executeQuery().withCloseable { rs ->
                    while(rs.next()) {
                        vagas << mapear(conn, rs)
                    }
                }
            }
        }

        return vagas
    }

    private Vaga mapear(Connection conn, ResultSet rs) {
        return new Vaga(
                id: rs.getInt("id"),
                nome: rs.getString("nome"),
                descricao: rs.getString("descricao"),
                endereco: rs.getString("endereco"),
                nomeEmpresa: rs.getString("empresa"),
                competencias: listaCompetenciaVaga(conn, rs.getInt("id"))
        )
    }

    private List<Competencia> listaCompetenciaVaga(Connection conn, int idVaga) {
        List<Competencia> lista = []
        String sql = "SELECT comp.id, comp.competencia FROM vaga_competencia vc JOIN competencias comp ON comp.id = vc.id_competencia WHERE vc.id_vaga = ?"

        conn.prepareStatement(sql).withCloseable { stmt ->
            stmt.setInt(1, idVaga)
            stmt.executeQuery().withCloseable {
                rs ->
                    while (rs.next()) {
                        lista << new Competencia(rs.getInt("id"), rs.getString("competencia"))
                    }
            }
        }

        return lista
    }

    Vaga inserirVaga(Vaga vaga) {
        String sql = "INSERT INTO vagas (nome, descricao, endereco, id_empresa) VALUES (?, ?, ?, ?) RETURNING ID"

        ConexaoDB.conectar().withCloseable {conn ->
            conn.autoCommit = false
            try {
                conn.prepareStatement(sql).withCloseable { stmt ->
                    stmt.setString(1, vaga.nome)
                    stmt.setString(2, vaga.descricao)
                    stmt.setString(3, vaga.endereco)
                    stmt.setInt(4, vaga.empresa)
                    stmt.executeQuery().withCloseable { rs ->
                        if (rs.next()) {
                            vaga.id = rs.getInt("id")
                        }
                    }
                }
                vaga.competencias?.each { comp ->

                    Competencia c = competenciaDAO.obterOuCriarCompetencia(conn, comp)
                    inserirRelacaoCompetenciaVaga(conn, vaga.id, c.id)
                }
                conn.commit()
            } catch (Exception e) {
                conn.rollback()
                vaga.id = null
                if (e instanceof SQLException) {
                    throw PostgresErrorTranslator.translate(e as SQLException, DatabaseOperation.CREATE)
                }
                throw e
            }
        }

        return vaga
    }

    private void inserirRelacaoCompetenciaVaga(Connection conn, int idVaga, int idCompetencia) {
        String sql = "INSERT INTO vaga_competencia (id_competencia, id_vaga) VALUES (?, ?)"

        conn.prepareStatement(sql).withCloseable { stmt ->
            stmt.setInt(1, idCompetencia)
            stmt.setInt(2, idVaga)
            stmt.executeUpdate()
        }

    }

    void deletaVaga(Integer idVaga) {
        String sql = "DELETE FROM vagas WHERE id = ?"

        try {
            ConexaoDB.conectar().withCloseable { conn ->
                conn.prepareStatement(sql).withCloseable { stmt ->
                    stmt.setInt(1, idVaga)
                    stmt.executeUpdate()
                }
            }
        } catch (SQLException e) {
            throw PostgresErrorTranslator.translate(e, DatabaseOperation.DELETE)
        }
    }

    Vaga buscaPorIdVaga(Integer idVaga) {
        Vaga vaga
        String sql = "SELECT v.id, v.nome, v.descricao, v.endereco, e.nome AS empresa FROM vagas v JOIN empresa e ON v.id_empresa = e.id WHERE v.id = ? "

        ConexaoDB.conectar().withCloseable { conn ->
            conn.prepareStatement(sql).withCloseable { stmt ->
                stmt.setInt(1, idVaga)
                stmt.executeQuery().withCloseable { rs ->
                    if(rs.next()){
                        vaga = mapear(conn, rs)
                    }
                }
            }
        }

        return vaga
    }

    Vaga atualizaVaga(Vaga vaga) {
        String sql = "UPDATE vagas SET nome = ?, descricao = ?, endereco = ? WHERE id = ?"

        ConexaoDB.conectar().withCloseable {conn ->
            conn.autoCommit = false
            try {
                conn.prepareStatement(sql).withCloseable { stmt ->
                    stmt.setString(1, vaga.nome)
                    stmt.setString(2, vaga.descricao)
                    stmt.setString(3, vaga.endereco)
                    stmt.setInt(4, vaga.id)
                    int afetadas = stmt.executeUpdate()
                    if (afetadas == 0) {
                        throw new IllegalStateException("Vaga não encontrada.")
                    }
                }

                if (vaga.competencias != null) {
                    apagarVinculos(conn, vaga.id)
                    vaga.competencias?.each { comp ->

                        Competencia c = competenciaDAO.obterOuCriarCompetencia(conn, comp)
                        inserirRelacaoCompetenciaVaga(conn, vaga.id, c.id)
                    }
                }
                conn.commit()
            } catch (Exception e) {
                conn.rollback()
                if (e instanceof SQLException) {
                    throw PostgresErrorTranslator.translate(e as SQLException, DatabaseOperation.UPDATE)
                }
                throw e
            }
        }

        return vaga
    }

    private void apagarVinculos(Connection conn, int idVaga) {
        String sql = "DELETE FROM vaga_competencia WHERE id_vaga = ?"

        conn.prepareStatement(sql).withCloseable {stmt ->
            stmt.setInt(1, idVaga)
            stmt.executeUpdate()
        }
    }
}
