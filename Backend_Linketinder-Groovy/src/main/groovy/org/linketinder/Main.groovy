package org.linketinder

import org.linketinder.controller.CandidatoController
import org.linketinder.controller.EmpresaController
import org.linketinder.controller.MenuController
import org.linketinder.controller.VagaController
import org.linketinder.dao.ICompetenciaDAO
import org.linketinder.dao.jdbc.CandidatoDAOjdbc
import org.linketinder.dao.jdbc.CompetenciaDAOjdbc
import org.linketinder.dao.jdbc.EmpresaDAOjdbc
import org.linketinder.dao.jdbc.VagaDAOjdbc
import org.linketinder.database.ConexaoDB
import org.linketinder.database.IConexaoFactory
import org.linketinder.database.IDatabaseErrorTranslator
import org.linketinder.database.PostgresErrorTranslator
import org.linketinder.services.CandidatoService
import org.linketinder.services.EmpresaService
import org.linketinder.services.VagaService
import org.linketinder.ui.CandidatoView
import org.linketinder.ui.ConsoleIO
import org.linketinder.ui.EmpresaView
import org.linketinder.ui.MenuView
import org.linketinder.ui.VagaView

static void main(String[] args) {
    IConexaoFactory conexaoFactory = new ConexaoDB()
    IDatabaseErrorTranslator tradutorErros = new PostgresErrorTranslator()
    ICompetenciaDAO competenciaDAO = new CompetenciaDAOjdbc(conexaoFactory, tradutorErros)

    CandidatoService candidatoService = new CandidatoService(new CandidatoDAOjdbc(conexaoFactory, competenciaDAO, tradutorErros))
    EmpresaService empresaService = new EmpresaService(new EmpresaDAOjdbc(conexaoFactory, tradutorErros))
    VagaService vagaService = new VagaService(new VagaDAOjdbc(conexaoFactory, competenciaDAO, tradutorErros))

    ConsoleIO console = new ConsoleIO(new Scanner(System.in), System.out)
    CandidatoController candidatos = new CandidatoController(candidatoService, new CandidatoView(console))
    EmpresaController empresas = new EmpresaController(empresaService, new EmpresaView(console))
    VagaController vagas = new VagaController(vagaService, new VagaView(console))

    new MenuController(new MenuView(console), candidatos, empresas, vagas).iniciar()
}
