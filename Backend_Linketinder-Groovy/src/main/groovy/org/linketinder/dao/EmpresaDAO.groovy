package org.linketinder.dao

import org.linketinder.database.ConexaoDB
import org.linketinder.model.Empresa

import java.sql.ResultSet

class EmpresaDAO {

    List<Empresa> listarEmpresas() {
        List<Empresa> listar = []
        String sql = "SELECT id, nome, cnpj, e_mail, descricao, pais, cep FROM empresa"

        ConexaoDB.conectar().withCloseable {
            conn -> conn.prepareStatement(sql).withCloseable {
                stmt -> stmt.executeQuery().withCloseable {
                    rs -> while (rs.next()){
                        listar << mapear(rs)
                    }
                }
            }
        }

        return listar
    }

    private Empresa mapear (ResultSet rs) {
        return new Empresa(
                id: rs.getInt("id"),
                nome: rs.getString("nome"),
                emailCorporativo: rs.getString("e_mail"),
                cnpj: rs.getString("cnpj"),
                pais: rs.getString("pais"),
                cep: rs.getString("cep"),
                descricao: rs.getString("descricao")
        )
    }
}
