package org.linketinder.dao

import org.linketinder.database.ConexaoDB
import org.linketinder.model.Candidato

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
                descricao: rs.getString("descricao")
        )
    }
}
