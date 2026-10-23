package org.linketinder.dao.jdbc

import org.linketinder.dao.ICompetenciaDAO
import org.linketinder.database.DatabaseOperation
import org.linketinder.database.IConexaoFactory
import org.linketinder.database.IDatabaseErrorTranslator
import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.linketinder.model.Empresa
import org.linketinder.model.Vaga
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate

class InjecaoConexaoSpec extends Specification {

    IConexaoFactory fabrica = Mock()
    ICompetenciaDAO competencias = Mock()
    IDatabaseErrorTranslator tradutor = Mock()
    Connection conexao = Mock()
    PreparedStatement statement = Mock()
    ResultSet resultado = Mock()

    @Unroll
    def "listagem de #tipo usa a fábrica recebida e fecha os recursos"() {
        given:
        def dao = criarDAO(tipo, fabrica, competencias, tradutor)

        when:
        def registros = dao."$metodo"()

        then:
        registros.empty
        1 * fabrica.criarConexao() >> conexao
        1 * conexao.prepareStatement(_ as String) >> statement
        1 * statement.executeQuery() >> resultado
        1 * resultado.next() >> false
        1 * resultado.close()
        1 * statement.close()
        1 * conexao.close()
        0 * conexao.commit()
        0 * conexao.rollback()
        0 * competencias._

        where:
        tipo          | metodo
        "candidato"   | "listarCandidatos"
        "empresa"     | "listarEmpresas"
        "vaga"        | "listarVagas"
        "competencia" | "listarCompetencias"
    }

    @Unroll
    def "inserção de #tipo compartilha a conexão com competências e confirma a transação"() {
        given:
        def competencia = new Competencia(competencia: "Groovy")
        def entidade = criarEntidade(tipo, competencia)
        def dao = criarDAO(tipo, fabrica, competencias, tradutor)
        PreparedStatement vinculo = Mock()

        when:
        def gravado = dao."$metodo"(entidade)

        then:
        gravado.is(entidade)
        gravado.id == 7
        1 * fabrica.criarConexao() >> conexao
        1 * conexao.setAutoCommit(false)
        1 * conexao.prepareStatement({ !it.toUpperCase().contains("RETURNING") }, { it instanceof String[] && it.toList() == ["id"] }) >> statement
        1 * conexao.prepareStatement(_ as String) >> vinculo
        1 * statement.executeUpdate() >> 1
        1 * statement.getGeneratedKeys() >> resultado
        1 * resultado.next() >> true
        1 * resultado.getInt(1) >> 7
        1 * competencias.obterOuCriarCompetencia(conexao, competencia) >> new Competencia(11, "Groovy")
        1 * vinculo.setInt(posicaoEntidade, 7)
        1 * vinculo.setInt(posicaoCompetencia, 11)
        1 * vinculo.executeUpdate() >> 1
        1 * resultado.close()
        1 * statement.close()
        1 * vinculo.close()
        1 * conexao.commit()
        0 * conexao.rollback()
        1 * conexao.close()

        where:
        tipo        | metodo             | posicaoEntidade | posicaoCompetencia
        "candidato" | "inserirCandidato" | 1               | 2
        "vaga"      | "inserirVaga"      | 2               | 1
    }

    @Unroll
    def "falha nas competências desfaz a inserção de #tipo e fecha a conexão"() {
        given:
        def competencia = new Competencia(competencia: "Groovy")
        def entidade = criarEntidade(tipo, competencia)
        def dao = criarDAO(tipo, fabrica, competencias, tradutor)
        def falha = new IllegalStateException("Falha ao gravar competência")

        when:
        dao."$metodo"(entidade)

        then:
        def erro = thrown(IllegalStateException)
        erro.is(falha)
        entidade.id == null
        1 * fabrica.criarConexao() >> conexao
        1 * conexao.setAutoCommit(false)
        1 * conexao.prepareStatement(_ as String, { it instanceof String[] && it.toList() == ["id"] }) >> statement
        1 * statement.executeUpdate() >> 1
        1 * statement.getGeneratedKeys() >> resultado
        1 * resultado.next() >> true
        1 * resultado.getInt(1) >> 7
        1 * competencias.obterOuCriarCompetencia(conexao, competencia) >> { throw falha }
        1 * resultado.close()
        1 * statement.close()
        0 * conexao.commit()
        1 * conexao.rollback()
        1 * conexao.close()

        where:
        tipo        | metodo
        "candidato" | "inserirCandidato"
        "vaga"      | "inserirVaga"
    }

    @Unroll
    def "competência existente=#existente usa a conexão recebida sem assumir sua transação"() {
        given:
        def dao = new CompetenciaDAOjdbc(fabrica, tradutor)
        def competencia = new Competencia(competencia: "Groovy")
        PreparedStatement insercao = Mock()
        ResultSet idInserido = Mock()

        when:
        def gravada = dao.obterOuCriarCompetencia(conexao, competencia)

        then:
        gravada.is(competencia)
        gravada.id == 11
        1 * conexao.prepareStatement(_ as String) >> statement
        (existente ? 0 : 1) * conexao.prepareStatement({ !it.toUpperCase().contains("RETURNING") }, { it instanceof String[] && it.toList() == ["id"] }) >> insercao
        1 * statement.setString(1, "Groovy")
        1 * statement.executeQuery() >> resultado
        1 * resultado.next() >> existente
        (existente ? 1 : 0) * resultado.getInt("id") >> 11
        1 * resultado.close()
        1 * statement.close()
        (existente ? 0 : 1) * insercao.setString(1, "Groovy")
        (existente ? 0 : 1) * insercao.executeUpdate() >> 1
        (existente ? 0 : 1) * insercao.getGeneratedKeys() >> idInserido
        (existente ? 0 : 1) * idInserido.next() >> true
        (existente ? 0 : 1) * idInserido.getInt(1) >> 11
        (existente ? 0 : 1) * idInserido.close()
        (existente ? 0 : 1) * insercao.close()
        0 * fabrica._
        0 * conexao.setAutoCommit(_)
        0 * conexao.commit()
        0 * conexao.rollback()
        0 * conexao.close()

        where:
        existente << [true, false]
    }

    def "inserção de empresa obtém o ID gerado por JDBC e confirma a transação"() {
        given:
        def empresa = criarEntidade("empresa")
        def dao = new EmpresaDAOjdbc(fabrica, tradutor)

        when:
        def gravada = dao.inserirEmpresa(empresa)

        then:
        gravada.is(empresa)
        gravada.id == 7
        1 * fabrica.criarConexao() >> conexao
        1 * conexao.setAutoCommit(false)
        1 * conexao.prepareStatement({ !it.toUpperCase().contains("RETURNING") }, { it instanceof String[] && it.toList() == ["id"] }) >> statement
        1 * statement.executeUpdate() >> 1
        1 * statement.getGeneratedKeys() >> resultado
        1 * resultado.next() >> true
        1 * resultado.getInt(1) >> 7
        1 * resultado.close()
        1 * statement.close()
        1 * conexao.commit()
        0 * conexao.rollback()
        1 * conexao.close()
        0 * tradutor._
    }

    @Unroll
    def "chave gerada #situacao desfaz a inserção de #tipo e fecha os recursos"() {
        given:
        def entidade = criarEntidade(tipo, new Competencia(competencia: "Groovy"))
        def dao = criarDAO(tipo, fabrica, competencias, tradutor)
        def traduzida = new IllegalStateException("Não foi possível obter a chave gerada")

        when:
        dao."$metodo"(entidade)

        then:
        def erro = thrown(IllegalStateException)
        erro.is(traduzida)
        entidade.id == null
        1 * fabrica.criarConexao() >> conexao
        1 * conexao.setAutoCommit(false)
        1 * conexao.prepareStatement(_ as String, { it instanceof String[] && it.toList() == ["id"] }) >> statement
        1 * statement.executeUpdate() >> 1
        1 * statement.getGeneratedKeys() >> resultado
        1 * resultado.next() >> presente
        (presente ? 1 : 0) * resultado.getInt(1) >> 0
        (presente ? 1 : 0) * resultado.wasNull() >> true
        1 * resultado.close()
        1 * statement.close()
        0 * conexao.commit()
        1 * conexao.rollback()
        1 * conexao.close()
        1 * tradutor.translate(_ as SQLException, DatabaseOperation.CREATE) >> traduzida
        0 * competencias._

        where:
        tipo        | metodo             | presente | situacao
        "candidato" | "inserirCandidato" | false    | "ausente"
        "candidato" | "inserirCandidato" | true     | "nula"
        "empresa"   | "inserirEmpresa"   | false    | "ausente"
        "empresa"   | "inserirEmpresa"   | true     | "nula"
        "vaga"      | "inserirVaga"      | false    | "ausente"
        "vaga"      | "inserirVaga"      | true     | "nula"
    }

    @Unroll
    def "#tipo delega erro SQL de #operacao ao tradutor injetado"() {
        given:
        def dao = criarDAO(tipo, fabrica, competencias, tradutor)
        def entidade = tipo == "competencia" ? null : criarEntidade(tipo)
        if (entidade != null) entidade.id = 7
        def argumento = operacao == DatabaseOperation.DELETE ? 7 : entidade
        def falha = new SQLException("Falha de escrita", "23000")
        def traduzida = new IllegalArgumentException("Erro traduzido")
        boolean transacional = operacao != DatabaseOperation.DELETE
        boolean criacao = operacao == DatabaseOperation.CREATE

        when:
        dao."$metodo"(argumento)

        then:
        def erro = thrown(IllegalArgumentException)
        erro.is(traduzida)
        1 * fabrica.criarConexao() >> conexao
        (transacional ? 1 : 0) * conexao.setAutoCommit(false)
        (criacao ? 1 : 0) * conexao.prepareStatement(_ as String, { it instanceof String[] && it.toList() == ["id"] }) >> statement
        (criacao ? 0 : 1) * conexao.prepareStatement(_ as String) >> statement
        1 * statement.executeUpdate() >> { throw falha }
        1 * statement.close()
        0 * conexao.commit()
        (transacional ? 1 : 0) * conexao.rollback()
        1 * conexao.close()
        1 * tradutor.translate(falha, operacao) >> traduzida
        0 * competencias._

        where:
        tipo          | metodo               | operacao
        "candidato"   | "inserirCandidato"   | DatabaseOperation.CREATE
        "candidato"   | "atualizaCandidato"  | DatabaseOperation.UPDATE
        "candidato"   | "deletaCandidato"    | DatabaseOperation.DELETE
        "empresa"     | "inserirEmpresa"     | DatabaseOperation.CREATE
        "empresa"     | "atualizaEmpresa"    | DatabaseOperation.UPDATE
        "empresa"     | "deletaEmpresa"      | DatabaseOperation.DELETE
        "vaga"        | "inserirVaga"        | DatabaseOperation.CREATE
        "vaga"        | "atualizaVaga"       | DatabaseOperation.UPDATE
        "vaga"        | "deletaVaga"         | DatabaseOperation.DELETE
        "competencia" | "deleteCompetencia"  | DatabaseOperation.DELETE
    }

    private static Object criarDAO(String tipo, IConexaoFactory fabrica, ICompetenciaDAO competencias,
                                   IDatabaseErrorTranslator tradutor) {
        switch (tipo) {
            case "candidato": return new CandidatoDAOjdbc(fabrica, competencias, tradutor)
            case "empresa": return new EmpresaDAOjdbc(fabrica, tradutor)
            case "vaga": return new VagaDAOjdbc(fabrica, competencias, tradutor)
            case "competencia": return new CompetenciaDAOjdbc(fabrica, tradutor)
            default: throw new IllegalArgumentException("DAO desconhecido: $tipo")
        }
    }

    private static Object criarEntidade(String tipo, Competencia competencia = null) {
        if (tipo == "candidato") {
            return new Candidato(nome: "João", sobrenome: "Silva", email: "joao@example.com",
                    cpf: "622.691.163-80", dataNascimento: LocalDate.of(2000, 1, 1),
                    estado: "CE", cep: "60000-000", descricao: "Desenvolvedor",
                    competencias: competencia == null ? null : [competencia])
        }
        if (tipo == "empresa") {
            return new Empresa(nome: "Empresa", emailCorporativo: "empresa@example.com",
                    cnpj: "11.222.333/0001-81", pais: "Brasil", cep: "60000-000",
                    descricao: "Tecnologia")
        }
        return new Vaga(nome: "Desenvolvedor", descricao: "Backend", endereco: "Fortaleza",
                idEmpresa: 3, competencias: competencia == null ? null : [competencia])
    }
}
