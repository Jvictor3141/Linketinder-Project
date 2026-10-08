package org.linketinder.dao

import org.linketinder.database.ConexaoDB
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
                empresa: rs.getString("empresa")
        )
    }
}
