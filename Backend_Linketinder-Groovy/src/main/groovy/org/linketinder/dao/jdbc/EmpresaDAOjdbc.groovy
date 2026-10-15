package org.linketinder.dao.jdbc

import org.linketinder.dao.IEmpresaDAO
import org.linketinder.database.ConexaoDB
import org.linketinder.model.Empresa

import java.sql.ResultSet
import java.sql.SQLException

class EmpresaDAOjdbc implements IEmpresaDAO{

    List<Empresa> listarEmpresas() {
        List<Empresa> empresas = []
        String sql = "SELECT id, nome, cnpj, e_mail, descricao, pais, cep FROM empresa"

        ConexaoDB.conectar().withCloseable {
            conn -> conn.prepareStatement(sql).withCloseable {
                stmt -> stmt.executeQuery().withCloseable {
                    rs -> while (rs.next()){
                        empresas << mapear(rs)
                    }
                }
            }
        }

        return empresas
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

    Empresa inserirEmpresa(Empresa empresa) {
        String sql = "INSERT INTO empresa (nome, cnpj, e_mail, descricao, pais, cep) VALUES (?, ?, ?, ?, ?, ?) RETURNING ID"

        ConexaoDB.conectar().withCloseable {conn ->
            conn.autoCommit = false
            try {
                conn.prepareStatement(sql).withCloseable { stmt ->
                    stmt.setString(1, empresa.nome)
                    stmt.setString(2, empresa.cnpj)
                    stmt.setString(3, empresa.emailCorporativo)
                    stmt.setString(4, empresa.descricao)
                    stmt.setString(5, empresa.pais)
                    stmt.setString(6, empresa.cep)
                    stmt.executeQuery().withCloseable { rs ->
                        if (rs.next()) {
                            empresa.id = rs.getInt("id")
                        }
                    }
                }
                conn.commit()
            } catch (Exception e) {
                conn.rollback()
                empresa.id = null
                if (e instanceof SQLException) {
                    throw traduzirErro(e as SQLException)
                }
                throw e
            }
        }

        return empresa
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

    void deletaEmpresa(Integer idEmpresa) {
        String sql = "DELETE FROM empresa WHERE id = ?"

        ConexaoDB.conectar().withCloseable { conn ->
            conn.prepareStatement(sql).withCloseable { stmt ->
                stmt.setInt(1, idEmpresa)
                stmt.executeUpdate()
            }
        }
    }

    Empresa buscaPorIdEmpresa(Integer idEmpresa) {
        Empresa empresa
        String sql = "SELECT id, nome, cnpj, e_mail, descricao, pais, cep FROM empresa WHERE id = ?"

        ConexaoDB.conectar().withCloseable { conn ->
            conn.prepareStatement(sql).withCloseable { stmt ->
                stmt.setInt(1, idEmpresa)
                stmt.executeQuery().withCloseable { rs ->
                    if(rs.next()){
                        empresa = mapear(rs)
                    }
                }
            }
        }

        return empresa
    }

    Empresa atualizaEmpresa(Empresa empresa) {
        String sql = "UPDATE empresa SET nome = ?, cnpj = ?, e_mail = ?, descricao = ?, pais = ?, cep = ? WHERE id = ?"

        ConexaoDB.conectar().withCloseable {conn ->
            conn.autoCommit = false
            try {
                conn.prepareStatement(sql).withCloseable { stmt ->
                    stmt.setString(1, empresa.nome)
                    stmt.setString(2, empresa.cnpj)
                    stmt.setString(3, empresa.emailCorporativo)
                    stmt.setString(4, empresa.descricao)
                    stmt.setString(5, empresa.pais)
                    stmt.setString(6, empresa.cep)
                    stmt.setInt(7, empresa.id)
                    stmt.executeUpdate()

                    int afetadas = stmt.executeUpdate()
                    if (afetadas == 0) {
                        throw new IllegalStateException("Empresa não encontrado.")
                    }
                }
                conn.commit()
            } catch (Exception e) {
                conn.rollback()
                if (e instanceof SQLException) {
                    throw traduzirErro(e as SQLException)
                }
                throw e
            }
        }

        return empresa
    }
}
