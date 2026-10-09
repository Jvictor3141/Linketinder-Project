package org.linketinder.dao

import org.linketinder.database.ConexaoDB
import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.linketinder.model.Empresa
import org.linketinder.model.Vagas

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class VagaDAO {

    private final competenciaDAO = new CompetenciaDAO()

    List<Vagas> listarVagas() {
        List<Vagas> lista =[]
        String sql = "SELECT v.id, v.nome, v.descricao, v.endereco, e.nome AS empresa FROM vagas v JOIN empresa e ON e.id = v.id_empresa"

        ConexaoDB.conectar().withCloseable {
            conn -> conn.prepareStatement(sql).withCloseable {
                stmt -> stmt.executeQuery().withCloseable { rs ->
                    while(rs.next()) {
                        lista << mapear(rs)
                    }
                }
            }
        }

        return lista
    }

    private Vagas mapear(ResultSet rs) {
        return new Vagas(
                id: rs.getInt("id"),
                nome: rs.getString("nome"),
                descricao: rs.getString("descricao"),
                endereco: rs.getString("endereco"),
                nomeEmpresa: rs.getString("empresa"),
                competencias: listaCompetencia(rs.getInt("id"))
        )
    }

    private List<Competencia> listaCompetencia(int idVaga) {
        List<Competencia> lista = []
        String sql = "SELECT comp.id, comp.competencia FROM vaga_competencia vc JOIN competencias comp ON comp.id = vc.id_competencia WHERE vc.id_vaga = ?"

        ConexaoDB.conectar().withCloseable {
            conn ->
                conn.prepareStatement(sql).withCloseable { stmt ->
                    stmt.setInt(1, idVaga)
                    stmt.executeQuery().withCloseable {
                        rs ->
                            while (rs.next()) {
                                lista << new Competencia(rs.getInt("id"), rs.getString("competencia"))
                            }
                    }
                }
        }

        return lista
    }

    Vagas inserir(Vagas vaga) {
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

                    Competencia c = competenciaDAO.obterOuCriar(conn, comp)
                    inserirRelacaoCompetencia(conn, vaga.id, c.id)
                }
                conn.commit()
            } catch (Exception e) {
                conn.rollback()
                vaga.id = null
                if (e instanceof SQLException) {
                    throw traduzirErro(e as SQLException)
                }
                throw e
            }
        }

        return vaga
    }

    private void inserirRelacaoCompetencia(Connection conn, int idVaga, int idCompetencia) {
        String sql = "INSERT INTO vaga_competencia (id_competencia, id_vaga) VALUES (?, ?)"

        conn.prepareStatement(sql).withCloseable { stmt ->
            stmt.setInt(1, idCompetencia)
            stmt.setInt(2, idVaga)
            stmt.executeUpdate()
        }

    }

    private Exception traduzirErro(SQLException e) {
        switch (e.getSQLState()) {
            case "23505":
                return new IllegalStateException("Competência repetida na lista do candidato.", e)
            case "23503":
                return new IllegalStateException("Competência inexistente.", e)
            default:
                return e
        }
    }

    void deletaVaga(Integer idVaga) {
        String sql = "DELETE FROM vagas WHERE id = ?"

        ConexaoDB.conectar().withCloseable { conn ->
            conn.prepareStatement(sql).withCloseable { stmt ->
                stmt.setInt(1, idVaga)
                stmt.executeUpdate()
            }
        }
    }

    Vagas buscaPorId(Integer idVaga) {
        Vagas vaga
        String sql = "SELECT v.id, v.nome, v.descricao, v.endereco, e.nome AS empresa FROM vagas v JOIN empresa e ON v.id_empresa = e.id WHERE v.id = ? "

        ConexaoDB.conectar().withCloseable { conn ->
            conn.prepareStatement(sql).withCloseable { stmt ->
                stmt.setInt(1, idVaga)
                stmt.executeQuery().withCloseable { rs ->
                    if(rs.next()){
                        vaga = mapear(rs)
                    }
                }
            }
        }

        return vaga
    }

    Vagas attVagas(Vagas vaga) {
        String sql = "UPDATE vagas SET nome = ?, descricao = ?, endereco = ? WHERE id = ?"

        ConexaoDB.conectar().withCloseable {conn ->
            conn.autoCommit = false
            try {
                conn.prepareStatement(sql).withCloseable { stmt ->
                    stmt.setString(1, vaga.nome)
                    stmt.setString(2, vaga.descricao)
                    stmt.setString(3, vaga.endereco)
                    stmt.setInt(4, vaga.id)
                    stmt.executeUpdate()

                    int afetadas = stmt.executeUpdate()
                    if (afetadas == 0) {
                        throw new IllegalStateException("Vaga não encontrada.")
                    }
                }

                if (vaga.competencias != null) {
                    apagarVinculos(conn, vaga.id)
                    vaga.competencias?.each { comp ->

                        Competencia c = competenciaDAO.obterOuCriar(conn, comp)
                        inserirRelacaoCompetencia(conn, vaga.id, c.id)
                    }
                }
                conn.commit()
            } catch (Exception e) {
                conn.rollback()
                vaga.id = null
                if (e instanceof SQLException) {
                    throw traduzirErro(e as SQLException)
                }
                throw e
            }
        }

        return vaga
    }

    private void apagarVinculos(Connection conn, int idVaga) {
        String sql = "DELETE FROM vaga_competencia WHERE id_candidato = ?"

        conn.prepareStatement(sql).withCloseable {stmt ->
            stmt.setInt(1, idVaga)
            stmt.executeUpdate()
        }
    }
}
