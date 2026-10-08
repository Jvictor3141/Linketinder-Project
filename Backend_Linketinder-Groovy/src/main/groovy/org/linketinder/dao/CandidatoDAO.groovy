package org.linketinder.dao

import org.linketinder.database.ConexaoDB
import org.linketinder.model.Candidato
import org.linketinder.model.Competencia

import java.sql.ResultSet

class CandidatoDAO {

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
}
