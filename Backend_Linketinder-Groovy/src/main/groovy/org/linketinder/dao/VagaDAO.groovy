package org.linketinder.dao

import org.linketinder.database.ConexaoDB
import org.linketinder.model.Competencia
import org.linketinder.model.Vagas

import java.sql.ResultSet

class VagaDAO {

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
                empresa: rs.getString("empresa"),
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
}
