package org.linketinder.dao

import org.linketinder.database.ConexaoDB
import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.postgresql.util.PSQLException

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class CandidatoDAO {

    private final competenciaDAO = new CompetenciaDAO()

    List<Candidato> listarCandidato() {
        List<Candidato> candidatos = []

        String sql = "SELECT id, nome, sobrenome, data_nascimento, e_mail, cpf, estado, cep, descricao FROM candidato"

        ConexaoDB.conectar().withCloseable {
            conn -> conn.prepareStatement(sql).withCloseable {
            stmt -> stmt.executeQuery().withCloseable {
                rs -> while (rs.next()){
                    candidatos << mapear(rs)
                }}}
        }
        return candidatos
    }

    private Candidato mapear (ResultSet rs) {
        return new Candidato(
                id: rs.getInt("id"),
                nome: rs.getString("nome"),
                sobrenome: rs.getString("sobrenome"),
                email: rs.getString("e_mail"),
                cpf: rs.getString("cpf"),
                dataNascimento: rs.getDate("data_nascimento").toLocalDate(),
                estado: rs.getString("estado"),
                cep: rs.getString("cep"),
                descricao: rs.getString("descricao"),
                competencias: listaCompetencia(rs.getInt("id"))
        )
    }

    private List<Competencia> listaCompetencia(int idCandidato) {
        List<Competencia> lista = []
        String sql = "SELECT comp.id, comp.competencia FROM candidato_competencia cc JOIN competencias comp ON comp.id = cc.id_competencia WHERE cc.id_candidato = ?"

        ConexaoDB.conectar().withCloseable {
            conn -> conn.prepareStatement(sql).withCloseable { stmt ->
                stmt.setInt(1, idCandidato)
                stmt.executeQuery().withCloseable {
                    rs -> while (rs.next()) {
                        lista << new Competencia(rs.getInt("id"), rs.getString("competencia"))
                    }
                }
            }
        }

        return  lista
    }

    Candidato inserir(Candidato candidato) {
        String sql = "INSERT INTO candidato (nome, sobrenome, e_mail, cpf, data_nascimento, estado, cep, descricao) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID"

        ConexaoDB.conectar().withCloseable {conn ->
            conn.autoCommit = false
            try {
                conn.prepareStatement(sql).withCloseable { stmt ->
                    stmt.setString(1, candidato.nome)
                    stmt.setString(2, candidato.sobrenome)
                    stmt.setString(3, candidato.email)
                    stmt.setString(4, candidato.cpf)
                    stmt.setDate(5, java.sql.Date.valueOf(candidato.dataNascimento))
                    stmt.setString(6, candidato.estado)
                    stmt.setString(7, candidato.cep)
                    stmt.setString(8, candidato.descricao)
                    stmt.executeQuery().withCloseable { rs ->
                        if (rs.next()) {
                            candidato.id = rs.getInt("id")
                        }
                    }
                }
                candidato.competencias?.each { comp ->

                    Competencia c = competenciaDAO.obterOuCriar(conn, comp)
                    inserirRelacaoCompetencia(conn, candidato.id, c.id)
                }
                conn.commit()
            } catch (Exception e) {
                conn.rollback()
                candidato.id = null
                if (e instanceof SQLException) {
                    throw traduzirErro(e as SQLException)
                }
                throw e
            }
        }

        return candidato
    }

    private void inserirRelacaoCompetencia(Connection conn, int idCandidato, int idCompetencia) {
        String sql = "INSERT INTO candidato_competencia (id_candidato, id_competencia) VALUES (?, ?)"

        conn.prepareStatement(sql).withCloseable { stmt ->
            stmt.setInt(1, idCandidato)
            stmt.setInt(2, idCompetencia)
            stmt.executeUpdate()
        }

    }

    private Exception traduzirErro(SQLException e) {
        switch (e.getSQLState()) {
            case "23505":
                if (e.message.contains("(cpf)")) {
                    return new IllegalStateException("CPF já cadastrado.", e)
                }
                return new IllegalStateException("Competência repetida na lista do candidato.", e)
            case "23503":
                return new IllegalStateException("Competência inexistente.", e)
            default:
                return e
        }
    }
}
